package ximronno.diore.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ximronno.api.menu.Menu;

public class InventoryListener implements Listener {


    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {

        if(e.getInventory().getHolder() instanceof Menu menu) {

            e.setCancelled(true);

            menu.handleMenu(e);

        }

    }

}
