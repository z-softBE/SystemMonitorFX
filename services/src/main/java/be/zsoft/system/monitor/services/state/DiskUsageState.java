package be.zsoft.system.monitor.services.state;

import be.zsoft.system.monitor.services.utils.FixedSizeList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import oshi.hardware.HWDiskStore;

@Slf4j
public class DiskUsageState {

    private final HWDiskStore diskStore;
    private final int cyclesToSkip;

    @Getter
    private final FixedSizeList<Long> readBytesData;
    @Getter
    private final FixedSizeList<Long> writeBytesData;

    private long readBytesOldTick;
    private long writeBytesOldTick;
    private boolean shouldSkipAddingData = true;
    private int skippedCycles = 0;

    public DiskUsageState(HWDiskStore diskStore, int cyclesToSkip) {
        this.diskStore = diskStore;
        this.cyclesToSkip = cyclesToSkip;

        readBytesData = new FixedSizeList<>(60, 0L);
        writeBytesData = new FixedSizeList<>(60, 0L);
    }

    public void updateData() {
        diskStore.updateAttributes();

        long readBytes = diskStore.getReadBytes();
        long writeBytes = diskStore.getWriteBytes();

        if (!shouldSkipAddingData) {
            readBytesData.add(readBytes - readBytesOldTick);
            writeBytesData.add(writeBytes - writeBytesOldTick);
        } else {
            skippedCycles++;
            shouldSkipAddingData = skippedCycles <= cyclesToSkip;
        }

        readBytesOldTick = readBytes;
        writeBytesOldTick = writeBytes;
    }
}
