package be.zsoft.system.monitor.services.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import oshi.hardware.HWDiskStore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiskUsageStateTest {

    @Mock
    private HWDiskStore hwDiskStore;

    private DiskUsageState state;

    @BeforeEach
    void setUp() {
        state = new DiskUsageState(hwDiskStore, 1);
    }

    @Test
    void updateData() {

        when(hwDiskStore.getReadBytes()).thenReturn(2L, 4L, 8L, 10L);
        when(hwDiskStore.getWriteBytes()).thenReturn(10L, 20L, 30L, 40L);

        testListSizes();

        state.updateData();
        state.updateData();
        state.updateData();
        state.updateData();

        verify(hwDiskStore, times(4)).updateAttributes();

        testListSizes();

        assertThat(state.getReadBytesData().get(58)).isEqualTo(4);
        assertThat(state.getReadBytesData().get(59)).isEqualTo(2);

        assertThat(state.getWriteBytesData().get(58)).isEqualTo(10);
        assertThat(state.getWriteBytesData().get(59)).isEqualTo(10);
    }

    private void testListSizes() {
        assertThat(state.getReadBytesData().getSize()).isEqualTo(60);
        assertThat(state.getWriteBytesData().getSize()).isEqualTo(60);
    }
}