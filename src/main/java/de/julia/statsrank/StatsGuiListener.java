package de.julia.statsrank;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public final class StatsGuiListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (StatsGuiCommand.GUI_TITLE_COMPONENT.equals(event.getView().title())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (StatsGuiCommand.GUI_TITLE_COMPONENT.equals(event.getView().title())) {
            event.setCancelled(true);
        }
    }
}