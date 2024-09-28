package be.zsoft.system.monitor.ui.controller;

import be.zsoft.system.monitor.ui.ResourceLoader;
import be.zsoft.system.monitor.ui.utils.FxmlLoaderUtils;
import be.zsoft.system.monitor.ui.utils.NavbarItem;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


@Component
public class MainController implements Initializable {

    @Autowired
    private ApplicationContext applicationContext;

    @FXML
    private BorderPane contentPane;

    @FXML
    private MFXScrollPane navbarScrollPane;

    @FXML
    private VBox navbarVBox;

    @FXML
    private StackPane logoContainer;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    private Object currentPageController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            buildNavbar();
            ScrollUtils.addSmoothScrolling(navbarScrollPane);
            initializeLogo();
            loadPage(NavbarItem.PROCESSES);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void buildNavbar() {
        List<ToggleButton> navbarToggles = NavbarItem.getForSupportedPlatform().stream().map(this::createToggle).toList();
        toggleGroup.selectToggle(navbarToggles.getFirst());
        navbarVBox.getChildren().setAll(navbarToggles);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            try {
                loadPage((NavbarItem) newValue.getUserData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadPage(NavbarItem navbarItem) throws IOException {
        Pair<Node, Object> loaderData = FxmlLoaderUtils.loadView(navbarItem.getFxmlPath(), applicationContext);

        if (currentPageController != null && currentPageController instanceof DisposableController) {
            ((DisposableController) currentPageController).dispose();
        }

        contentPane.getChildren().clear();
        contentPane.setCenter(loaderData.getKey());
        currentPageController = loaderData.getValue();
    }

    private void initializeLogo() {
        Image logoImage = new Image(ResourceLoader.load("logo.png"), 32, 32, true, true);
        ImageView logoView = new ImageView(logoImage);
        Circle clip = new Circle(30);
        clip.centerXProperty().bind(logoView.layoutBoundsProperty().map(Bounds::getCenterX));
        clip.centerYProperty().bind(logoView.layoutBoundsProperty().map(Bounds::getCenterY));
        logoView.setClip(clip);
        logoContainer.getChildren().add(logoView);
    }

    private ToggleButton createToggle(NavbarItem item) {
        MFXIconWrapper wrapper = new MFXIconWrapper(item.getIcon(), 24, 32);
        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(item.getText(), wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);
        toggleNode.setUserData(item);

        return toggleNode;
    }
}
