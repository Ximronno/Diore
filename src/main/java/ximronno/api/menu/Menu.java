package ximronno.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class Menu implements InventoryHolder {


    protected Inventory inventory;

    public abstract int getSize();
    public abstract String getTitle();
    public abstract void handleMenu(InventoryClickEvent e);
    public abstract void setContents(Player p);
    public void open(Player p) {

        inventory = Bukkit.createInventory(this, getSize(), getTitle());

        setContents(p);

        p.openInventory(inventory);


    }
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
