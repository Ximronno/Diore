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
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.impl.Languages;

import java.util.ArrayList;
import java.util.List;

public class BalanceMenu extends DioreMenu {
    public BalanceMenu(NamespacedKey key) {
        super(key);
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public String getTitle() {
        return configManager.getFormattedString("balance-menu-title");
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

                case "withdraw":
                    break;
                case "deposit":
                    break;
                case "transfer":
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
                case 10:
                    inventory.setItem(i, getWithdrawItem(language.getCFG()));
                    break;
                case 13:
                    inventory.setItem(i, getDepositItem(language.getCFG()));
                    break;
                case 16:
                    inventory.setItem(i, getTransferItem(language.getCFG()));
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
    private ItemStack getWithdrawItem(FileConfiguration config) {
        ItemStack item = new ItemStack(Material.DIAMOND_ORE);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("balance-menu-withdraw", config));

        List<String> lore = new ArrayList<>();

        config.getStringList("balance-menu-withdraw-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "withdraw");

        item.setItemMeta(meta);

        return item;
    }
    private ItemStack getDepositItem(FileConfiguration config) {
        ItemStack item = new ItemStack(Material.DEEPSLATE_DIAMOND_ORE);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("balance-menu-deposit", config));

        List<String> lore = new ArrayList<>();

        config.getStringList("balance-menu-deposit-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "deposit");

        item.setItemMeta(meta);

        return item;
    }
    private ItemStack getTransferItem(FileConfiguration config) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("balance-menu-transfer", config));

        List<String> lore = new ArrayList<>();

        config.getStringList("balance-menu-transfer-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "transfer");

        item.setItemMeta(meta);

        return item;
    }
}
