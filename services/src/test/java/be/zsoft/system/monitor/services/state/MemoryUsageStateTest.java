package be.zsoft.system.monitor.services.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import oshi.hardware.GlobalMemory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemoryUsageStateTest {

    @Mock
    private GlobalMemory globalMemory;

    private MemoryUsageState state;

    @BeforeEach
    void setUp() {
        state = new MemoryUsageState(globalMemory);
    }

    @Test
    void updateData() {
        when(globalMemory.getTotal()).thenReturn(1000L);
        when(globalMemory.getAvailable()).thenReturn(900L);

        state.updateData();

        assertThat(state.getMemoryUsage().get(59)).isEqualTo(10);
    }

    private void testListSizes() {
        assertThat(state.getMemoryUsage().getSize()).isEqualTo(60);
    }
}