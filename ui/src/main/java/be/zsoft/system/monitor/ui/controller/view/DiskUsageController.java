package be.zsoft.system.monitor.ui.controller.view;

import be.zsoft.system.monitor.services.DiskService;
import be.zsoft.system.monitor.services.state.DiskUsageState;
import be.zsoft.system.monitor.ui.controller.DisposableController;
import be.zsoft.system.monitor.ui.service.DiskUsageSeriesBuilder;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.mfxcore.utils.converters.FunctionalStringConverter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import oshi.hardware.HWDiskStore;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Component
public class DiskUsageController implements DisposableController {

    @Autowired
    private DiskService diskService;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private DiskUsageSeriesBuilder diskUsageSeriesBuilder;

    @FXML
    private Label titleLbl;

    @FXML
    private HBox headerContainer;

    @FXML
    private StackedAreaChart<Number, Number> diskUsageChart;

    @FXML
    private MFXComboBox<HWDiskStore> switchDiskCb;

    private DiskUsageState diskUsageState;
    private ScheduledFuture<?> dataUpdater;

    @FXML
    void initialize() {
        initSwitchDiskComboBox();
    }

    @FXML
    void onSwitchDisk(ActionEvent event) {
        updateDisk(switchDiskCb.getSelectedItem());
    }

    @Override
    public void dispose() {
        if (dataUpdater != null) {
            dataUpdater.cancel(true);
        }
    }

    private void initSwitchDiskComboBox() {
        switchDiskCb.setConverter(FunctionalStringConverter.to(disk -> disk != null ? disk.getModel() : ""));

        switchDiskCb.getItems().clear();
        switchDiskCb.getItems().addAll(diskService.getAllDisk());
        switchDiskCb.selectFirst();
    }

    private void updateDisk(HWDiskStore disk) {
        dispose();

        initHeader(disk);

        diskUsageState = new DiskUsageState(disk, 1);

        dataUpdater = taskScheduler.scheduleAtFixedRate(this::updateData, Duration.ofSeconds(1));
    }

    private void initHeader(HWDiskStore disk) {
        titleLbl.setText(disk.getModel());
    }

    private void updateData() {
        diskUsageState.updateData();

        Platform.runLater(this::updateChart);
    }

    private void updateChart() {
        diskUsageChart.getData().clear();
        diskUsageChart.getData().add(diskUsageSeriesBuilder.buildDiskUsageSeries("Read Usage", diskUsageState.getReadBytesData()));
        diskUsageChart.getData().add(diskUsageSeriesBuilder.buildDiskUsageSeries("Write Usage", diskUsageState.getWriteBytesData()));
    }


}
