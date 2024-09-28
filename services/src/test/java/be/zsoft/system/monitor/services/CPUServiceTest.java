package be.zsoft.system.monitor.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CPUServiceTest {

    @Mock
    private HardwareAbstractionLayer hardware;

    @Mock
    private CentralProcessor processor;

    @Mock
    private SystemInfo systemInfo;

    @InjectMocks
    private CPUService cpuService;

    @Test
    void getCentralProcessor() {
        when(systemInfo.getHardware()).thenReturn(hardware);
        when(hardware.getProcessor()).thenReturn(processor);

        CentralProcessor result = cpuService.getCentralProcessor();

        verify(systemInfo).getHardware();
        verify(hardware).getProcessor();

        assertThat(result).isEqualTo(processor);
    }

    @Test
    void getCPUUsageState() {
    }
}