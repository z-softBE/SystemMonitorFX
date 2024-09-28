package be.zsoft.system.monitor.app;

import be.zsoft.system.monitor.ui.exception.ExceptionHandler;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;

@Slf4j

@SpringBootApplication(
        scanBasePackages = {
                "com.gluonhq.ignite.spring",
                "be.zsoft.system.monitor"
        }
)
@EnableScheduling
public class SystemMonitorApplication {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        SetupSwingLookAndFeel();
        Application.launch(SystemMonitorFXApplication.class, args);
    }

    private static void SetupSwingLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            log.error(ex.getMessage(), ex);
            System.exit(-1);
        }
    }
}
