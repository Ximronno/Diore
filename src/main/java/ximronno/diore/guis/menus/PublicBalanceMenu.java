package ximronno.diore.guis.menus;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.api.item.ItemBuilder;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.utils.AccountUtils;


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
    public void handleMenu(Player p, Account acc, Languages language, PersistentDataContainer container) {

        if(container.has(key, PersistentDataType.STRING)) {

            String func = container.get(key, PersistentDataType.STRING);
            if(func == null) return;

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
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.GREEN_TERRACOTTA)
                .setDisplayName(configManager.getFormattedString(config, "public-balance-menu-true"))
                .setLore(configManager.getFormattedList(config, "public-balance-menu-true-lore"))
                .build();

        ItemBuilder.addPersistentData(item, key, "true");

        return item;
    }
    private ItemStack getPublicBalanceFalse(FileConfiguration config) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.RED_TERRACOTTA)
                .setDisplayName(configManager.getFormattedString(config, "public-balance-menu-false"))
                .setLore(configManager.getFormattedList(config, "public-balance-menu-false-lore"))
                .build();

        ItemBuilder.addPersistentData(item, key, "false");

        return item;
    }
}
