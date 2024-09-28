package be.zsoft.system.monitor.ui.controller.dialog;

import be.zsoft.system.monitor.ui.controller.control.PhysicalMemoryItemControl;
import be.zsoft.system.monitor.ui.utils.FxmlLoaderUtils;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import oshi.hardware.GlobalMemory;

import java.io.IOException;
import java.util.List;

public class MemoryInfoDialog extends MFXGenericDialog {

    @FXML
    private VBox itemsPane;

    private final GlobalMemory memory;

    public MemoryInfoDialog(GlobalMemory memory) {
        super("Memory Details", "");
        this.memory = memory;

        initData();
    }

    @Override
    protected void buildContent() {
        try {
            VBox vBox = FxmlLoaderUtils.loadControl("fxml/dialogs/memory-info-body.fxml", this, false);
            MFXScrollPane scrollPane = new MFXScrollPane(vBox);
            scrollPane.getStyleClass().add("content-container");
            scrollPane.setFitToWidth(true);
            ScrollUtils.addSmoothScrolling(scrollPane, 0.5);
            setContent(scrollPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        List<PhysicalMemoryItemControl> items = memory.getPhysicalMemory().stream()
                .map(PhysicalMemoryItemControl::new)
                .toList();

        itemsPane.getChildren().addAll(items);
    }
}
