package be.zsoft.system.monitor.services.state;

import be.zsoft.system.monitor.services.utils.FixedSizeList;
import lombok.Getter;
import oshi.hardware.CentralProcessor;

import java.util.ArrayList;
import java.util.List;

public class CPUUsageState {

    private final CentralProcessor processor;

    @Getter
    private final FixedSizeList<Double> systemCPUUsage;
    @Getter
    private final List<FixedSizeList<Double>> logicalProcessorCPUUsage;

    private long[] systemCPUOldTicks;
    private long[][] logicalProcessorCPUOldTicks;

    public CPUUsageState(CentralProcessor processor) {
        this.processor = processor;

        systemCPUUsage = new FixedSizeList<>(60, 0d);
        systemCPUOldTicks = new long[CentralProcessor.TickType.values().length];

        int processorCount = processor.getLogicalProcessorCount();
        logicalProcessorCPUUsage = new ArrayList<>(processorCount);

        for (int i = 0; i < processorCount; i++) {
            logicalProcessorCPUUsage.add(new FixedSizeList<>(60, 0d));
        }

        logicalProcessorCPUOldTicks = new long[processorCount][CentralProcessor.TickType.values().length];
    }

    public void updateData() {
        updateSystemCpuUsageData();
        updateLogicalProcessorsUsageData();
    }

    private void updateSystemCpuUsageData() {
        systemCPUUsage.add(processor.getSystemCpuLoadBetweenTicks(systemCPUOldTicks) * 100f);
        systemCPUOldTicks = processor.getSystemCpuLoadTicks();
    }

    private void updateLogicalProcessorsUsageData() {
        double[] data = processor.getProcessorCpuLoadBetweenTicks(logicalProcessorCPUOldTicks);
        for (int i = 0; i < logicalProcessorCPUUsage.size(); i++) {
            logicalProcessorCPUUsage.get(i).add(data[i] * 100f);
        }
        logicalProcessorCPUOldTicks = processor.getProcessorCpuLoadTicks();
    }
}
