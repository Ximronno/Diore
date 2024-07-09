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
import ximronno.api.interfaces.Language;
import ximronno.api.item.ItemBuilder;
import ximronno.diore.Diore;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.DioreConfigManager;

public class AccountMenu extends DioreMenu {
    private final Diore plugin = Diore.getInstance();
    private final AccountManager accountManager = plugin.getAccountManager();
    private final DioreConfigManager configManager = plugin.getConfigManager();

    public AccountMenu(NamespacedKey key) {
        super(key);
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public String getTitle() {
        return configManager.getFormattedString("account-menu-title");
    }
    @Override
    public void handleMenu(Player p, Account acc, Language language, PersistentDataContainer container) {

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

        Language language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        for(int i = 0; i < inventory.getSize(); i++) {

            switch(i) {
                case 10:
                    inventory.setItem(i, getAccountMenuBalance(acc, language.getConfig()));
                    break;
                case 13:
                    inventory.setItem(i, getAccountMenuLanguage(language));
                    break;
                case 16:
                    inventory.setItem(i, getAccountMenuPublic(acc, language.getConfig()));
                    break;
                case 26:
                    inventory.setItem(i, getMenuBack(language.getConfig()));
                    break;
                default:
                    inventory.setItem(i, getMenuBlank());
                    break;
            }

        }

    }
    private ItemStack getAccountMenuBalance(Account acc, FileConfiguration config) {
        return ItemBuilder.builder()
                .setMaterial(Material.DIAMOND_ORE)
                .setDisplayName(configManager.getFormattedString(config, "account-menu-balance")
                        .replace("<balance>", accountManager.formatBalance(acc.getBalance())))
                .setLore(configManager.getFormattedList(config, "account-menu-balance-lore"))
                .addPersistentData(key, "balance")
                .build();
    }
    private ItemStack getAccountMenuLanguage(@NotNull Language language) {
        FileConfiguration config = language.getConfig();

        return ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(configManager.getFormattedString(config, "account-menu-language")
                        .replace("<language>", language.getName()))
                .setLore(configManager.getFormattedList(config, "account-menu-language-lore"))
                .setProfileFromURL(language.getTextureURL())
                .addPersistentData(key, "language")
                .build();
    }
    private ItemStack getAccountMenuPublic(Account acc, FileConfiguration config) {
        String yes = configManager.getFormattedString(config, "menu-yes");
        String no = configManager.getFormattedString(config, "menu-no");

        return ItemBuilder.builder()
                .setMaterial(getPublicMaterial(acc.isPublicBalance()))
                .setDisplayName(configManager.getFormattedString(config, "account-menu-public")
                        .replace("<public>", (acc.isPublicBalance() ? yes : no)))
                .setLore(configManager.getFormattedList(config, "account-menu-public-lore"))
                .addPersistentData(key, "public")
                .build();
    }
    private Material getPublicMaterial(Boolean publicBalance) {
        if(publicBalance) return Material.LIME_TERRACOTTA;
        else return Material.RED_TERRACOTTA;
    }

}
