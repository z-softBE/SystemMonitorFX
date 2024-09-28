package be.zsoft.system.monitor.ui.controller.view;

import be.zsoft.system.monitor.ui.BaseUITest;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.application.HostServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AboutControllerTest extends BaseUITest {

    @Mock
    private HostServices hostServices;

    @Mock
    private Label versionLabel;

    @InjectMocks
    private AboutController aboutController;

    @BeforeEach
    void setup() throws Exception {
        ReflectionTestUtils.setField(aboutController, "projectVersion", "1.0.0");
    }

    @Test
    void initialize() {
        aboutController.initialize();

        verify(versionLabel).setText("1.0.0");
    }

    @Test
    void onDonate() {
        aboutController.onDonate(null);
        verify(hostServices).showDocument("https://buymeacoffee.com/zsoft");
    }

    @Test
    void onNavigateToSourceCode() {
        aboutController.onNavigateToSourceCode(null);
        verify(hostServices).showDocument("https://github.com/z-softBE/SystemMonitorFX");
    }

    @Test
    void onNavigateToZSoft() {
        aboutController.onNavigateToZSoft(null);
        verify(hostServices).showDocument("https://z-soft.be");
    }
}