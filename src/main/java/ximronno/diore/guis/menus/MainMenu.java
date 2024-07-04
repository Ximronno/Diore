package ximronno.diore.guis.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainMenu extends DioreMenu {
    private final Diore plugin = Diore.getInstance();
    private final AccountManager accountManager = plugin.getAccountManager();
    private final ConfigManager configManager = plugin.getConfigManager();

    public MainMenu(NamespacedKey key) {
        super(key);
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public String getTitle() {
        return configManager.getFormattedString("main-menu-title");
    }
    @Override
    public void handleMenu(InventoryClickEvent e) {

        ItemStack item = e.getCurrentItem();
        if(item == null) return;

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;

        Player p = (Player) e.getWhoClicked();

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if(container.has(key, PersistentDataType.STRING)) {
            switch (container.get(key, PersistentDataType.STRING)) {
                case "skull":
                    new AccountMenu(plugin.getMenuKey()).open(p);
                    break;
                case "top":
                    break;
            }
        }

    }

    @Override
    public void setContents(Player p) {

        Account acc = plugin.getAccountManager().getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        for(int i = 0; i < inventory.getSize(); i++) {

            switch(i) {
                case 4:
                    inventory.setItem(i, getMainMenuSkull(acc, language.getCFG(), p));
                    break;
                case 8:
                    inventory.setItem(i, getMainMenuTopSkull(language.getCFG()));
                    break;
                default:
                    inventory.setItem(i, getMenuBlank());
                    break;
            }

        }

    }
    private ItemStack getMainMenuSkull(Account acc, FileConfiguration config, Player p) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if(skullMeta == null) return null;

        skullMeta.setDisplayName(configManager.getFormattedString("main-menu-skull", config)
                .replace("<player>", p.getDisplayName()));
        skullMeta.setOwningPlayer(p);

        List<String> lore = new ArrayList<>();

        String yes = configManager.getFormattedString("menu-yes", config);
        String no = configManager.getFormattedString("menu-no", config);

        config.getStringList("main-menu-skull-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)
                        .replace("<balance>", accountManager.formatBalance(acc.getBalance()))
                        .replace("<language>", acc.getLanguage().getName())
                        .replace("<public>", (acc.isPublicBalance() ? yes : no))));

        skullMeta.setLore(lore);

        skullMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "skull");

        skull.setItemMeta(skullMeta);

        return skull;
    }
    private ItemStack getMainMenuTopSkull(FileConfiguration config) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL("https://textures.minecraft.net/texture/7e69b5cfebe05dd43050277e5be83c807b45ade30e08291f6867c723e854b482");
        } catch(Exception e) {
            return null;
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if(skullMeta == null) return null;

        skullMeta.setOwnerProfile(profile);

        skullMeta.setDisplayName(configManager.getFormattedString("main-menu-top-skull", config));

        skullMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "top");

        skull.setItemMeta(skullMeta);

        return skull;
    }
}
