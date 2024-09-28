package be.zsoft.system.monitor.services.model;

import oshi.software.os.OSProcess;

public record Process(
        String name,
        int pid,
        String state,
        String user,
        int threadCount,
        double cpuUsage,
        double memoryUsageInPercentage,
        String memoryUsage,
        OSProcess osProcess
) {
}
