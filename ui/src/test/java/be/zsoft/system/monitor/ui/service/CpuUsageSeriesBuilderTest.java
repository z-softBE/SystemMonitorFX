package be.zsoft.system.monitor.ui.service;

import be.zsoft.system.monitor.services.state.CPUUsageState;
import be.zsoft.system.monitor.services.utils.FixedSizeList;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CpuUsageSeriesBuilderTest {

    @Mock
    private CPUUsageState state;

    @InjectMocks
    private CpuUsageSeriesBuilder builder;

    @Test
    void buildOverallUsage() {
        FixedSizeList<Double> usage = new FixedSizeList<>(3, 0d);
        usage.add(1d);
        usage.add(2d);
        usage.add(3d);

        when(state.getSystemCPUUsage()).thenReturn(usage);

        XYChart.Series<Number, Number> result = builder.buildOverallUsage(state);

        assertThat(result.getName()).isEqualTo("CPU Usage");
        assertThat(result.getData().get(0).getXValue()).isEqualTo(0);
        assertThat(result.getData().get(0).getYValue()).isEqualTo(1.0);
        assertThat(result.getData().get(1).getXValue()).isEqualTo(1);
        assertThat(result.getData().get(1).getYValue()).isEqualTo(2.0);
        assertThat(result.getData().get(2).getXValue()).isEqualTo(2);
        assertThat(result.getData().get(2).getYValue()).isEqualTo(3.0);
    }

    @Test
    void buildLogicalUsage() {
        FixedSizeList<Double> usage1 = new FixedSizeList<>(3, 0d);
        usage1.add(1d);
        usage1.add(2d);
        usage1.add(3d);

        FixedSizeList<Double> usage2 = new FixedSizeList<>(3, 0d);
        usage2.add(7d);
        usage2.add(8d);
        usage2.add(9d);

        when(state.getLogicalProcessorCPUUsage()).thenReturn(List.of(usage1, usage2));

        List<XYChart.Series<Number, Number>> result = builder.buildLogicalUsage(state);

        assertThat(result.get(0).getName()).isEqualTo("Processor #1");
        assertThat(result.get(0).getData().get(0).getXValue()).isEqualTo(0);
        assertThat(result.get(0).getData().get(0).getYValue()).isEqualTo(1.0);
        assertThat(result.get(0).getData().get(1).getXValue()).isEqualTo(1);
        assertThat(result.get(0).getData().get(1).getYValue()).isEqualTo(2.0);
        assertThat(result.get(0).getData().get(2).getXValue()).isEqualTo(2);
        assertThat(result.get(0).getData().get(2).getYValue()).isEqualTo(3.0);

        assertThat(result.get(1).getName()).isEqualTo("Processor #2");
        assertThat(result.get(1).getData().get(0).getXValue()).isEqualTo(0);
        assertThat(result.get(1).getData().get(0).getYValue()).isEqualTo(7.0);
        assertThat(result.get(1).getData().get(1).getXValue()).isEqualTo(1);
        assertThat(result.get(1).getData().get(1).getYValue()).isEqualTo(8.0);
        assertThat(result.get(1).getData().get(2).getXValue()).isEqualTo(2);
        assertThat(result.get(1).getData().get(2).getYValue()).isEqualTo(9.0);
    }
}