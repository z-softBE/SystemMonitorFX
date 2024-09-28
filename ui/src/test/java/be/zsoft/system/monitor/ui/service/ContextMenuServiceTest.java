package be.zsoft.system.monitor.ui.service;

import be.zsoft.system.monitor.ui.BaseUITest;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContextMenuServiceTest extends BaseUITest {

    @Mock
    private Runnable overallRunnable;

    @Mock
    private Runnable logicalSelectedRunnable;

    @InjectMocks
    private ContextMenuService contextMenuService;

    @Test
    void buildCpuUsageChartContextMenu() {
        ContextMenu result = contextMenuService.buildCpuUsageChartContextMenu(overallRunnable, logicalSelectedRunnable);

        List<MenuItem> items = result.getItems();

        assertThat(result.isAutoHide()).isTrue();
        assertThat(items.size()).isEqualTo(2);

        MenuItem item1 = items.get(0);
        assertThat(item1).isInstanceOf(RadioMenuItem.class);
        assertThat(item1.getText()).isEqualTo("Show Overall Usage");
        assertThat(((RadioMenuItem) item1).isSelected()).isTrue();

        MenuItem item2 = items.get(1);
        assertThat(item2).isInstanceOf(RadioMenuItem.class);
        assertThat(item2.getText()).isEqualTo("Show logical processors usage");
        assertThat(((RadioMenuItem) item2).isSelected()).isFalse();
    }

    @Test
    void buildCpuUsageChartContextMenu_selectOverall() {
        ContextMenu result = contextMenuService.buildCpuUsageChartContextMenu(overallRunnable, logicalSelectedRunnable);

        ((RadioMenuItem)result.getItems().get(0)).selectedProperty().set(false);
        ((RadioMenuItem)result.getItems().get(0)).selectedProperty().set(true);

        verify(overallRunnable).run();
    }

    @Test
    void buildCpuUsageChartContextMenu_selectLogical() {
        ContextMenu result = contextMenuService.buildCpuUsageChartContextMenu(overallRunnable, logicalSelectedRunnable);

        ((RadioMenuItem)result.getItems().get(1)).selectedProperty().set(false);
        ((RadioMenuItem)result.getItems().get(1)).selectedProperty().set(true);

        verify(logicalSelectedRunnable).run();
    }
}