package be.zsoft.system.monitor.services;

import be.zsoft.system.monitor.services.mapper.ProcessMapper;
import be.zsoft.system.monitor.services.model.Process;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessServiceTest {

    @Mock
    private SystemInfo systemInfo;

    @Mock
    private OperatingSystem operatingSystem;

    @Mock
    private ProcessMapper processMapper;

    @Mock
    private OSProcess osProcess1;

    @Mock
    private OSProcess osProcess2;

    @InjectMocks
    private ProcessService processService;

    @Test
    void getProcesses() {
        Process process1 = new Process("Process1", 100, "Running", "Z-Soft", 4, 5.42d, 20d, "195.3 KiB", osProcess1);
        Process process2 = new Process("Process2", 200, "Other", "Z-Soft", 6, 9.87d, 40d, "390.6 KiB", osProcess2);

        List<OSProcess> osProcesses = List.of(osProcess1, osProcess2);

        when(systemInfo.getOperatingSystem()).thenReturn(operatingSystem);
        when(operatingSystem.getProcesses()).thenReturn(osProcesses);
        when(processMapper.toProcess(eq(osProcesses), anyMap())).thenReturn(List.of(process1, process2));

        List<Process> result = processService.getProcesses();

        assertThat(result).containsExactlyInAnyOrder(process1, process2);
    }
}