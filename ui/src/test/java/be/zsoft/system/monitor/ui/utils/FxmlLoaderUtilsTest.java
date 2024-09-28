package be.zsoft.system.monitor.ui.utils;

import be.zsoft.system.monitor.ui.BaseUITest;
import be.zsoft.system.monitor.ui.dto.ControlControllerForTest;
import be.zsoft.system.monitor.ui.dto.MainControllerForTest;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FxmlLoaderUtilsTest extends BaseUITest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Test
    void loadView() throws Exception {
        when(applicationContext.getAutowireCapableBeanFactory()).thenReturn(autowireCapableBeanFactory);

        Pair<AnchorPane, MainControllerForTest> result = FxmlLoaderUtils.loadView("main.fxml", applicationContext);

        verify(applicationContext).getAutowireCapableBeanFactory();
        verify(autowireCapableBeanFactory).autowireBean(any());

        assertThat(result.getKey()).isInstanceOf(AnchorPane.class);
        assertThat(result.getValue()).isInstanceOf(MainControllerForTest.class);
    }

    @Test
    void loadControl_withRoot() throws Exception {
        ControlControllerForTest controller = new ControlControllerForTest();
        GridPane result = FxmlLoaderUtils.loadControl("control.fxml", controller);

        assertThat(result).isInstanceOf(GridPane.class);
    }
}