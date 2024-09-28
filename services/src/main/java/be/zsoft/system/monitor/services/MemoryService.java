package be.zsoft.system.monitor.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

@Slf4j
@RequiredArgsConstructor


@Service
public class MemoryService {

    private final SystemInfo systemInfo;

    public long getTotalMemory() {
        return systemInfo.getHardware().getMemory().getTotal();
    }

    public long getUsedMemory() {
        return getTotalMemory() - systemInfo.getHardware().getMemory().getAvailable();
    }

    public GlobalMemory getMemory() {
        return systemInfo.getHardware().getMemory();
    }
}
