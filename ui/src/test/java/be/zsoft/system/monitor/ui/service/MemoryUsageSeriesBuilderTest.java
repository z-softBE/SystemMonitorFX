package be.zsoft.system.monitor.ui.service;

import be.zsoft.system.monitor.services.state.MemoryUsageState;
import be.zsoft.system.monitor.services.utils.FixedSizeList;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemoryUsageSeriesBuilderTest {

    @Mock
    private MemoryUsageState memoryUsageState;

    @InjectMocks
    private MemoryUsageSeriesBuilder builder;

    @Test
    void buildMemoryUsageSeries() {
        FixedSizeList<Double> usage = new FixedSizeList<>(3, 0d);
        usage.add(1d);
        usage.add(2d);
        usage.add(3d);

        when(memoryUsageState.getMemoryUsage()).thenReturn(usage);

        XYChart.Series<Number, Number> result = builder.buildMemoryUsageSeries(memoryUsageState);

        assertThat(result.getName()).isEqualTo("Memory Usage");
        assertThat(result.getData().get(0).getXValue()).isEqualTo(0);
        assertThat(result.getData().get(0).getYValue()).isEqualTo(1.0);
        assertThat(result.getData().get(1).getXValue()).isEqualTo(1);
        assertThat(result.getData().get(1).getYValue()).isEqualTo(2.0);
        assertThat(result.getData().get(2).getXValue()).isEqualTo(2);
        assertThat(result.getData().get(2).getYValue()).isEqualTo(3.0);
    }
}