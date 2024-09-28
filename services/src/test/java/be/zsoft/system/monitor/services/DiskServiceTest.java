package be.zsoft.system.monitor.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiskServiceTest {

    @Mock
    private HardwareAbstractionLayer hardware;

    @Mock
    private SystemInfo systemInfo;

    @Mock
    private HWDiskStore diskStore1;

    @Mock
    private HWDiskStore diskStore2;

    @InjectMocks
    private DiskService diskService;

    @Test
    void getAllDisk() {
        when(systemInfo.getHardware()).thenReturn(hardware);
        when(hardware.getDiskStores()).thenReturn(List.of(diskStore1, diskStore2));

        List<HWDiskStore> result = diskService.getAllDisk();

        assertThat(result).contains(diskStore1, diskStore2);
    }
}