package be.zsoft.system.monitor.services.state;

import be.zsoft.system.monitor.services.utils.FixedSizeList;
import lombok.Getter;
import oshi.hardware.GlobalMemory;

public class MemoryUsageState {

    private final GlobalMemory globalMemory;

    @Getter
    private final FixedSizeList<Double> memoryUsage;

    public MemoryUsageState(GlobalMemory globalMemory) {
        this.globalMemory = globalMemory;

        memoryUsage = new FixedSizeList<>(60, 0d);
    }

    public void updateData() {
        double used = (double) globalMemory.getTotal() - globalMemory.getAvailable();
        double usage =  (used / globalMemory.getTotal()) * 100.0d;

        memoryUsage.add(usage);
    }
}
