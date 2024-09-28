package be.zsoft.system.monitor.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;

import java.util.List;

@RequiredArgsConstructor
@Slf4j

@Service
public class DiskService {

    private final SystemInfo systemInfo;

    public List<HWDiskStore> getAllDisk() {
        return systemInfo.getHardware().getDiskStores();
    }
}
