package be.zsoft.system.monitor.ui.controller.view;

import be.zsoft.system.monitor.services.DiskService;
import be.zsoft.system.monitor.services.state.DiskUsageState;
import be.zsoft.system.monitor.services.utils.FixedSizeList;
import be.zsoft.system.monitor.ui.BaseUITest;
import be.zsoft.system.monitor.ui.service.DiskUsageSeriesBuilder;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.collections.ObservableList;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.util.ReflectionTestUtils;
import oshi.hardware.HWDiskStore;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiskUsageControllerTest extends BaseUITest {

    @Mock
    private DiskService diskService;

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private DiskUsageSeriesBuilder diskUsageSeriesBuilder;

    @Mock
    private Label titleLbl;

    @Mock
    private HBox headerContainer;

    @Mock
    private StackedAreaChart<Number, Number> diskUsageChart;

    @Mock
    private MFXComboBox<HWDiskStore> switchDiskCb;

    @Mock
    private DiskUsageState state;

    @InjectMocks
    private DiskUsageController diskUsageController;

    @Test
    void initialize() {
        HWDiskStore store1 = mock(HWDiskStore.class);
        HWDiskStore store2 = mock(HWDiskStore.class);
        List<HWDiskStore> diskStores = List.of(store1, store2);
        ObservableList<HWDiskStore> cbItems = mock(ObservableList.class);

        when(diskService.getAllDisk()).thenReturn(diskStores);
        when(switchDiskCb.getItems()).thenReturn(cbItems);

        diskUsageController.initialize();

        verify(switchDiskCb).setConverter(any(StringConverter.class));
        verify(cbItems).clear();
        verify(diskService).getAllDisk();
        verify(cbItems).addAll(diskStores);
        verify(switchDiskCb).selectFirst();
    }

    @Test
    void onSwitchDisk() {
        HWDiskStore store = mock(HWDiskStore.class);
        ScheduledFuture<?> dataUpdater = mock(ScheduledFuture.class);

        ReflectionTestUtils.setField(diskUsageController, "dataUpdater", dataUpdater);

        when(switchDiskCb.getSelectedItem()).thenReturn(store);
        when(store.getModel()).thenReturn("Disk name");

        diskUsageController.onSwitchDisk(null);

        verify(dataUpdater).cancel(true);
        verify(titleLbl).setText("Disk name");
        verify(taskScheduler).scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(1)));

        DiskUsageState usageState = (DiskUsageState) ReflectionTestUtils.getField(diskUsageController, "diskUsageState");
        assertThat(usageState).isNotNull();
    }

    @Test
    void dispose() {
        ScheduledFuture<?> dataUpdater = mock(ScheduledFuture.class);
        ReflectionTestUtils.setField(diskUsageController, "dataUpdater", dataUpdater);

        diskUsageController.dispose();

        verify(dataUpdater).cancel(true);

    }

    @Test
    void updateData() {
        ReflectionTestUtils.setField(diskUsageController, "diskUsageState", state);
        ReflectionTestUtils.invokeMethod(diskUsageController, "updateData");

        verify(state).updateData();
    }

    @Test
    void updateChart() {
        FixedSizeList<Long> readData = new FixedSizeList<>(60, 15L);
        FixedSizeList<Long> writeData = new FixedSizeList<>(60, 30L);
        XYChart.Series<Number, Number> readSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> writeSeries = new XYChart.Series<>();
        ObservableList<XYChart.Series<Number, Number>> chartChildren = mock(ObservableList.class);

        when(state.getReadBytesData()).thenReturn(readData);
        when(state.getWriteBytesData()).thenReturn(writeData);
        when(diskUsageSeriesBuilder.buildDiskUsageSeries("Read Usage", readData)).thenReturn(readSeries);
        when(diskUsageSeriesBuilder.buildDiskUsageSeries("Write Usage", writeData)).thenReturn(writeSeries);
        when(diskUsageChart.getData()).thenReturn(chartChildren);

        ReflectionTestUtils.setField(diskUsageController, "diskUsageState", state);
        ReflectionTestUtils.invokeMethod(diskUsageController, "updateChart");

        verify(chartChildren).clear();
        verify(chartChildren).add(readSeries);
        verify(chartChildren).add(writeSeries);
    }
}