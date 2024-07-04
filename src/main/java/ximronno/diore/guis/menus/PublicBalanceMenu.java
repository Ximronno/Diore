package ximronno.diore.guis.menus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.utils.AccountUtils;

import java.util.ArrayList;
import java.util.List;

public class PublicBalanceMenu extends DioreMenu {
    public PublicBalanceMenu(NamespacedKey key) {
        super(key);
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public String getTitle() {
        return configManager.getFormattedString("public-balance-menu-title");
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

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if(container.has(key, PersistentDataType.STRING)) {

            String func = container.get(key, PersistentDataType.STRING);

            switch(func) {
                case "true":
                    AccountUtils.setPublicBalance(p, acc, language.getCFG(), true);
                    new AccountMenu(key).open(p);
                    break;
                case "false":
                    AccountUtils.setPublicBalance(p, acc, language.getCFG(), false);
                    new AccountMenu(key).open(p);
                    break;
                case "back":
                    new AccountMenu(key).open(p);
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
                case 11:
                    inventory.setItem(i, getPublicBalanceTrue(language.getCFG()));
                    break;
                case 15:
                    inventory.setItem(i, getPublicBalanceFalse(language.getCFG()));
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
    private ItemStack getPublicBalanceTrue(FileConfiguration config) {
        ItemStack item = new ItemStack(Material.GREEN_TERRACOTTA);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("public-balance-menu-true", config));

        List<String> lore = new ArrayList<>();

        config.getStringList("public-balance-menu-true-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");

        item.setItemMeta(meta);

        return item;
    }
    private ItemStack getPublicBalanceFalse(FileConfiguration config) {
        ItemStack item = new ItemStack(Material.RED_TERRACOTTA);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("public-balance-menu-false", config));

        List<String> lore = new ArrayList<>();

        config.getStringList("public-balance-menu-false-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "false");

        item.setItemMeta(meta);

        return item;
    }
}
