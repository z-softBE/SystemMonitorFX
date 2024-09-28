package be.zsoft.system.monitor.services;

import be.zsoft.system.monitor.services.state.CPUUsageState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

@Slf4j
@RequiredArgsConstructor


@Service
public class CPUService {

    private final SystemInfo systemInfo;

    public CentralProcessor getCentralProcessor() {
        return systemInfo.getHardware().getProcessor();
    }

    public CPUUsageState getCPUUsageState(CentralProcessor processor) {
        return new CPUUsageState(processor);
    }
}
