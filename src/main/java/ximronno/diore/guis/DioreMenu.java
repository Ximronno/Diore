package ximronno.diore.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import ximronno.api.menu.Menu;
import ximronno.diore.Diore;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;

import java.net.URL;
import java.util.UUID;

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
    protected ItemStack getMenuBlank() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(" ");

        item.setItemMeta(meta);

        return item;
    }
    protected ItemStack getMenuBack(FileConfiguration config) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL("https://textures.minecraft.net/texture/7f3e7bca5c651bba29cd359d5cd474402cc23ca7b309dc48736436b9f055b905");
        } catch(Exception e) {
            return null;
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if(meta == null) return null;

        meta.setOwnerProfile(profile);

        meta.setDisplayName(configManager.getFormattedString("menu-back", config));

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "back");

        skull.setItemMeta(meta);

        return skull;
    }
}
