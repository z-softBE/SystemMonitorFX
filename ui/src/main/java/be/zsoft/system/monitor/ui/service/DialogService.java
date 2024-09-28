package be.zsoft.system.monitor.ui.service;

import be.zsoft.system.monitor.ui.controller.dialog.CpuInfoDialog;
import be.zsoft.system.monitor.ui.controller.dialog.MemoryInfoDialog;
import be.zsoft.system.monitor.ui.utils.StageHolder;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import org.springframework.stereotype.Service;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import java.util.Map;

@Service
public class DialogService {

    public MFXStageDialog createCpuInfoDialog(CentralProcessor processor, Pane owner) {
        CpuInfoDialog dialogContent = new CpuInfoDialog(processor);
        dialogContent.showMinimizeProperty().set(false);
        dialogContent.showAlwaysOnTopProperty().set(false);

        MFXStageDialog infoDialog = MFXGenericDialogBuilder.build(dialogContent)
                .toStageDialogBuilder()
                .initOwner(StageHolder.getPrimaryStage())
                .initModality(Modality.APPLICATION_MODAL)
                .setDraggable(true)
                .setTitle("Dialogs Preview")
                .setOwnerNode(owner)
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimOwner(true)
                .get();

        dialogContent.addActions(Map.entry(new MFXButton("Ok"), _ -> infoDialog.close()));

        return infoDialog;
    }

    public MFXStageDialog createMemoryInfoDialog(GlobalMemory memory, Pane owner) {
        MemoryInfoDialog dialogContent = new MemoryInfoDialog(memory);
        dialogContent.showMinimizeProperty().set(false);
        dialogContent.showAlwaysOnTopProperty().set(false);

        MFXStageDialog infoDialog = MFXGenericDialogBuilder.build(dialogContent)
                .toStageDialogBuilder()
                .initOwner(StageHolder.getPrimaryStage())
                .initModality(Modality.APPLICATION_MODAL)
                .setDraggable(true)
                .setTitle("Memory Details")
                .setOwnerNode(owner)
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimOwner(true)
                .get();

        dialogContent.addActions(Map.entry(new MFXButton("Ok"), _ -> infoDialog.close()));

        return infoDialog;
    }
}
