package be.zsoft.system.monitor.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemoryServiceTest {

    @Mock
    private HardwareAbstractionLayer hardware;

    @Mock
    private GlobalMemory globalMemory;

    @Mock
    private SystemInfo systemInfo;

    @InjectMocks
    private MemoryService memoryService;

    @BeforeEach
    void setUp() {
        when(systemInfo.getHardware()).thenReturn(hardware);
        when(hardware.getMemory()).thenReturn(globalMemory);
    }

    @Test
    void getTotalMemory() {
        when(globalMemory.getTotal()).thenReturn(100L);

        long result = memoryService.getTotalMemory();

        assertThat(result).isEqualTo(100L);
    }

    @Test
    void getUsedMemory() {
        when(globalMemory.getTotal()).thenReturn(1000L);
        when(globalMemory.getAvailable()).thenReturn(900L);

        long result = memoryService.getUsedMemory();

        assertThat(result).isEqualTo(100L);
    }

    @Test
    void getMemory() {
        GlobalMemory result = memoryService.getMemory();

        assertThat(result).isEqualTo(globalMemory);
    }
}