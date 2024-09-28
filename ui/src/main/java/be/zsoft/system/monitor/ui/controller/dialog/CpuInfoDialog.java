package be.zsoft.system.monitor.ui.controller.dialog;

import be.zsoft.system.monitor.ui.utils.FxmlLoaderUtils;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import oshi.hardware.CentralProcessor;
import oshi.util.FormatUtil;

import java.io.IOException;

public class CpuInfoDialog extends MFXGenericDialog {

    @FXML
    private Label coresAmountLbl;

    @FXML
    private Label logicalAmountLbl;

    @FXML
    private Label maxFreqLbl;

    @FXML
    private Label nameLbl;

    @FXML
    private Label vendorLbl;

    private final CentralProcessor processor;

    public CpuInfoDialog(CentralProcessor processor) {
        super("CPU Details", "");
        this.processor = processor;

        initData();
    }

    @Override
    protected void buildContent() {
        try {
            GridPane gridPane = FxmlLoaderUtils.loadControl("fxml/dialogs/cpu-info-body.fxml", this, false);
            setContent(gridPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        if (processor == null) return;

        nameLbl.setText(processor.getProcessorIdentifier().getName());
        vendorLbl.setText(processor.getProcessorIdentifier().getVendor());
        coresAmountLbl.setText(processor.getPhysicalProcessors().size() + "");
        logicalAmountLbl.setText(processor.getLogicalProcessorCount() + "");
        maxFreqLbl.setText(FormatUtil.formatHertz(processor.getMaxFreq()));
    }
}
