package ximronno.diore.guis;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.api.item.ItemBuilder;
import ximronno.api.menu.Menu;
import ximronno.diore.Diore;
import ximronno.diore.impl.Languages;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;

import java.net.URL;

public abstract class DioreMenu extends Menu {
    protected final Diore plugin;
    protected final ConfigManager configManager;
    protected final AccountManager accountManager;
    protected final NamespacedKey key;
    public DioreMenu(NamespacedKey key) {
        plugin = Diore.getInstance();
        configManager = plugin.getConfigManager();
        accountManager = plugin.getAccountManager();
        this.key = key;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if(item == null) return;

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;

        Player p = (Player) e.getWhoClicked();

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Language language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        handleMenu(p, acc, language, container);
    }

    public abstract void handleMenu(Player p, Account acc, Language language, PersistentDataContainer container);
    protected ItemStack getMenuBlank() {
        return ItemBuilder.builder()
                .setMaterial(Material.BLACK_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .build();
    }
    protected ItemStack getMenuBack(FileConfiguration config) {
        URL url;
        try {
            url = new URL("https://textures.minecraft.net/texture/7f3e7bca5c651bba29cd359d5cd474402cc23ca7b309dc48736436b9f055b905");
        } catch(Exception e) {
            return null;
        }

        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(configManager.getFormattedString(config, "menu-back"))
                .setProfileFromURL(url)
                .build();

        ItemBuilder.addPersistentData(item, key, "back");

        return item;
    }
}
