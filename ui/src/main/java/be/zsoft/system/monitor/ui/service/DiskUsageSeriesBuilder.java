package be.zsoft.system.monitor.ui.service;

import be.zsoft.system.monitor.services.utils.FixedSizeList;
import javafx.scene.chart.XYChart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j

@Service
public class DiskUsageSeriesBuilder {

    public XYChart.Series<Number, Number> buildDiskUsageSeries(String title, FixedSizeList<Long> data) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(title);

        for (int x = 0; x < data.getSize(); x++) {
            series.getData().add(new XYChart.Data<>(x, data.get(x) / 1024.0d));
        }

        return series;
    }
}
