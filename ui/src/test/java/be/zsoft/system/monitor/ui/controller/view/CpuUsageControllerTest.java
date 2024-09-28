package be.zsoft.system.monitor.ui.controller.view;

import be.zsoft.system.monitor.services.CPUService;
import be.zsoft.system.monitor.services.state.CPUUsageState;
import be.zsoft.system.monitor.ui.BaseUITest;
import be.zsoft.system.monitor.ui.controller.control.InfoButtonControl;
import be.zsoft.system.monitor.ui.service.ContextMenuService;
import be.zsoft.system.monitor.ui.service.CpuUsageSeriesBuilder;
import be.zsoft.system.monitor.ui.service.DialogService;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.util.ReflectionTestUtils;
import oshi.hardware.CentralProcessor;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CpuUsageControllerTest extends BaseUITest {

    @Mock
    private CPUService cpuService;

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private CpuUsageSeriesBuilder cpuUsageSeriesBuilder;

    @Mock
    private DialogService dialogService;

    @Mock
    private ContextMenuService contextMenuService;

    @Mock
    private AnchorPane rootPane;

    @Mock
    private Label cpuUsageLabel;

    @Mock
    private HBox headerContainer;

    @Mock
    private StackedAreaChart<Number, Number> logicProcessorChart;

    @Mock
    private AreaChart<Number, Number> systemCpuChart;

    @Mock
    private CentralProcessor processor;

    @Mock
    private CPUUsageState state;

    @InjectMocks
    private CpuUsageController cpuUsageController;

    @Captor
    private ArgumentCaptor<Runnable> contextMenuActionCaptor;

    @Captor
    private ArgumentCaptor<EventHandler<ContextMenuEvent>> contextMenuEventCaptor;

    @Test
    void initialize() {
        CentralProcessor.ProcessorIdentifier identifier = mock(CentralProcessor.ProcessorIdentifier.class);
        ObservableList<Node> headerContainerChildren = FXCollections.observableArrayList();
        ContextMenu contextMenu = mock(ContextMenu.class);

        when(cpuService.getCentralProcessor()).thenReturn(processor);
        when(processor.getProcessorIdentifier()).thenReturn(identifier);
        when(identifier.getName()).thenReturn("processor name");
        when(headerContainer.getChildren()).thenReturn(headerContainerChildren);
        when(contextMenuService.buildCpuUsageChartContextMenu(any(Runnable.class), any(Runnable.class))).thenReturn(contextMenu);

        cpuUsageController.initialize();

        verify(dialogService).createCpuInfoDialog(processor, rootPane);
        verify(cpuService).getCentralProcessor();
        verify(cpuUsageLabel).setText("processor name");
        verify(contextMenuService).buildCpuUsageChartContextMenu(any(Runnable.class), any(Runnable.class));
        verify(systemCpuChart).setOnContextMenuRequested(any(EventHandler.class));
        verify(logicProcessorChart).setOnContextMenuRequested(any(EventHandler.class));
        verify(systemCpuChart).setVisible(true);
        verify(logicProcessorChart).setVisible(false);
        verify(taskScheduler).scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(1)));

        assertThat(headerContainerChildren.get(0)).isInstanceOf(InfoButtonControl.class);
    }

    @Test
    void initialize_contextMenu_overall() {
        CentralProcessor.ProcessorIdentifier identifier = mock(CentralProcessor.ProcessorIdentifier.class);
        ObservableList<Node> headerContainerChildren = FXCollections.observableArrayList();
        ContextMenu contextMenu = mock(ContextMenu.class);
        ContextMenuEvent event = new ContextMenuEvent(null, null, null, 0,0, 100, 200, false, null);

        when(cpuService.getCentralProcessor()).thenReturn(processor);
        when(processor.getProcessorIdentifier()).thenReturn(identifier);
        when(headerContainer.getChildren()).thenReturn(headerContainerChildren);
        when(contextMenuService.buildCpuUsageChartContextMenu(any(Runnable.class), any(Runnable.class))).thenReturn(contextMenu);

        cpuUsageController.initialize();
        ReflectionTestUtils.setField(cpuUsageController, "cpuUsageState", state);

        verify(contextMenuService).buildCpuUsageChartContextMenu(contextMenuActionCaptor.capture(), any(Runnable.class));
        verify(systemCpuChart).setOnContextMenuRequested(contextMenuEventCaptor.capture());

        contextMenuEventCaptor.getValue().handle(event);
        verify(contextMenu).show(systemCpuChart, 100, 200);

        contextMenuActionCaptor.getValue().run();
        assertThat(ReflectionTestUtils.getField(cpuUsageController, "showOverallUsage")).isEqualTo(true);
        verify(systemCpuChart, times(2)).setVisible(true);
        verify(logicProcessorChart, times(2)).setVisible(false);
        verify(state).updateData();
    }

    @Test
    void initialize_contextMenu_logicalProcessors() {
        CentralProcessor.ProcessorIdentifier identifier = mock(CentralProcessor.ProcessorIdentifier.class);
        ObservableList<Node> headerContainerChildren = FXCollections.observableArrayList();
        ContextMenu contextMenu = mock(ContextMenu.class);
        ContextMenuEvent event = new ContextMenuEvent(null, null, null, 0,0, 100, 200, false, null);

        when(cpuService.getCentralProcessor()).thenReturn(processor);
        when(processor.getProcessorIdentifier()).thenReturn(identifier);
        when(headerContainer.getChildren()).thenReturn(headerContainerChildren);
        when(contextMenuService.buildCpuUsageChartContextMenu(any(Runnable.class), any(Runnable.class))).thenReturn(contextMenu);

        cpuUsageController.initialize();
        ReflectionTestUtils.setField(cpuUsageController, "cpuUsageState", state);

        verify(contextMenuService).buildCpuUsageChartContextMenu(any(Runnable.class), contextMenuActionCaptor.capture());
        verify(logicProcessorChart).setOnContextMenuRequested(contextMenuEventCaptor.capture());

        contextMenuEventCaptor.getValue().handle(event);
        verify(contextMenu).show(logicProcessorChart, 100, 200);

        contextMenuActionCaptor.getValue().run();
        assertThat(ReflectionTestUtils.getField(cpuUsageController, "showOverallUsage")).isEqualTo(false);
        verify(systemCpuChart).setVisible(true);
        verify(systemCpuChart).setVisible(false);
        verify(logicProcessorChart).setVisible(false);
        verify(logicProcessorChart).setVisible(true);
        verify(state).updateData();
    }

    @Test
    void updateData() {
        ReflectionTestUtils.setField(cpuUsageController, "cpuUsageState", state);
        ReflectionTestUtils.invokeMethod(cpuUsageController, "updateData");

        verify(state).updateData();
    }

    @Test
    void updateCharts_overallUsage() {
        ObservableList<XYChart.Series<Number, Number>> chartChildren = mock(ObservableList.class);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        when(systemCpuChart.getData()).thenReturn(chartChildren);
        when(cpuUsageSeriesBuilder.buildOverallUsage(state)).thenReturn(series);

        ReflectionTestUtils.setField(cpuUsageController, "cpuUsageState", state);
        ReflectionTestUtils.setField(cpuUsageController, "showOverallUsage", true);
        ReflectionTestUtils.invokeMethod(cpuUsageController, "updateCharts");

        verify(chartChildren).clear();
        verify(chartChildren).add(series);
    }

    @Test
    void updateCharts_logicalUsage() {
        ObservableList<XYChart.Series<Number, Number>> chartChildren = mock(ObservableList.class);
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        List<XYChart.Series<Number, Number>> seriesList = List.of(series1, series2);

        when(logicProcessorChart.getData()).thenReturn(chartChildren);
        when(cpuUsageSeriesBuilder.buildLogicalUsage(state)).thenReturn(seriesList);

        ReflectionTestUtils.setField(cpuUsageController, "cpuUsageState", state);
        ReflectionTestUtils.setField(cpuUsageController, "showOverallUsage", false);
        ReflectionTestUtils.invokeMethod(cpuUsageController, "updateCharts");

        verify(chartChildren).clear();
        verify(chartChildren).addAll(seriesList);
    }

    @Test
    void dispose() {
        ScheduledFuture<?> dataUpdater = mock(ScheduledFuture.class);
        ReflectionTestUtils.setField(cpuUsageController, "dataUpdater", dataUpdater);

        cpuUsageController.dispose();

        verify(dataUpdater).cancel(true);

    }

}