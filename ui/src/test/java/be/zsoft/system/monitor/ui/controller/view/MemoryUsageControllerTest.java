package be.zsoft.system.monitor.ui.controller.view;

import be.zsoft.system.monitor.services.MemoryService;
import be.zsoft.system.monitor.services.state.MemoryUsageState;
import be.zsoft.system.monitor.ui.BaseUITest;
import be.zsoft.system.monitor.ui.controller.control.InfoButtonControl;
import be.zsoft.system.monitor.ui.service.DialogService;
import be.zsoft.system.monitor.ui.service.MemoryUsageSeriesBuilder;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.util.ReflectionTestUtils;
import oshi.hardware.GlobalMemory;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemoryUsageControllerTest extends BaseUITest {

    @Mock
    private MemoryService memoryService;

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private DialogService dialogService;

    @Mock
    private MemoryUsageSeriesBuilder memoryUsageSeriesBuilder;

    @Mock
    private AnchorPane rootPane;

    @Mock
    private HBox headerContainer;

    @Mock
    private Label titleLbl;

    @Mock
    private AreaChart<Number, Number> usageChart;

    @Mock
    private GlobalMemory memory;

    @Mock
    private MemoryUsageState state;

    @InjectMocks
    private MemoryUsageController memoryUsageController;

    @Test
    void initialize() {
        ObservableList<Node> headerContainerChildren = FXCollections.observableArrayList();

        when(memoryService.getMemory()).thenReturn(memory);
        when(memoryService.getUsedMemory()).thenReturn(100_000L);
        when(memoryService.getTotalMemory()).thenReturn(1_000_000L);
        when(headerContainer.getChildren()).thenReturn(headerContainerChildren);

        memoryUsageController.initialize();

        verify(memoryService).getMemory();
        verify(dialogService).createMemoryInfoDialog(memory, rootPane);
        verify(titleLbl).setText("Memory usage: 97.7 KiB / 976.6 KIB");
        verify(taskScheduler).scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(1)));

        assertThat(headerContainerChildren.get(0)).isInstanceOf(InfoButtonControl.class);
    }

    @Test
    void dispose() {
        ScheduledFuture<?> dataUpdater = mock(ScheduledFuture.class);
        ReflectionTestUtils.setField(memoryUsageController, "dataUpdater", dataUpdater);

        memoryUsageController.dispose();

        verify(dataUpdater).cancel(true);
    }

    @Test
    void updateData() {
        ReflectionTestUtils.setField(memoryUsageController, "memoryUsageState", state);
        ReflectionTestUtils.invokeMethod(memoryUsageController, "updateData");

        verify(state).updateData();
    }

    @Test
    void updateChartAndTitle() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        ObservableList<XYChart.Series<Number, Number>> chartChildren = mock(ObservableList.class);

        when(memoryService.getUsedMemory()).thenReturn(100_000L);
        when(memoryService.getTotalMemory()).thenReturn(1_000_000L);
        when(memoryUsageSeriesBuilder.buildMemoryUsageSeries(state)).thenReturn(series);
        when(usageChart.getData()).thenReturn(chartChildren);

        ReflectionTestUtils.setField(memoryUsageController, "memoryUsageState", state);
        ReflectionTestUtils.invokeMethod(memoryUsageController, "updateTitle");
        ReflectionTestUtils.invokeMethod(memoryUsageController, "updateChart");

        verify(titleLbl).setText("Memory usage: 97.7 KiB / 976.6 KIB");
        verify(chartChildren).clear();
        verify(chartChildren).add(series);
    }
}