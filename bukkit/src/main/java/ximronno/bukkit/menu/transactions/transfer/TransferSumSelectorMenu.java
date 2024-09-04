package ximronno.bukkit.menu.transactions.transfer;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import ximronno.bukkit.menu.DioreDataMenu;
import ximronno.bukkit.message.type.menu.MenuItemLorePaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.DiorePlugin;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransferSumSelectorMenu extends DioreDataMenu {

    private final NamespacedKey key = DiorePlugin.getKey();

    private final int DEPOSIT_SIGN_SLOT = 16;

    private double amount = 0;

    Account targetAcc;

    OfflinePlayer target;

    PlayerProfile profile;

    public TransferSumSelectorMenu(Account targetAcc, OfflinePlayer target, PlayerProfile profile) {
        this.targetAcc = targetAcc;
        this.target = target;
        this.profile = profile;
    }

    public TransferSumSelectorMenu(Account targetAcc, OfflinePlayer target, PlayerProfile profile, double amount) {
        this(targetAcc, target, profile);
        this.amount = amount;
    }


    @Override
    public Menu getPreviousMenu() {
        return new TransferSelectorMenu();
    }

    @Override
    public String getTitle(Locale locale) {
        return messageManager.getMessage(MenuNamesPaths.TRANSFER_SUM_SELECTOR_MENU_NAME, locale, true);
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, true);

        final int PLAYER_HEAD_SLOT = 4;
        final int WITHDRAW_ALL_SLOT = 10;
        final int WITHDRAW_HALF_SLOT = 12;
        final int WITHDRAW_QUARTER_SLOT = 14;

        inventory.setItem(PLAYER_HEAD_SLOT, ItemBuilder.builder()
                .materialSelection(Material.PLAYER_HEAD, Material.SKELETON_SKULL, target.isOnline())
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSFER_SUM_SELECTOR_HEAD, locale, true, Map.of(
                        "{target_name}", target.getName()
                )))
                .setLore(messageManager.getList(MenuItemLorePaths.TRANSFER_SUM_SELECTOR_HEAD, locale, true))
                .setProfile(profile)
                .build());

        double balance = acc.getBalance();
        double oresAmount = balance;
        inventory.setItem(WITHDRAW_ALL_SLOT, ItemBuilder.builder()
                .setMaterial(Material.HOPPER)
                .setAmount(64)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSFER_ALL, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.TRANSFER_ALL, locale, true, Map.of(
                        "{amount}", api.getAccountInfoFormatter().getFormattedAmount(oresAmount, locale),
                        "{balance-amount}", api.getAccountInfoFormatter().getFormattedAmount(balance - oresAmount, locale)
                )))
                .addPersistentData(key, PersistentDataType.DOUBLE, oresAmount)
                .build());

        double halfOres = oresAmount / 2;
        inventory.setItem(WITHDRAW_HALF_SLOT, ItemBuilder.builder()
                .setMaterial(Material.HOPPER)
                .setAmount(32)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSFER_HALF, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.TRANSFER_HALF, locale, true, Map.of(
                        "{amount}", api.getAccountInfoFormatter().getFormattedAmount(halfOres, locale),
                        "{balance-amount}", api.getAccountInfoFormatter().getFormattedAmount(balance - halfOres, locale)
                )))
                .addPersistentData(key, PersistentDataType.DOUBLE, halfOres)
                .build());

        double quarterOres = oresAmount / 4;
        inventory.setItem(WITHDRAW_QUARTER_SLOT, ItemBuilder.builder()
                .setMaterial(Material.HOPPER)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSFER_QUARTER, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.TRANSFER_QUARTER, locale, true, Map.of(
                        "{amount}", api.getAccountInfoFormatter().getFormattedAmount(quarterOres, locale),
                        "{balance-amount}", api.getAccountInfoFormatter().getFormattedAmount(balance - quarterOres, locale)
                )))
                .addPersistentData(key, PersistentDataType.DOUBLE, quarterOres)
                .build());

        inventory.setItem(DEPOSIT_SIGN_SLOT, ItemBuilder.builder()
                .setMaterial(Material.HOPPER)
                .displayNameSelection(messageManager.getMessage(MenuItemNamesPaths.TRANSFER_CUSTOM, locale, true),
                        messageManager.getMessage(MenuItemNamesPaths.TRANSFER_CUSTOM_AMOUNT, locale, true),
                        amount <= 0)
                .loreSelection(messageManager.getList(MenuItemLorePaths.TRANSFER_CUSTOM, locale, true),
                        messageManager.getList(MenuItemLorePaths.TRANSFER_CUSTOM_AMOUNT, locale, true, Map.of(
                                "{amount}", api.getAccountInfoFormatter().getFormattedAmount(amount, locale),
                                "{balance-amount}", api.getAccountInfoFormatter().getFormattedAmount(balance - amount, locale)
                        )),
                        amount <= 0)
                .addPersistentData(key, PersistentDataType.DOUBLE, amount)
                .build());
    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot, PersistentDataContainer container) {
        if(slot == DEPOSIT_SIGN_SLOT) {
            if(amount <= 0) {
                callSign(p);
                return;
            }
            else {
                if (e.getClick().equals(ClickType.RIGHT)) {
                    callSign(p);
                    return;
                }
            }
        }
        if(container.has(key, PersistentDataType.DOUBLE)) {
            double sumToDeposit = container.get(key, PersistentDataType.DOUBLE);
            if(sumToDeposit <= 0) {
                return;
            }

            close(p);
            new TransferConfirmMenu(targetAcc, target, profile, sumToDeposit).open(p);
        }
    }

    private void callSign(Player p) {
        SignGUI signGUI = SignGUI.builder()
                .setType(Material.DARK_OAK_SIGN)
                .setHandler(((player, signGUIResult) -> {
                    String line = signGUIResult.getLine(0);

                    double doubleFromLine;
                    try {
                        doubleFromLine = Double.parseDouble(line);
                    } catch(Exception exc) {
                        return Collections.emptyList();
                    }

                    return List.of(SignGUIAction.runSync(DiorePlugin.getInstance().getJavaPlugin(), () -> new TransferSumSelectorMenu(targetAcc, target, profile, doubleFromLine).open(p)));
                }))
                .build();

        signGUI.open(p);
    }
}
