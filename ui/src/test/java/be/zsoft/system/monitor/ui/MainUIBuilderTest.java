package be.zsoft.system.monitor.ui;

import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainUIBuilderTest extends BaseUITest {

    @Mock
    private FXMLLoader fxmlLoader;

    @Mock
    private Stage stage;

    @InjectMocks
    private MainUIBuilder mainUIBuilder;

    @Captor
    private ArgumentCaptor<Scene> sceneArgumentCaptor;

    @Captor
    private ArgumentCaptor<Image> iconCaptor;

    @Test
    void buildMainStage() throws Exception{
        try (MockedStatic<CSSFX> cssfxMockedStatic = mockStatic(CSSFX.class)) {
            ObservableList<Image> icons = mock(ObservableList.class);

            AnchorPane root = new AnchorPane();

            when(fxmlLoader.load()).thenReturn(root);
            when(stage.getIcons()).thenReturn(icons);

            mainUIBuilder.buildMainStage(stage);

            verify(fxmlLoader).setLocation(ResourceLoader.loadURL("fxml/main.fxml"));
            verify(stage).initStyle(StageStyle.DECORATED);
            verify(stage).setTitle("Z-Soft System Monitor");
            verify(stage).setMinWidth(1200);
            verify(stage).setMinHeight(800);
            verify(stage).getIcons();
            verify(icons).add(iconCaptor.capture());
            verify(stage).setScene(sceneArgumentCaptor.capture());
            verify(stage).show();

            assertThat(iconCaptor.getValue().getUrl()).isEqualTo(ResourceLoader.load("logo.png"));

            assertThat(sceneArgumentCaptor.getValue().getRoot()).isEqualTo(root);
            assertThat(sceneArgumentCaptor.getValue().getWidth()).isEqualTo(1200);
            assertThat(sceneArgumentCaptor.getValue().getHeight()).isEqualTo(800);
            assertThat(sceneArgumentCaptor.getValue().getFill()).isEqualTo(Color.TRANSPARENT);

            cssfxMockedStatic.verify(CSSFX::start);
            assertThat(Application.getUserAgentStylesheet()).isEqualTo(Files.readString(Paths.get(MainUIBuilderTest.class.getResource("stylesheet_data.txt").toURI())));
        }
    }
}