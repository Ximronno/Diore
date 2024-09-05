package ximronno.bukkit.menu.transactions;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.bukkit.listeners.TransactionsListener;
import ximronno.bukkit.menu.DioreDataMenu;
import ximronno.bukkit.message.type.CommandMessagesPaths;
import ximronno.bukkit.message.type.menu.MenuItemLorePaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.DiorePlugin;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WithdrawMenu extends DioreDataMenu {

    private final NamespacedKey key = DiorePlugin.getKey();

    private final int DEPOSIT_SIGN_SLOT = 16;

    private double amount = 0;

    public WithdrawMenu() {
    }

    public WithdrawMenu(double amount) {
        this.amount = amount;
    }

    @Override
    public Menu getPreviousMenu() {
        return new TransactionsMenu();
    }

    @Override
    public String getTitle(Locale locale) {
        return messageManager.getMessage(MenuNamesPaths.WITHDRAW_MENU_NAME, locale, true);
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, true);

        int WITHDRAW_ALL_SLOT = 10;
        int WITHDRAW_HALF_SLOT = 12;
        int WITHDRAW_QUARTER_SLOT = 14;

        double balance = acc.getBalance();
        double oresAmount = round(balance);
        inventory.setItem(WITHDRAW_ALL_SLOT, ItemBuilder.builder()
                .setMaterial(Material.HOPPER)
                .setAmount(64)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.WITHDRAW_ALL, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.WITHDRAW_ALL, locale, true, Map.of(
                        "{amount}", api.getAccountInfoFormatter().getFormattedAmount(oresAmount, locale),
                        "{balance-amount}", api.getAccountInfoFormatter().getFormattedAmount(balance - oresAmount, locale)
                )))
                .addPersistentData(key, PersistentDataType.DOUBLE, oresAmount)
                .build());

        double halfOres = round(oresAmount / 2);
        inventory.setItem(WITHDRAW_HALF_SLOT, ItemBuilder.builder()
                .setMaterial(Material.HOPPER)
                .setAmount(32)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.WITHDRAW_HALF, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.WITHDRAW_HALF, locale, true, Map.of(
                        "{amount}", api.getAccountInfoFormatter().getFormattedAmount(halfOres, locale),
                        "{balance-amount}", api.getAccountInfoFormatter().getFormattedAmount(balance - halfOres, locale)
                )))
                .addPersistentData(key, PersistentDataType.DOUBLE, halfOres)
                .build());

        double quarterOres = round(oresAmount / 4);
        inventory.setItem(WITHDRAW_QUARTER_SLOT, ItemBuilder.builder()
                .setMaterial(Material.HOPPER)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.WITHDRAW_QUARTER, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.WITHDRAW_QUARTER, locale, true, Map.of(
                        "{amount}", api.getAccountInfoFormatter().getFormattedAmount(quarterOres, locale),
                        "{balance-amount}", api.getAccountInfoFormatter().getFormattedAmount(balance - quarterOres, locale)
                )))
                .addPersistentData(key, PersistentDataType.DOUBLE, quarterOres)
                .build());

        inventory.setItem(DEPOSIT_SIGN_SLOT, ItemBuilder.builder()
                .setMaterial(Material.HOPPER)
                .displayNameSelection(messageManager.getMessage(MenuItemNamesPaths.WITHDRAW_CUSTOM, locale, true),
                        messageManager.getMessage(MenuItemNamesPaths.WITHDRAW_CUSTOM_AMOUNT, locale, true),
                        amount <= 0)
                .loreSelection(messageManager.getList(MenuItemLorePaths.WITHDRAW_CUSTOM, locale, true),
                        messageManager.getList(MenuItemLorePaths.WITHDRAW_CUSTOM_AMOUNT, locale, true, Map.of(
                                "{amount}", api.getAccountInfoFormatter().getFormattedAmount(amount, locale),
                                "{balance-amount}", api.getAccountInfoFormatter().getFormattedAmount(balance - amount, locale)
                        )),
                        amount <= 0)
                .addPersistentData(key, PersistentDataType.DOUBLE, amount)
                .build());
    }

    private double round(double amount) {
        BigDecimal bd = new BigDecimal(Double.toString(amount));
        bd = bd.setScale(1, RoundingMode.FLOOR);

        return bd.doubleValue();
    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot, PersistentDataContainer container) {
        if(slot == DEPOSIT_SIGN_SLOT) {
            if(amount <= 0) {
                callSign(p, locale);
                return;
            }
            else {
                if(e.getClick().equals(ClickType.RIGHT)) {
                    callSign(p, locale);
                    return;
                }
            }
        }
        if(container.has(key, PersistentDataType.DOUBLE)) {
            double sumToDeposit = container.get(key, PersistentDataType.DOUBLE);
            if(sumToDeposit <= 0) {
                return;
            }

            api.getAccountController().withdraw(p, acc, locale, sumToDeposit);
            update(p);
        }
    }
    private void callSign(Player p, Locale locale) {
        SignGUI signGUI = SignGUI.builder()
                .setType(Material.DARK_OAK_SIGN)
                .setColor(DyeColor.LIGHT_BLUE)
                .setLine(1, "↑")
                .setLine(2, "|")
                .setLine(3, "Write amount to withdraw!")
                .setHandler(((player, signGUIResult) -> {
                    String line = signGUIResult.getLine(0);

                    double doubleFromLine;
                    try {
                        doubleFromLine = Double.parseDouble(line);
                    } catch(Exception exc) {
                        return List.of(SignGUIAction.runSync(DiorePlugin.getInstance().getJavaPlugin(), () -> new WithdrawMenu().open(p)),
                                SignGUIAction.run(() -> player.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_INVALID_AMOUNT, locale, true))));
                    }

                    return List.of(SignGUIAction.runSync(DiorePlugin.getInstance().getJavaPlugin(), () -> new WithdrawMenu(doubleFromLine).open(p)));
                }))
                .build();

        signGUI.open(p);
    }
}
