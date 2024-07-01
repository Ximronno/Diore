package ximronno.diore.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import ximronno.diore.Diore;
import ximronno.diore.api.interfaces.Account;
import ximronno.diore.impl.Languages;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountGUI {

    private static final Diore plugin = Diore.getInstance();
    private static final ConfigManager configManager = plugin.getConfigManager();
    private static final AccountManager accountManager = plugin.getAccountManager();
    private static final NamespacedKey dioreMenuKey = new NamespacedKey(plugin, "diore-menu-key");
    private static final NamespacedKey dioreLanguageKey = new NamespacedKey(plugin, "diore-language-key");

    public static void openMainMenu(Player p) {

        Inventory inv = Bukkit.createInventory(p, 36, plugin.getConfigManager().getFormattedString("main-menu-title"));

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        for(int i = 0; i < inv.getSize(); i++) {

            switch(i) {
                case 4:
                    inv.setItem(i, getMainMenuSkull(acc, language.getCFG(), p));
                    break;
                case 8:
                    inv.setItem(i, getMainMenuTopSkull(language.getCFG()));
                    break;
                default:
                    inv.setItem(i, getMenuBlank());
                    break;
            }

        }

        p.openInventory(inv);

    }
    private static ItemStack getMenuBlank() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(" ");

        item.setItemMeta(meta);

        return item;
    }
    private static ItemStack getMenuBack(FileConfiguration config, String backTo) {
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

        meta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "back-" + backTo);

        skull.setItemMeta(meta);

        return skull;
    }
    private static ItemStack getMainMenuSkull(Account acc, FileConfiguration config, Player p) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if(skullMeta == null) return null;

        skullMeta.setDisplayName(AccountGUI.configManager.getFormattedString("main-menu-skull", config)
                .replace("<player>", p.getDisplayName()));
        skullMeta.setOwningPlayer(p);

        List<String> lore = new ArrayList<>();

        String yes = AccountGUI.configManager.getFormattedString("menu-yes", config);
        String no = AccountGUI.configManager.getFormattedString("menu-no", config);

        config.getStringList("main-menu-skull-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)
                        .replace("<balance>", AccountGUI.accountManager.formatBalance(acc.getBalance()))
                        .replace("<language>", acc.getLanguage().getName())
                        .replace("<public>", (acc.isPublicBalance() ? yes : no))));

        skullMeta.setLore(lore);

        skullMeta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "skull");

        skull.setItemMeta(skullMeta);

        return skull;
    }
    private static ItemStack getMainMenuTopSkull(FileConfiguration config) {
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

        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if(meta == null) return null;

        meta.setOwnerProfile(profile);

        meta.setDisplayName(AccountGUI.configManager.getFormattedString("main-menu-top-skull", config));

        meta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "top");

        skull.setItemMeta(meta);

        return skull;
    }
    public static void openAccountMenu(Player p) {

        Inventory inv = Bukkit.createInventory(p, 27, plugin.getConfigManager().getFormattedString("account-menu-title"));

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        for(int i = 0; i < inv.getSize(); i++) {

            switch(i) {
                case 10:
                    inv.setItem(i, getAccountMenuBalance(acc, language.getCFG()));
                    break;
                case 13:
                    inv.setItem(i, getAccountMenuLanguage(language));
                    break;
                case 16:
                    inv.setItem(i, getAccountMenuPublic(acc, language.getCFG()));
                    break;
                case 26:
                    inv.setItem(i, getMenuBack(language.getCFG(), "main-menu"));
                    break;
                default:
                    inv.setItem(i, getMenuBlank());
                    break;
            }

        }

        p.openInventory(inv);
    }
    private static ItemStack getAccountMenuBalance(Account acc, FileConfiguration config) {
        ItemStack item = new ItemStack(Material.DIAMOND_ORE);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("account-menu-balance", config)
                .replace("<balance>", accountManager.formatBalance(acc.getBalance())));

        List<String> lore = new ArrayList<>();

        config.getStringList("account-menu-balance-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "balance");

        item.setItemMeta(meta);

        return item;
    }
    private static ItemStack getAccountMenuLanguage(Languages language) {
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

        meta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "language");

        skull.setItemMeta(meta);

        return skull;
    }
    private static ItemStack getAccountMenuPublic(Account acc, FileConfiguration config) {
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

        meta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "public");

        item.setItemMeta(meta);

        return item;
    }
    public static void openPublicBalanceMenu(Player p) {

        Inventory inv = Bukkit.createInventory(p, 27, configManager.getFormattedString("public-balance-menu-title"));

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        for(int i = 0; i < inv.getSize(); i++) {

            switch(i) {
                case 11:
                    inv.setItem(i, getPublicBalanceTrue(language.getCFG()));
                    break;
                case 15:
                    inv.setItem(i, getPublicBalanceFalse(language.getCFG()));
                    break;
                case 26:
                    inv.setItem(i, getMenuBack(language.getCFG(), "account-menu"));
                    break;
                default:
                    inv.setItem(i, getMenuBlank());
                    break;
            }

        }


        p.openInventory(inv);
    }
    private static ItemStack getPublicBalanceTrue(FileConfiguration config) {
        ItemStack item = new ItemStack(Material.GREEN_TERRACOTTA);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("public-balance-menu-true", config));

        List<String> lore = new ArrayList<>();

        config.getStringList("public-balance-menu-true-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "true");

        item.setItemMeta(meta);

        return item;
    }
    private static ItemStack getPublicBalanceFalse(FileConfiguration config) {
        ItemStack item = new ItemStack(Material.RED_TERRACOTTA);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("public-balance-menu-false", config));

        List<String> lore = new ArrayList<>();

        config.getStringList("public-balance-menu-false-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "false");

        item.setItemMeta(meta);

        return item;
    }
    public static void openLanguagesMenu(Player p) {

        Inventory inv = Bukkit.createInventory(p, 27, configManager.getFormattedString("languages-menu-title"));

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Languages accLanguage = acc.getLanguage();
        if(accLanguage == null) accLanguage = Languages.ENGLISH;

        for(int i = 0; i < inv.getSize(); i++) {

            if(i == 26) inv.setItem(i, getMenuBack(accLanguage.getCFG(), "account-menu"));

            else inv.setItem(i, getMenuBlank());
        }
        int languageIterator = 10;
        for(Languages language : Languages.values()) {

            inv.setItem(languageIterator, getLanguageItem(language));

            if((languageIterator + 2) % 9 == 0) languageIterator += 3;
            else languageIterator += 1;

        }


        p.openInventory(inv);
    }
    private static ItemStack getLanguageItem(Languages language) {
        ItemStack item = language.getItemStack();

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(language.getName());

        meta.getPersistentDataContainer().set(dioreLanguageKey, PersistentDataType.STRING, language.name());

        item.setItemMeta(meta);

        return item;


    }
    public static void openBalanceMenu(Player p) {
        Inventory inv = Bukkit.createInventory(p, 27, configManager.getFormattedString("balance-menu-title"));

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        for(int i = 0; i < inv.getSize(); i++) {
            switch(i) {
                case 10:
                    inv.setItem(i, getWithdrawItem(language.getCFG()));
                    break;
                case 13:
                    inv.setItem(i, getDepositItem(language.getCFG()));
                    break;
                case 16:
                    inv.setItem(i, getTransferItem(language.getCFG()));
                    break;
                case 26:
                    inv.setItem(i, getMenuBack(language.getCFG(), "account-menu"));
                    break;
                default:
                    inv.setItem(i, getMenuBlank());
                    break;
            }
        }

        p.openInventory(inv);
    }
    private static ItemStack getWithdrawItem(FileConfiguration config) {
        ItemStack item = new ItemStack(Material.DIAMOND_ORE);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("balance-menu-withdraw", config));

        List<String> lore = new ArrayList<>();

        config.getStringList("balance-menu-withdraw-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "withdraw");

        item.setItemMeta(meta);

        return item;
    }
    private static ItemStack getDepositItem(FileConfiguration config) {
        ItemStack item = new ItemStack(Material.DEEPSLATE_DIAMOND_ORE);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("balance-menu-deposit", config));

        List<String> lore = new ArrayList<>();

        config.getStringList("balance-menu-deposit-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "deposit");

        item.setItemMeta(meta);

        return item;
    }
    private static ItemStack getTransferItem(FileConfiguration config) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("balance-menu-transfer", config));

        List<String> lore = new ArrayList<>();

        config.getStringList("balance-menu-transfer-lore")
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(dioreMenuKey, PersistentDataType.STRING, "transfer");

        item.setItemMeta(meta);

        return item;
    }
    public static void OpenTopMenu(Player p) {

        Inventory inv = Bukkit.createInventory(p, 27, configManager.getFormattedString("top-menu-title"));

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;


        for(int i = 0; i < inv.getSize(); i++) {
            switch(i) {
                case 26:
                    inv.setItem(i, getMenuBack(language.getCFG(), "main-menu"));
                    break;
                default:
                    inv.setItem(i, getMenuBlank());
                    break;
            }
        }

        p.openInventory(inv);
    }
    public static NamespacedKey getDioreMenuKey() {
        return dioreMenuKey;
    }
    public static NamespacedKey getDioreLanguageKey() {
        return dioreLanguageKey;
    }




}
