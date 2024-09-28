package be.zsoft.system.monitor.ui;

import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.FileSystems;

import static org.assertj.core.api.Assertions.assertThat;
class ResourceLoaderTest {

    @Test
    void loadURL() {
        URL expected = ResourceLoaderTest.class.getResource("main.fxml");
        URL result = ResourceLoader.loadURL("main.fxml");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void load() {
        String fileSeparator = FileSystems.getDefault().getSeparator();

        String expected = ResourceLoaderTest.class.getResource("main.fxml").toString();
        String result = ResourceLoader.load("main.fxml");

        assertThat(result).isEqualTo(expected);
    }
}