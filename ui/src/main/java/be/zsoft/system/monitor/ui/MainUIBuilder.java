package be.zsoft.system.monitor.ui;

import fr.brouillard.oss.cssfx.CSSFX;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor

@Component
public class MainUIBuilder {

    private final FXMLLoader fxmlLoader;

    public void buildMainStage(Stage stage) throws IOException {
        log.info("Building the main stage and scene");

        CSSFX.start();

        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();

        fxmlLoader.setLocation(ResourceLoader.loadURL("fxml/main.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1200, 800);
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Z-Soft System Monitor");
        stage.setMinWidth(1200);
        stage.setMinHeight(800);
        stage.setScene(scene);
        stage.show();
    }
}
