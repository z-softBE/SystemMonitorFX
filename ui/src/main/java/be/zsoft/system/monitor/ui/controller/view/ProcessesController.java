package be.zsoft.system.monitor.ui.controller.view;

import be.zsoft.system.monitor.services.ProcessService;
import be.zsoft.system.monitor.services.model.Process;
import be.zsoft.system.monitor.ui.controller.DisposableController;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Comparator;
import java.util.concurrent.ScheduledFuture;

@Slf4j

@Component
public class ProcessesController implements DisposableController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private TaskScheduler taskScheduler;

    @FXML
    private MFXTableView<Process> mainTable;

    private ScheduledFuture<?> dataUpdater;

    @FXML
    public void initialize() {
        MFXTableColumn<Process> nameColumn = new MFXTableColumn<>("Name", true, Comparator.comparing(Process::name));
        MFXTableColumn<Process> pidColumn = new MFXTableColumn<>("PID", true, Comparator.comparingInt(Process::pid));
        MFXTableColumn<Process> stateColumn = new MFXTableColumn<>("State", true, Comparator.comparing(Process::state));
        MFXTableColumn<Process> userColumn = new MFXTableColumn<>("User", true, Comparator.comparing(Process::user));
        MFXTableColumn<Process> threatsColumn = new MFXTableColumn<>("Thread count", true, Comparator.comparingInt(Process::threadCount));
        MFXTableColumn<Process> cpuColumn = new MFXTableColumn<>("CPU %", true, Comparator.comparingDouble(Process::cpuUsage));
        MFXTableColumn<Process> memoryColumn = new MFXTableColumn<>("Memory %", true, Comparator.comparingDouble(Process::memoryUsageInPercentage));
        MFXTableColumn<Process> memoryUsageColumn = new MFXTableColumn<>("Memory Usage", true, Comparator.comparingDouble(p -> p.osProcess().getResidentSetSize()));

        nameColumn.setRowCellFactory(_ -> new MFXTableRowCell<>(Process::name));
        pidColumn.setRowCellFactory(_ -> new MFXTableRowCell<>(Process::pid));
        stateColumn.setRowCellFactory(_ -> new MFXTableRowCell<>(Process::state));
        userColumn.setRowCellFactory(_ -> new MFXTableRowCell<>(Process::user));
        threatsColumn.setRowCellFactory(_ -> new MFXTableRowCell<>(Process::threadCount));
        cpuColumn.setRowCellFactory(_ -> new MFXTableRowCell<>(Process::cpuUsage, value -> String.format("%.2f", value)));
        memoryColumn.setRowCellFactory(_ -> new MFXTableRowCell<>(Process::memoryUsageInPercentage, value -> String.format("%.2f", value)));
        memoryUsageColumn.setRowCellFactory(_ -> new MFXTableRowCell<>(Process::memoryUsage));

        mainTable.getTableColumns().addAll(nameColumn, pidColumn, stateColumn, userColumn, threatsColumn, cpuColumn, memoryColumn, memoryUsageColumn);
        mainTable.setItems(FXCollections.observableList(processService.getProcesses()));
        mainTable.autosizeColumnsOnInitialization();

        dataUpdater = taskScheduler.scheduleAtFixedRate(this::updateMainTableData,  Duration.ofSeconds(1));
    }

    @Override
    public void dispose() {
        processService.clearPreviousSnapshot();

        if (dataUpdater != null) {
            dataUpdater.cancel(true);
        }
    }

    private void updateMainTableData() {
        Platform.runLater(() -> mainTable.setItems(FXCollections.observableList(processService.getProcesses())));
    }
}
