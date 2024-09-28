package be.zsoft.system.monitor.services.mapper;

import be.zsoft.system.monitor.services.model.Process;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.util.FormatUtil;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ProcessMapper {

    private final SystemInfo systemInfo;

    public List<Process> toProcess(List<OSProcess> processes, Map<Integer, OSProcess> previousSnapshot) {
        long totalMem = systemInfo.getHardware().getMemory().getTotal();
        return processes.stream()
                .filter(osProcess -> osProcess.getProcessID() > 0)
                .map(process -> toProcess(process, previousSnapshot, totalMem))
                .toList();
    }

    private Process toProcess(OSProcess process, Map<Integer, OSProcess> previousSnapshot, long totalMemory) {
        return new Process(
                process.getName(),
                process.getProcessID(),
                getProcessStateString(process.getState()),
                process.getUser(),
                process.getThreadCount(),
                process.getProcessCpuLoadBetweenTicks(previousSnapshot.get(process.getProcessID())) * 100d,
                100d * process.getResidentSetSize() / totalMemory,
                FormatUtil.formatBytes(process.getResidentSetSize()),
                process
        );
    }

    private String getProcessStateString(OSProcess.State state) {
        return switch (state) {
            case NEW -> "New";
            case RUNNING -> "Running";
            case SLEEPING -> "Sleeping";
            case WAITING -> "Waiting";
            case ZOMBIE -> "Zombie";
            case STOPPED -> "Stopped";
            case OTHER -> "Other";
            case INVALID -> "Invalid";
            case SUSPENDED -> "Suspended";
        };
    }
}
