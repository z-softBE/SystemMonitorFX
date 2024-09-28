package be.zsoft.system.monitor.ui.controller.view;

import io.github.palexdev.mfxcore.controls.Label;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AboutController {

    private static final String GITHUB_URL = "https://github.com/z-softBE/SystemMonitorFX";
    private static final String ZSOFT_URL = "https://z-soft.be";
    private static final String DONATE_URL = "https://buymeacoffee.com/zsoft";

    @Value("${project.version}")
    private String projectVersion;

    @Autowired
    private HostServices hostServices;

    @FXML
    private Label versionLbl;

    @FXML
    void initialize() {
        versionLbl.setText(projectVersion);
    }

    @FXML
    void onDonate(ActionEvent event) {
        hostServices.showDocument(DONATE_URL);
    }

    @FXML
    void onNavigateToSourceCode(ActionEvent event) {
        hostServices.showDocument(GITHUB_URL);
    }

    @FXML
    void onNavigateToZSoft(ActionEvent event) {
        hostServices.showDocument(ZSOFT_URL);
    }
}
