package be.zsoft.system.monitor.ui.service;

import be.zsoft.system.monitor.services.utils.FixedSizeList;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DiskUsageSeriesBuilderTest {

    @InjectMocks
    private DiskUsageSeriesBuilder builder;

    @Test
    void buildDiskUsageSeries() {
        FixedSizeList<Long> usage = new FixedSizeList<>(3, 0L);
        usage.add(1024L);
        usage.add(2048L);
        usage.add(4096L);

        XYChart.Series<Number, Number> result = builder.buildDiskUsageSeries("Read usage", usage);

        assertThat(result.getName()).isEqualTo("Read usage");
        assertThat(result.getData().get(0).getXValue()).isEqualTo(0);
        assertThat(result.getData().get(0).getYValue()).isEqualTo(1.0);
        assertThat(result.getData().get(1).getXValue()).isEqualTo(1);
        assertThat(result.getData().get(1).getYValue()).isEqualTo(2.0);
        assertThat(result.getData().get(2).getXValue()).isEqualTo(2);
        assertThat(result.getData().get(2).getYValue()).isEqualTo(4.0);
    }
}