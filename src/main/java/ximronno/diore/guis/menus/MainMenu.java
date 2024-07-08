package ximronno.diore.guis.menus;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.api.item.ItemBuilder;
import ximronno.diore.Diore;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.model.AccountManager;
import ximronno.diore.model.ConfigManager;

import java.net.URL;
import java.util.Map;

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
    public void handleMenu(Player p, Account acc, Language language, PersistentDataContainer container) {

        if(container.has(key, PersistentDataType.STRING)) {

            String func = container.get(key, PersistentDataType.STRING);
            if(func == null) return;

            switch (func) {
                case "skull":
                    new AccountMenu(key).open(p);
                    break;
                case "issues":
                    TextComponent component = new TextComponent(configManager.getFormattedString(language.getConfig(), "issues-link-text"));

                    component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Ximronno/Diore/issues"));

                    p.spigot().sendMessage(component);
                    p.closeInventory();
                    break;
            }
        }

    }

    @Override
    public void setContents(Player p) {

        Account acc = plugin.getAccountManager().getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Language language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        for(int i = 0; i < inventory.getSize(); i++) {

            switch(i) {
                case 4:
                    inventory.setItem(i, getMainMenuSkull(acc, language.getConfig(), p));
                    break;
                case 29:
                    inventory.setItem(i, getMainMenuIssuesSkull(language.getConfig()));
                    break;
                default:
                    inventory.setItem(i, getMenuBlank());
                    break;
            }

        }

    }
    private ItemStack getMainMenuSkull(Account acc, FileConfiguration config, Player p) {

        String yes = configManager.getFormattedString(config, "menu-yes");
        String no = configManager.getFormattedString(config, "menu-no");

        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(configManager.getFormattedString(config, "main-menu-skull")
                        .replace("<player>", p.getDisplayName()))
                .setLore(configManager.getFormattedList(config, "main-menu-skull-lore",
                                Map.of("<balance>", accountManager.formatBalance(acc.getBalance()),
                                        "<language>", acc.getLanguage().getName(),
                                        "<public>", (acc.isPublicBalance() ? yes : no))))
                .setProfile(p.getPlayerProfile())
                .build();

        ItemBuilder.addPersistentData(item, key, "skull");

        return item;
    }
    private ItemStack getMainMenuIssuesSkull(FileConfiguration config) {
        URL url;
        try {
            url = new URL("https://textures.minecraft.net/texture/26e27da12819a8b053da0cc2b62dec4cda91de6eeec21ccf3bfe6dd8d4436a7");
        } catch(Exception e) {
            return null;
        }

        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(configManager.getFormattedString(config, "main-menu-issues-skull"))
                .setLore(configManager.getFormattedList(config, "main-menu-issues-skull-lore"))
                .setProfileFromURL(url)
                .build();

        ItemBuilder.addPersistentData(item, key, "issues");

        return item;
    }
}
