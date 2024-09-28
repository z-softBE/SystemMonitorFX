package be.zsoft.system.monitor.ui.controller.control;

import be.zsoft.system.monitor.ui.utils.FxmlLoaderUtils;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import oshi.hardware.PhysicalMemory;
import oshi.util.FormatUtil;

import java.io.IOException;

public class PhysicalMemoryItemControl extends GridPane {

    @FXML
    private Label capacityLbl;

    @FXML
    private Label slotLbl;

    @FXML
    private Label speedLbl;

    @FXML
    private Label typeLbl;

    private final PhysicalMemory memory;

    public PhysicalMemoryItemControl(PhysicalMemory memory) {
        this.memory = memory;

        try {
            FxmlLoaderUtils.loadControl("fxml/controls/physical-memory-item.fxml", this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setWidth(250);
        setHeight(100);
    }

    @FXML
    public void initialize() {
        speedLbl.setText(FormatUtil.formatHertz(memory.getClockSpeed()));
        capacityLbl.setText(FormatUtil.formatBytes(memory.getCapacity()));
        typeLbl.setText(memory.getMemoryType());
        slotLbl.setText(memory.getBankLabel());
    }
}
