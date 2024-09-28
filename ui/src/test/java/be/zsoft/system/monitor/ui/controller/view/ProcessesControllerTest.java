package be.zsoft.system.monitor.ui.controller.view;

import be.zsoft.system.monitor.services.ProcessService;
import be.zsoft.system.monitor.services.model.Process;
import be.zsoft.system.monitor.ui.BaseUITest;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Labeled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessesControllerTest extends BaseUITest {

    @Mock
    private ProcessService processService;

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private MFXTableView<Process> mainTable;

    @InjectMocks
    private ProcessesController processesController;

    @Test
    void initialize() {
        Process process1 = new Process("Process1", 100, "Running", "Z-Soft", 4, 5.42d, 20d, "195.3 KiB", null);
        Process process2 = new Process("Process2", 200, "Other", "Z-Soft", 6, 9.87d, 40d, "390.6 KiB", null);
        List<Process> processes = List.of(process1, process2);

        ObservableList<MFXTableColumn<Process>> columns = FXCollections.observableArrayList();

        when(mainTable.getTableColumns()).thenReturn(columns);
        when(processService.getProcesses()).thenReturn(processes);

        processesController.initialize();

        verify(mainTable).getTableColumns();
        verify(processService).getProcesses();
        verify(mainTable).setItems(FXCollections.observableList(processes));
        verify(mainTable).autosizeColumnsOnInitialization();
        verify(taskScheduler).scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(1)));

        assertThat(columns.size()).isEqualTo(8);
        List<String> columnNames = columns.stream().map(Labeled::getText).toList();
        List<String> expectedColumnNames = List.of("Name", "PID", "State", "User", "Thread count", "CPU %", "Memory %", "Memory Usage");
        assertThat(columnNames).containsAll(expectedColumnNames);
    }

    @Test
    void dispose() {
        ScheduledFuture<?> dataUpdater = mock(ScheduledFuture.class);
        ReflectionTestUtils.setField(processesController, "dataUpdater", dataUpdater);

        processesController.dispose();

        verify(dataUpdater).cancel(true);
    }
}