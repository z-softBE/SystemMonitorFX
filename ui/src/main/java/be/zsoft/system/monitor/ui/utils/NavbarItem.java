package be.zsoft.system.monitor.ui.utils;

import com.sun.javafx.PlatformUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum NavbarItem {

    PROCESSES("fas-cubes", "Processes", "fxml/views/processes.fxml", true, true, true, true),
    CPU_USAGE("fas-microchip", "CPU usage", "fxml/views/cpu-usage.fxml", true, true, true, true),
    MEMORY_USAGE("fas-memory", "Memory usage", "fxml/views/memory-usage.fxml", true, true, true, true),
    DISK_USAGE("fas-hard-drive", "Disk usage", "fxml/views/disk-usage.fxml", true, true, true, true),
    ABOUT("fas-circle-info", "About", "fxml/views/about.fxml", true, true, true, true);

    private final String icon;
    private final String text;
    private final String fxmlPath;
    private final boolean windowsSupported;
    private final boolean macSupported;
    private final boolean linuxSupported;
    private final boolean unixSupported;

    public static List<NavbarItem> getForSupportedPlatform() {
        return Stream.of(values())
                .filter(NavbarItem::isSupported)
                .toList();
    }

    private static boolean isSupported(NavbarItem item) {
        if (PlatformUtil.isWindows() && item.windowsSupported) {
            return true;
        }

        if (PlatformUtil.isMac() && item.macSupported) {
            return true;
        }

        if (PlatformUtil.isLinux() && item.linuxSupported) {
            return true;
        }

        if (PlatformUtil.isUnix() && item.unixSupported) {
            return true;
        }

        return false;
    }
}
