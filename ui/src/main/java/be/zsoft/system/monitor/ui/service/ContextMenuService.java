package be.zsoft.system.monitor.ui.service;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import org.springframework.stereotype.Service;

@Service
public class ContextMenuService {

    public ContextMenu buildCpuUsageChartContextMenu(Runnable overAllSelected, Runnable logicalSelected) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAutoHide(true);

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioMenuItem showOverallUsageItem = new RadioMenuItem("Show Overall Usage");
        showOverallUsageItem.setSelected(true);
        showOverallUsageItem.setToggleGroup(toggleGroup);
        RadioMenuItem showLogicalProcessorsUsageItem = new RadioMenuItem("Show logical processors usage");
        showLogicalProcessorsUsageItem.setToggleGroup(toggleGroup);
        showLogicalProcessorsUsageItem.setSelected(false);

        contextMenu.getItems().addAll(showOverallUsageItem, showLogicalProcessorsUsageItem);

        showOverallUsageItem.selectedProperty().addListener((_, _, newValue) -> {
            if (newValue) {
                overAllSelected.run();
            }
        });

        showLogicalProcessorsUsageItem.selectedProperty().addListener((_, _, newValue) -> {
            if (newValue) {
                logicalSelected.run();
            }
        });

        return contextMenu;
    }
}
