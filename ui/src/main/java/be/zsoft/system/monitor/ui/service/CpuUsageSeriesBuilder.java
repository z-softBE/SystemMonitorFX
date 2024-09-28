package be.zsoft.system.monitor.ui.service;

import be.zsoft.system.monitor.services.state.CPUUsageState;
import be.zsoft.system.monitor.services.utils.FixedSizeList;
import javafx.scene.chart.XYChart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j

@Service
public class CpuUsageSeriesBuilder {

    public XYChart.Series<Number, Number> buildOverallUsage(CPUUsageState state) {
        XYChart.Series<Number, Number> cpuUsageSeries = new XYChart.Series<>();
        cpuUsageSeries.setName("CPU Usage");

        for (int x = 0; x < state.getSystemCPUUsage().getSize(); x++) {
            cpuUsageSeries.getData().add(new XYChart.Data<>(x, state.getSystemCPUUsage().get(x)));
        }

        return cpuUsageSeries;
    }

    public List<XYChart.Series<Number, Number>> buildLogicalUsage(CPUUsageState state) {
        List<XYChart.Series<Number, Number>> cpuUsageSeries = new ArrayList<>();

        int processorIndex = 1;
        for (FixedSizeList<Double> logicalProcessorUsage : state.getLogicalProcessorCPUUsage()) {
            XYChart.Series<Number, Number> processorUsageSeries = new XYChart.Series<>();
            processorUsageSeries.setName("Processor #" + processorIndex);

            for (int x = 0; x < logicalProcessorUsage.getSize(); x++) {
                processorUsageSeries.getData().add(new XYChart.Data<>(x, logicalProcessorUsage.get(x)));
            }

            cpuUsageSeries.add(processorUsageSeries);
            processorIndex++;
        }

        return cpuUsageSeries;
    }
}
