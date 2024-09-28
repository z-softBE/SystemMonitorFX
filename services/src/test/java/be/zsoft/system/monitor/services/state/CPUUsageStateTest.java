package be.zsoft.system.monitor.services.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import oshi.hardware.CentralProcessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CPUUsageStateTest {

    @Mock
    private CentralProcessor processor;

    private CPUUsageState state;

    @BeforeEach
    void setUp() {
        when(processor.getLogicalProcessorCount()).thenReturn(2);
        state = new CPUUsageState(processor);
    }

    @Test
    void updateData() {
        testListSizes();

        long[] systemTicks1 = new long[]{0, 0, 0, 0, 0, 0, 0, 0};
        long[] systemTicks2 = new long[]{0, 1, 2, 3, 4, 5, 6, 7};

        when(processor.getSystemCpuLoadBetweenTicks(systemTicks1)).thenReturn(0.05);
        when(processor.getSystemCpuLoadBetweenTicks(systemTicks2)).thenReturn(0.1);
        when(processor.getSystemCpuLoadTicks()).thenReturn(systemTicks2);

        long[][] processorTicks1 = new long[][] {{0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 0, 0, 0, 0, 0}};
        long[][] processorTicks2 = new long[][] {{0, 1, 2, 3, 4, 5, 6, 7},{0, 1, 2, 3, 4, 5, 6, 7}};

        when(processor.getProcessorCpuLoadBetweenTicks(eq(processorTicks1))).thenReturn(new double[] {0.02, 0.09});
        when(processor.getProcessorCpuLoadBetweenTicks(eq(processorTicks2))).thenReturn(new double[] {0.04, 0.18});
        when(processor.getProcessorCpuLoadTicks()).thenReturn(processorTicks1);
        when(processor.getProcessorCpuLoadTicks()).thenReturn(processorTicks2);

        state.updateData();
        state.updateData();

        testListSizes();
        assertThat(state.getSystemCPUUsage().get(58)).isEqualTo(5);
        assertThat(state.getSystemCPUUsage().get(59)).isEqualTo(10);

        assertThat(state.getLogicalProcessorCPUUsage().get(0).get(58)).isEqualTo(2);
        assertThat(state.getLogicalProcessorCPUUsage().get(0).get(59)).isEqualTo(4);

        assertThat(state.getLogicalProcessorCPUUsage().get(1).get(58)).isEqualTo(9);
        assertThat(state.getLogicalProcessorCPUUsage().get(1).get(59)).isEqualTo(18);
    }

    private void testListSizes() {
        assertThat(state.getSystemCPUUsage().getSize()).isEqualTo(60);
        assertThat(state.getLogicalProcessorCPUUsage().size()).isEqualTo(2);
        assertThat(state.getLogicalProcessorCPUUsage().get(0).getSize()).isEqualTo(60);
        assertThat(state.getLogicalProcessorCPUUsage().get(1).getSize()).isEqualTo(60);
    }
}