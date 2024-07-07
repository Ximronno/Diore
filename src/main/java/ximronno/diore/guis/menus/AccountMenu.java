package ximronno.diore.guis.menus;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import ximronno.api.interfaces.Account;
import ximronno.api.item.ItemBuilder;
import ximronno.diore.Diore;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;

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
    public void handleMenu(Player p, Account acc, Languages language, PersistentDataContainer container) {

        if(container.has(key, PersistentDataType.STRING)) {
            String func = container.get(key, PersistentDataType.STRING);
            if(func == null) return;

            switch (func) {
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
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.DIAMOND_ORE)
                .setDisplayName(configManager.getFormattedString(config, "account-menu-balance")
                        .replace("<balance>", accountManager.formatBalance(acc.getBalance())))
                .setLore(configManager.getFormattedList(config, "account-menu-balance-lore"))
                .build();

        ItemBuilder.addPersistentData(item, key, "balance");

        return item;
    }
    private ItemStack getAccountMenuLanguage(@NotNull Languages language) {
        FileConfiguration config = language.getCFG();

        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(configManager.getFormattedString(config, "account-menu-language")
                        .replace("<language>", language.getName()))
                .setLore(configManager.getFormattedList(config, "account-menu-language-lore"))
                .setProfileFromURL(language.getTextureURL())
                .build();

        ItemBuilder.addPersistentData(item, key, "language");

        return item;
    }
    private ItemStack getAccountMenuPublic(Account acc, FileConfiguration config) {

        String yes = configManager.getFormattedString(config, "menu-yes");
        String no = configManager.getFormattedString(config, "menu-no");

        ItemStack item = ItemBuilder.builder()
                .setMaterial(getPublicMaterial(acc.isPublicBalance()))
                .setDisplayName(configManager.getFormattedString(config, "account-menu-public")
                        .replace("<public>", (acc.isPublicBalance() ? yes : no)))
                .setLore(configManager.getFormattedList(config, "account-menu-public-lore"))
                .build();

        ItemBuilder.addPersistentData(item, key, "public");

        return item;
    }
    private Material getPublicMaterial(Boolean publicBalance) {
        if(publicBalance) return Material.LIME_TERRACOTTA;
        else return Material.RED_TERRACOTTA;
    }

}
