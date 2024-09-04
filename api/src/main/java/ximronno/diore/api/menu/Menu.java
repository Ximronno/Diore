package ximronno.diore.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;

    public int getIntSize() {
        return getSize().size();
    }

    public abstract MenuSizes getSize();

    public abstract Menu getPreviousMenu();

    public abstract String getTitle();

    public abstract String getTitle(Locale locale);

    public abstract void handleMenu(InventoryClickEvent e);

    public abstract void setContents(Player p);

    public void open(Player p) {

        inventory = Bukkit.createInventory(this, getIntSize(), getTitle());

        setContents(p);

        p.openInventory(inventory);

    }

    public void update(Player p) {

        open(p);

    }

    public void close(Player p) {

        p.closeInventory();

        inventory.clear();
        
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
