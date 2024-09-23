package ximronno.bukkit.menu.yap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.profile.PlayerProfile;
import ximronno.bukkit.menu.DioreMenu;
import ximronno.bukkit.message.type.menu.MenuItemAdditionsPaths;
import ximronno.bukkit.message.type.menu.MenuItemLorePaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.SortingVariant;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.AccountLeaderBoard;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;
import ximronno.diore.api.menu.MenuSizes;

import java.util.*;

public class LeaderBoardMenu extends DioreMenu {

    private final int SORT_SLOT = 29;

    private SortingVariant sortVariant = SortingVariant.NO_SORT;

    private final AccountLeaderBoard accountLeaderBoard = api.getAccountLeaderBoard();

    @Override
    public MenuSizes getSize() {
        return MenuSizes.FOUR_ROWS;
    }

    @Override
    public Menu getPreviousMenu() {
        return new MainMenu();
    }

    @Override
    public String getTitle(Locale locale) {
        return messageManager.getMessage(MenuNamesPaths.LEADERBOARD_MENU, locale, true);
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, true);

        final int SENDER_TOP = 8;
        final int TOP_ONE = 13;
        final int TOP_TWO = 21;
        final int TOP_THREE = 23;
        final int NO_LEADERS = 13;

        Account topOne = accountLeaderBoard.fromPlace(1, sortVariant);
        Account topTwo = accountLeaderBoard.fromPlace(2, sortVariant);
        Account topThree = accountLeaderBoard.fromPlace(3, sortVariant);
        int senderPlace = accountLeaderBoard.getPlace(acc, sortVariant);

        inventory.setItem(SENDER_TOP, ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .displayNameSelection(messageManager.getMessage(MenuItemNamesPaths.LEADERBOARD_SENDER, locale, true, Map.of("{place}", String.valueOf(senderPlace))),
                        messageManager.getMessage(MenuItemAdditionsPaths.LEADERBOARD_SENDER_NO_PLACE, locale, true),
                        senderPlace > 0
                )
                .setLore(messageManager.getList(MenuItemLorePaths.LEADERBOARD_SENDER, locale, true, Map.of(
                        "{balance}", api.getAccountInfoFormatter().getFormattedBalance(acc, locale)
                )))
                .setProfile(p.getPlayerProfile())
                .build());


        inventory.setItem(SORT_SLOT, ItemBuilder.builder()
                .setMaterial(Material.PAPER)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.LEADERBOARD_SORT, locale, true))
                .setLore(getSortLore(locale))
                .build());

        if(topOne != null) {

            OfflinePlayer topOnePlayer = Bukkit.getOfflinePlayer(topOne.getUuid());
            boolean isOnline = topOnePlayer.isOnline();

            PlayerProfile profile = null;
            if(isOnline) {
                profile = topOnePlayer.getPlayerProfile();
            }

            inventory.setItem(TOP_ONE, ItemBuilder.builder()
                    .materialSelection(Material.PLAYER_HEAD, Material.SKELETON_SKULL, isOnline)
                    .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.LEADERBOARD_TOP_ONE, locale, true, Map.of(
                            "{target_name}", topOnePlayer.getName()
                    )))
                    .setLore(messageManager.getList(MenuItemLorePaths.LEADERBOARD_TOP_ONE, locale, true, Map.of(
                            "{balance}", api.getAccountInfoFormatter().getFormattedAmount(topOne.getBalance())
                    )))
                    .setProfile(profile)
                    .build());

        }

        if(topTwo != null) {

            OfflinePlayer topTwoPlayer = Bukkit.getOfflinePlayer(topTwo.getUuid());
            boolean isOnline = topTwoPlayer.isOnline();

            PlayerProfile profile = null;
            if(isOnline) {
                profile = topTwoPlayer.getPlayerProfile();
            }

            inventory.setItem(TOP_TWO, ItemBuilder.builder()
                    .materialSelection(Material.PLAYER_HEAD, Material.SKELETON_SKULL, isOnline)
                    .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.LEADERBOARD_TOP_TWO, locale, true, Map.of(
                            "{target_name}", topTwoPlayer.getName()
                    )))
                    .setLore(messageManager.getList(MenuItemLorePaths.LEADERBOARD_TOP_TWO, locale, true,Map.of(
                            "{balance}", api.getAccountInfoFormatter().getFormattedAmount(topTwo.getBalance())
                    )))
                    .setProfile(profile)
                    .build());

        }

        if(topThree != null) {

            OfflinePlayer topThreePlayer = Bukkit.getOfflinePlayer(topThree.getUuid());
            boolean isOnline = topThreePlayer.isOnline();
            PlayerProfile profile = null;
            if(isOnline) {
                profile = topThreePlayer.getPlayerProfile();
            }

            inventory.setItem(TOP_THREE, ItemBuilder.builder()
                    .materialSelection(Material.PLAYER_HEAD, Material.SKELETON_SKULL, isOnline)
                    .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.LEADERBOARD_TOP_THREE, locale, true, Map.of(
                            "{target_name}", topThreePlayer.getName()
                    ) ))
                    .setLore(messageManager.getList(MenuItemLorePaths.LEADERBOARD_TOP_THREE, locale, true, Map.of(
                            "{balance}", api.getAccountInfoFormatter().getFormattedAmount(topThree.getBalance())
                    )))
                    .setProfile(profile)
                    .build());

        }

        if(topOne == null && topTwo == null && topThree == null) {

            inventory.setItem(NO_LEADERS, ItemBuilder.builder()
                    .setMaterial(Material.WITHER_SKELETON_SKULL)
                    .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.LEADERBOARD_NO_LEADERS, locale, true))
                    .setLore(messageManager.getList(MenuItemLorePaths.LEADERBOARD_NO_LEADERS, locale, true))
                    .build());

        }

    }

    private List<String> getSortLore(Locale locale) {
        List<String> itemSortLore = messageManager.getList(MenuItemLorePaths.LEADERBOARD_SORT, locale, true);

        HashMap<String, SortingVariant> tagsForArrows = SortingVariant.getTagsForArrows();

        List<String> sortLore = new ArrayList<>();

        for(String loreString : itemSortLore) {

            for(String tag : tagsForArrows.keySet()) {

                if(loreString.contains(tag)) {
                    SortingVariant variant = tagsForArrows.get(tag);

                    if(variant.equals(sortVariant)) {

                        loreString = loreString.replace(tag, messageManager.getMessage(MenuItemAdditionsPaths.LEADERBOARD_SORT_ARROW, locale, true));

                    }
                    else {

                        loreString = loreString.replace(tag, "");

                    }

                }

            }

            sortLore.add(loreString);

        }

        return sortLore;
    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot) {
        if(slot == SORT_SLOT) {
            sortVariant = SortingVariant.cycleVariants(sortVariant);
            update(p);
        }
    }

}
