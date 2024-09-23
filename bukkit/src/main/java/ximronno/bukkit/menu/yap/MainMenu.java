package ximronno.bukkit.menu.yap;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import ximronno.bukkit.menu.DioreMenu;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.bukkit.message.type.LanguagePath;
import ximronno.bukkit.message.type.menu.MenuItemLorePaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;

import java.util.Locale;
import java.util.Map;

public class MainMenu extends DioreMenu {

    private final int PLAYER_HEAD_SLOT = 4;

    private final int LEADERBOARD_SLOT = 8;

    private final int GITHUB_ISSUES_SLOT = 20;

    @Override
    public Menu getPreviousMenu() {
        return null;
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, true);

        inventory.setItem(PLAYER_HEAD_SLOT, ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.PLAYER_HEAD, locale, true, Map.of("{player_name}",p.getDisplayName())))
                .setLore(messageManager.getList(MenuItemLorePaths.PLAYER_SKULL, locale, true,
                        Map.of("{balance}", api.getAccountInfoFormatter().getFormattedBalance(acc, locale),
                                "{lang_name}", messageManager.getMessage(LanguagePath.NAME, locale, true))))
                .setProfile(p.getPlayerProfile())
                .build());

        inventory.setItem(LEADERBOARD_SLOT, ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.LEADERBOARD_HEAD, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.LEADERBOARD_HEAD, locale, true))
                .build());

        inventory.setItem(GITHUB_ISSUES_SLOT, ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.GITHUB_ISSUES, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.GITHUB_ISSUES, locale, true))
                .setProfileFromURL("https://textures.minecraft.net/texture/26e27da12819a8b053da0cc2b62dec4cda91de6eeec21ccf3bfe6dd8d4436a7")
                .build());
    }

    @Override
    public String getTitle(Locale locale) {
        return api.getMessageManager().getMessage(MenuNamesPaths.MAIN_MENU_NAME, locale, true);
    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot) {

        switch (slot) {
            case PLAYER_HEAD_SLOT:
                close(p);
                new AccountMenu().open(p);
                break;
            case LEADERBOARD_SLOT:
                close(p);
                new LeaderBoardMenu().open(p);
                break;
            case GITHUB_ISSUES_SLOT:
                TextComponent component = new TextComponent(api.getMessageManager().getMessage(CommandMessagesPaths.ISSUES_LINK, locale, true));

                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Ximronno/Diore/issues"));

                p.spigot().sendMessage(component);
                close(p);
                break;
        }
        if(slot == BACK_BUTTON_SLOT) {
            close(p);
        }
    }
}
