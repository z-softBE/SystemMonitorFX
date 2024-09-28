package be.zsoft.system.monitor.ui.service;

import be.zsoft.system.monitor.services.state.MemoryUsageState;
import javafx.scene.chart.XYChart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j

@Service
public class MemoryUsageSeriesBuilder {

    public XYChart.Series<Number, Number> buildMemoryUsageSeries(MemoryUsageState state) {
        XYChart.Series<Number, Number> usageSeries = new XYChart.Series<>();
        usageSeries.setName("Memory Usage");

        for (int x = 0; x < state.getMemoryUsage().getSize(); x++) {
            usageSeries.getData().add(new XYChart.Data<>(x, state.getMemoryUsage().get(x)));
        }

        return usageSeries;
    }
}
