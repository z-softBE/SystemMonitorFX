package be.zsoft.system.monitor.ui.controller.control;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.utils.NodeUtils;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class InfoButtonControl extends MFXIconWrapper {

    public InfoButtonControl(MFXStageDialog dialog) {
        super("fas-info", 16, Color.WHITE, 32);
        defaultRippleGeneratorBehavior();
        setBackground(new Background(new BackgroundFill(Color.rgb(33,150,243), CornerRadii.EMPTY, Insets.EMPTY)));
        NodeUtils.makeRegionCircular(this);

        addEventHandler(MouseEvent.MOUSE_ENTERED, _ -> getScene().setCursor(Cursor.HAND));
        addEventHandler(MouseEvent.MOUSE_EXITED, _ -> getScene().setCursor(Cursor.DEFAULT));
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;
            dialog.show();
        });
    }
}
