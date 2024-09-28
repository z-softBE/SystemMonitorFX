package be.zsoft.system.monitor.ui.controller.view;

import be.zsoft.system.monitor.services.CPUService;
import be.zsoft.system.monitor.services.state.CPUUsageState;
import be.zsoft.system.monitor.ui.controller.DisposableController;
import be.zsoft.system.monitor.ui.controller.control.InfoButtonControl;
import be.zsoft.system.monitor.ui.service.ContextMenuService;
import be.zsoft.system.monitor.ui.service.CpuUsageSeriesBuilder;
import be.zsoft.system.monitor.ui.service.DialogService;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import oshi.hardware.CentralProcessor;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Component
public class CpuUsageController implements DisposableController {

    @Autowired
    private CPUService cpuService;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private CpuUsageSeriesBuilder cpuUsageSeriesBuilder;

    @Autowired
    private DialogService dialogService;

    @Autowired
    private ContextMenuService contextMenuService;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label cpuNameLabel;

    @FXML
    private HBox headerContainer;

    @FXML
    private StackedAreaChart<Number, Number> logicProcessorChart;

    @FXML
    private AreaChart<Number, Number> systemCpuChart;

    private CPUUsageState cpuUsageState;
    private ScheduledFuture<?> dataUpdater;
    private boolean showOverallUsage = true;
    private MFXStageDialog infoDialog;

    @FXML
    public void initialize() {
        CentralProcessor processor = cpuService.getCentralProcessor();
        cpuUsageState = new CPUUsageState(processor);

        infoDialog = dialogService.createCpuInfoDialog(processor, rootPane);
        initHeader(processor);

        ContextMenu contextMenu = contextMenuService.buildCpuUsageChartContextMenu(this::selectOverallUsage, this::selectLogicalUsage);
        systemCpuChart.setOnContextMenuRequested(event -> contextMenu.show(systemCpuChart, event.getScreenX(), event.getScreenY()));
        logicProcessorChart.setOnContextMenuRequested(event -> contextMenu.show(logicProcessorChart, event.getScreenX(), event.getScreenY()));

        systemCpuChart.setVisible(true);
        logicProcessorChart.setVisible(false);

        dataUpdater = taskScheduler.scheduleAtFixedRate(this::updateData, Duration.ofSeconds(1));
    }

    @Override
    public void dispose() {
        if (dataUpdater != null) {
            dataUpdater.cancel(true);
        }
    }

    private void initHeader(CentralProcessor processor) {
        cpuNameLabel.setText(processor.getProcessorIdentifier().getName());
        headerContainer.getChildren().add(new InfoButtonControl(infoDialog));
    }

    private void selectOverallUsage() {
        showOverallUsage = true;
        systemCpuChart.setVisible(true);
        logicProcessorChart.setVisible(false);
        updateData();
    }

    private void selectLogicalUsage() {
        showOverallUsage = false;
        systemCpuChart.setVisible(false);
        logicProcessorChart.setVisible(true);
        updateData();
    }

    private void updateData() {
        cpuUsageState.updateData();
        Platform.runLater(this::updateCharts);
    }

    private void updateCharts() {
        if (showOverallUsage) {
            systemCpuChart.getData().clear();
            systemCpuChart.getData().add(cpuUsageSeriesBuilder.buildOverallUsage(cpuUsageState));
        } else {
            logicProcessorChart.getData().clear();
            logicProcessorChart.getData().addAll(cpuUsageSeriesBuilder.buildLogicalUsage(cpuUsageState));
        }
    }
}
