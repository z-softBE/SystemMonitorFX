package be.zsoft.system.monitor.ui;

import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseUITest {

    static boolean booted = false;


    @BeforeAll
    static void setUp() throws Exception {
        if (booted) return;

        Platform.startup(() -> {
            booted = true;
        });

        while (!booted) {
            Thread.sleep(10);
        }
    }

    @AfterAll
    static void tearDown() throws Exception {
        Platform.exit();
    }
}
