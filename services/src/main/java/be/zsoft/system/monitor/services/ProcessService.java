package be.zsoft.system.monitor.services;

import be.zsoft.system.monitor.services.mapper.ProcessMapper;
import be.zsoft.system.monitor.services.model.Process;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j

@Service
public class ProcessService {

    private final SystemInfo systemInfo;
    private final ProcessMapper processMapper;

    private final Map<Integer, OSProcess> previousSnapshot = new HashMap<>();

    public List<Process> getProcesses() {
        List<OSProcess> osProcesses = systemInfo.getOperatingSystem().getProcesses();
        List<Process> processes = processMapper.toProcess(osProcesses, previousSnapshot);

        updatePreviousSnapshot(osProcesses);

        return processes;
    }

    public void clearPreviousSnapshot() {
        previousSnapshot.clear();
    }

    private void updatePreviousSnapshot(List<OSProcess> processes) {
        previousSnapshot.clear();
        for (OSProcess p : processes) {
            previousSnapshot.put(p.getProcessID(), p);
        }
    }
}
