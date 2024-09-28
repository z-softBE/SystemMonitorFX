package be.zsoft.system.monitor.ui.controller.view;

import be.zsoft.system.monitor.services.MemoryService;
import be.zsoft.system.monitor.services.state.MemoryUsageState;
import be.zsoft.system.monitor.ui.controller.DisposableController;
import be.zsoft.system.monitor.ui.controller.control.InfoButtonControl;
import be.zsoft.system.monitor.ui.service.DialogService;
import be.zsoft.system.monitor.ui.service.MemoryUsageSeriesBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import oshi.hardware.GlobalMemory;
import oshi.util.FormatUtil;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Component
public class MemoryUsageController implements DisposableController {

    @Autowired
    private MemoryService memoryService;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private DialogService dialogService;

    @Autowired
    private MemoryUsageSeriesBuilder memoryUsageSeriesBuilder;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private HBox headerContainer;

    @FXML
    private Label titleLbl;

    @FXML
    private AreaChart<Number, Number> usageChart;

    private MemoryUsageState memoryUsageState;
    private ScheduledFuture<?> dataUpdater;
    private MFXStageDialog infoDialog;

    @FXML
    public void initialize() {
        GlobalMemory memory = memoryService.getMemory();

        memoryUsageState = new MemoryUsageState(memory);
        infoDialog = dialogService.createMemoryInfoDialog(memory, rootPane);

        initHeader();

        dataUpdater = taskScheduler.scheduleAtFixedRate(this::updateData, Duration.ofSeconds(1));
    }

    @Override
    public void dispose() {
        if (dataUpdater != null) {
            dataUpdater.cancel(true);
        }
    }

    private void initHeader() {
        updateTitle();
        headerContainer.getChildren().add(new InfoButtonControl(infoDialog));
    }

    private void updateData() {
        memoryUsageState.updateData();
        Platform.runLater(() -> {
            updateTitle();
            updateChart();
        });
    }

    private void updateTitle() {
        String title = "Memory usage: %s / %S".formatted(
                FormatUtil.formatBytes(memoryService.getUsedMemory()),
                FormatUtil.formatBytes(memoryService.getTotalMemory())
        );
        titleLbl.setText(title);
    }

    private void updateChart() {
        usageChart.getData().clear();
        usageChart.getData().add(memoryUsageSeriesBuilder.buildMemoryUsageSeries(memoryUsageState));
    }
}
