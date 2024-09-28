package be.zsoft.system.monitor.ui.controller;

import be.zsoft.system.monitor.ui.BaseUITest;
import be.zsoft.system.monitor.ui.dto.MainControllerForTest;
import be.zsoft.system.monitor.ui.utils.FxmlLoaderUtils;
import be.zsoft.system.monitor.ui.utils.NavbarItem;
import com.sun.javafx.PlatformUtil;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainControllerTest extends BaseUITest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private BorderPane contentPane;

    @Mock
    private MFXScrollPane navbarScrollPane;

    @Mock
    private VBox navbarVBox;

    @Mock
    private StackPane logoContainer;

    @Mock
    private ToggleGroup toggleGroup;

    @Mock
    private ObservableList<Node> navbarToggleButtons;

    @Mock
    private ObservableList<Node> logoContainerChildren;

    @Mock
    private ObservableList<Node> contentChildren;

    @InjectMocks
    private MainController mainController;

    @Captor
    private ArgumentCaptor<List<ToggleButton>> navbarTogglesCaptor;

    @Captor
    private ArgumentCaptor<ImageView> logoCaptor;

    @Captor
    private ArgumentCaptor<Node> contentCaptor;

    @BeforeEach
    void setup()  throws Exception {
        ReflectionTestUtils.setField(mainController, "toggleGroup", toggleGroup);
        when(toggleGroup.getToggles()).thenReturn(FXCollections.observableList(new ArrayList<>()));
    }

    @Test
    void initialize() {
        try (MockedStatic<ScrollUtils> mockedScrollUtils = mockStatic(ScrollUtils.class);
             MockedStatic<FxmlLoaderUtils> mockedFXMLLoaderUtils = mockStatic(FxmlLoaderUtils.class);
             MockedStatic<PlatformUtil> mockedPlatformUtils = Mockito.mockStatic(PlatformUtil.class)) {

            mockedPlatformUtils.when(PlatformUtil::isWindows).thenReturn(true);
            when(toggleGroup.selectedToggleProperty()).thenReturn(new ReadOnlyObjectWrapper<>());
            when(navbarVBox.getChildren()).thenReturn(navbarToggleButtons);
            when(logoContainer.getChildren()).thenReturn(logoContainerChildren);
            mockedFXMLLoaderUtils.when(() -> FxmlLoaderUtils.loadView(NavbarItem.PROCESSES.getFxmlPath(), applicationContext))
                    .thenReturn(new Pair<>(new GridPane(), new MainControllerForTest()));
            when(contentPane.getChildren()).thenReturn(contentChildren);

            mainController.initialize(null, null);

            verify(toggleGroup).selectToggle(any(ToggleButton.class));
            verify(navbarToggleButtons).setAll(navbarTogglesCaptor.capture());
            verify(logoContainerChildren).add(logoCaptor.capture());
            mockedScrollUtils.verify(() -> ScrollUtils.addSmoothScrolling(navbarScrollPane));
            verify(contentChildren).clear();
            verify(contentPane).setCenter(contentCaptor.capture());

            assertThat(navbarTogglesCaptor.getValue().size()).isEqualTo(5);
            assertThat(logoCaptor.getValue().getClip()).isInstanceOf(Circle.class);
            assertThat(((Circle) logoCaptor.getValue().getClip()).getRadius()).isEqualTo(30);
            assertThat(logoCaptor.getValue().getImage().getWidth()).isEqualTo(32);
            assertThat(logoCaptor.getValue().getImage().getHeight()).isEqualTo(32);
            assertThat(logoCaptor.getValue().getImage().isPreserveRatio()).isTrue();
            assertThat(logoCaptor.getValue().getImage().isSmooth()).isTrue();
            assertThat(contentCaptor.getValue()).isInstanceOf(GridPane.class);
        }
    }
}