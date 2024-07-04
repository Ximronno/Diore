package ximronno.diore.guis.menus;

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
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class AccountMenu extends DioreMenu {
    private final Diore plugin = Diore.getInstance();
    private final AccountManager accountManager = plugin.getAccountManager();
    private final ConfigManager configManager = plugin.getConfigManager();

    public AccountMenu(NamespacedKey key) {
        super(key);
    }

    @Override
    public int getSize() {
        return 27;
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
                case "balance":
                    new BalanceMenu(key).open(p);
                    break;
                case "language":
                    new LanguagesMenu(key).open(p);
                    break;
                case "public":
                    new PublicBalanceMenu(key).open(p);
                    break;
                case "back":
                    new MainMenu(key).open(p);
                    break;
            }
        }

    }
    @Override
    public void setContents(Player p) {

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        for(int i = 0; i < inventory.getSize(); i++) {

            switch(i) {
                case 10:
                    inventory.setItem(i, getAccountMenuBalance(acc, language.getCFG()));
                    break;
                case 13:
                    inventory.setItem(i, getAccountMenuLanguage(language));
                    break;
                case 16:
                    inventory.setItem(i, getAccountMenuPublic(acc, language.getCFG()));
                    break;
                case 26:
                    inventory.setItem(i, getMenuBack(language.getCFG()));
                    break;
                default:
                    inventory.setItem(i, getMenuBlank());
                    break;
            }

        }

    }
    private ItemStack getAccountMenuBalance(Account acc, FileConfiguration config) {
        ItemStack item = new ItemStack(Material.DIAMOND_ORE);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("account-menu-balance", config)
                .replace("<balance>", accountManager.formatBalance(acc.getBalance())));

        List<String> lore = new ArrayList<>();

        config.getStringList("account-menu-balance-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "balance");

        item.setItemMeta(meta);

        return item;
    }
    private ItemStack getAccountMenuLanguage(Languages language) {
        if(language == null) language = Languages.ENGLISH;
        ItemStack skull = language.getItemStack();

        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if(meta == null) return null;

        FileConfiguration config = language.getCFG();

        meta.setDisplayName(configManager.getFormattedString("account-menu-language", config)
                .replace("<language>", language.getName()));

        List<String> lore = new ArrayList<>();

        config.getStringList("account-menu-language-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "language");

        skull.setItemMeta(meta);

        return skull;
    }
    private ItemStack getAccountMenuPublic(Account acc, FileConfiguration config) {
        ItemStack item;
        if(acc.isPublicBalance()) {
            item = new ItemStack(Material.GREEN_TERRACOTTA);
        }
        else {
            item = new ItemStack(Material.RED_TERRACOTTA);
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        String yes = configManager.getFormattedString("menu-yes", config);
        String no = configManager.getFormattedString("menu-no", config);

        meta.setDisplayName(configManager.getFormattedString("account-menu-public", config)
                .replace("<public>", (acc.isPublicBalance() ? yes : no)));

        List<String> lore = new ArrayList<>();

        config.getStringList("account-menu-public-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "public");

        item.setItemMeta(meta);

        return item;
    }

}
