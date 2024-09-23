package ximronno.bukkit.menu.transactions.transfer;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.profile.PlayerProfile;
import ximronno.bukkit.menu.DioreMenu;
import ximronno.bukkit.menu.transactions.TransactionsMenu;
import ximronno.bukkit.message.type.menu.MenuItemLorePaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;

import java.util.Locale;
import java.util.Map;

public class TransferConfirmMenu extends DioreMenu {

    Account targetAcc;

    OfflinePlayer target;

    PlayerProfile profile;

    double amount;

    private final int CONFIRM_BUTTON = 12;

    private final int CANCEL_BUTTON = 14;

    TransferConfirmMenu(Account targetAcc, OfflinePlayer target, PlayerProfile profile, double amount) {
        this.targetAcc = targetAcc;
        this.target = target;
        this.amount = amount;
        this.profile = profile;
    }

    @Override
    public Menu getPreviousMenu() {
        return new TransferSumSelectorMenu(targetAcc, target, profile);
    }

    @Override
    public String getTitle(Locale locale) {
        return messageManager.getMessage(MenuNamesPaths.TRANSFER_CONFIRM_MENU_NAME, locale, true);
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, false);

        final int PLAYER_HEAD_SLOT = 4;

        inventory.setItem(PLAYER_HEAD_SLOT, ItemBuilder.builder()
                .materialSelection(Material.PLAYER_HEAD, Material.SKELETON_SKULL, target.isOnline())
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSFER_CONFIRM_HEAD, locale, true, Map.of(
                        "{target_name}", target.getName()
                )))
                .setLore(messageManager.getList(MenuItemLorePaths.TRANSFER_CONFIRM_HEAD, locale, true, Map.of(
                        "{amount}", api.getAccountInfoFormatter().getFormattedAmount(amount, locale)
                )))
                .setProfile(profile)
                .build());

        inventory.setItem(CONFIRM_BUTTON, ItemBuilder.builder()
                .setMaterial(Material.GREEN_TERRACOTTA)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSFER_CONFIRM, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.TRANSFER_CONFIRM, locale, true))
                .build());

        inventory.setItem(CANCEL_BUTTON, ItemBuilder.builder()
                .setMaterial(Material.RED_TERRACOTTA)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSFER_DECLINE, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.TRANSFER_DECLINE, locale, true))
                .build());

    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot) {
        switch (slot) {
            case CONFIRM_BUTTON:
                api.getAccountController().transfer(p, target, acc, targetAcc, locale, amount);
                close(p);
                new TransactionsMenu().open(p);
                break;
            case CANCEL_BUTTON:
                openPreviousMenu(p);
                break;
        }
    }
}
