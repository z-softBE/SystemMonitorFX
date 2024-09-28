package be.zsoft.system.monitor.ui.utils;

import com.sun.javafx.PlatformUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NavbarItemTest {

    @Test
    void getForSupportedPlatform_windows() {
        try(MockedStatic<PlatformUtil> mockedPlatformUtils = Mockito.mockStatic(PlatformUtil.class)) {
            mockedPlatformUtils.when(PlatformUtil::isWindows).thenReturn(true);
            mockedPlatformUtils.when(PlatformUtil::isMac).thenReturn(false);
            mockedPlatformUtils.when(PlatformUtil::isLinux).thenReturn(false);
            mockedPlatformUtils.when(PlatformUtil::isUnix).thenReturn(false);

            List<NavbarItem> expected = List.of(NavbarItem.PROCESSES, NavbarItem.CPU_USAGE, NavbarItem.MEMORY_USAGE, NavbarItem.DISK_USAGE, NavbarItem.ABOUT);
            List<NavbarItem> result = NavbarItem.getForSupportedPlatform();

            assertThat(result).isEqualTo(expected);
        }
    }

    @Test
    void getForSupportedPlatform_mac() {
        try(MockedStatic<PlatformUtil> mockedPlatformUtils = Mockito.mockStatic(PlatformUtil.class)) {
            mockedPlatformUtils.when(PlatformUtil::isWindows).thenReturn(false);
            mockedPlatformUtils.when(PlatformUtil::isMac).thenReturn(true);
            mockedPlatformUtils.when(PlatformUtil::isLinux).thenReturn(false);
            mockedPlatformUtils.when(PlatformUtil::isUnix).thenReturn(false);

            List<NavbarItem> expected = List.of(NavbarItem.PROCESSES, NavbarItem.CPU_USAGE, NavbarItem.MEMORY_USAGE, NavbarItem.DISK_USAGE, NavbarItem.ABOUT);
            List<NavbarItem> result = NavbarItem.getForSupportedPlatform();

            assertThat(result).isEqualTo(expected);
        }
    }

    @Test
    void getForSupportedPlatform_linux() {
        try(MockedStatic<PlatformUtil> mockedPlatformUtils = Mockito.mockStatic(PlatformUtil.class)) {
            mockedPlatformUtils.when(PlatformUtil::isWindows).thenReturn(false);
            mockedPlatformUtils.when(PlatformUtil::isMac).thenReturn(false);
            mockedPlatformUtils.when(PlatformUtil::isLinux).thenReturn(true);
            mockedPlatformUtils.when(PlatformUtil::isUnix).thenReturn(false);

            List<NavbarItem> expected = List.of(NavbarItem.PROCESSES, NavbarItem.CPU_USAGE, NavbarItem.MEMORY_USAGE, NavbarItem.DISK_USAGE, NavbarItem.ABOUT);
            List<NavbarItem> result = NavbarItem.getForSupportedPlatform();

            assertThat(result).isEqualTo(expected);
        }
    }

    @Test
    void getForSupportedPlatform_unix() {
        try(MockedStatic<PlatformUtil> mockedPlatformUtils = Mockito.mockStatic(PlatformUtil.class)) {
            mockedPlatformUtils.when(PlatformUtil::isWindows).thenReturn(false);
            mockedPlatformUtils.when(PlatformUtil::isMac).thenReturn(false);
            mockedPlatformUtils.when(PlatformUtil::isLinux).thenReturn(false);
            mockedPlatformUtils.when(PlatformUtil::isUnix).thenReturn(true);

            List<NavbarItem> expected = List.of(NavbarItem.PROCESSES, NavbarItem.CPU_USAGE, NavbarItem.MEMORY_USAGE, NavbarItem.DISK_USAGE, NavbarItem.ABOUT);
            List<NavbarItem> result = NavbarItem.getForSupportedPlatform();

            assertThat(result).isEqualTo(expected);
        }
    }
}