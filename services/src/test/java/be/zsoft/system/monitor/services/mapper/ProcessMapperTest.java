package be.zsoft.system.monitor.services.mapper;

import be.zsoft.system.monitor.services.model.Process;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessMapperTest {

    @Mock
    private SystemInfo systemInfo;

    @Mock
    private HardwareAbstractionLayer hardware;

    @Mock
    private GlobalMemory memory;

    @Mock
    private OSProcess oldTickProcess;

    @InjectMocks
    private ProcessMapper mapper;

    @Test
    void shouldMapToProcessObjects() {
        OSProcess osProcess1 = osProcess1();
        OSProcess osProcess2 = osProcess2();
        OSProcess osProcess3 = osProcess3();

        Process process1 = new Process("Process1", 100, "Running", "Z-Soft", 4, 5.42d, 20d, "195.3 KiB", osProcess1);
        Process process2 = new Process("Process2", 200, "Other", "Z-Soft", 6, 9.87d, 40d, "390.6 KiB", osProcess2);

        when(systemInfo.getHardware()).thenReturn(hardware);
        when(hardware.getMemory()).thenReturn(memory);
        when(memory.getTotal()).thenReturn(1_000_000L);

        List<Process> result = mapper.toProcess(List.of(osProcess1, osProcess2, osProcess3), Map.of(100, oldTickProcess, 200, oldTickProcess));

        assertThat(result).containsExactlyInAnyOrder(process1, process2);
    }

    private OSProcess osProcess1() {
        OSProcess process = mock(OSProcess.class);

        when(process.getName()).thenReturn("Process1");
        when(process.getProcessID()).thenReturn(100);
        when(process.getState()).thenReturn(OSProcess.State.RUNNING);
        when(process.getUser()).thenReturn("Z-Soft");
        when(process.getThreadCount()).thenReturn(4);
        when(process.getProcessCpuLoadBetweenTicks(any(OSProcess.class))).thenReturn(0.0542d);
        when(process.getResidentSetSize()).thenReturn(200_000L);

        return process;
    }

    private OSProcess osProcess2() {
        OSProcess process = mock(OSProcess.class);

        when(process.getName()).thenReturn("Process2");
        when(process.getProcessID()).thenReturn(200);
        when(process.getState()).thenReturn(OSProcess.State.OTHER);
        when(process.getUser()).thenReturn("Z-Soft");
        when(process.getThreadCount()).thenReturn(6);
        when(process.getProcessCpuLoadBetweenTicks(any(OSProcess.class))).thenReturn(0.0987d);
        when(process.getResidentSetSize()).thenReturn(400_000L);

        return process;
    }

    private OSProcess osProcess3() {
        OSProcess process = mock(OSProcess.class);

        when(process.getProcessID()).thenReturn(0);

        return process;
    }
}