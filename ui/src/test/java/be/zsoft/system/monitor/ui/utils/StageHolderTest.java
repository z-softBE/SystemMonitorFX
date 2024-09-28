package be.zsoft.system.monitor.ui.utils;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StageHolderTest {

    @Mock
    private Stage stage;

    @Test
    void holdPrimaryStage() {
        StageHolder.holdPrimaryStage(stage);

        assertThat(StageHolder.getPrimaryStage()).isEqualTo(stage);
    }
}