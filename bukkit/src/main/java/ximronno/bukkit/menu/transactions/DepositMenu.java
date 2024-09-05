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

public class DepositMenu extends DioreDataMenu {

    private final NamespacedKey key = DiorePlugin.getKey();

    private final int DEPOSIT_SIGN_SLOT = 16;

    private double amount = 0;

    public DepositMenu() {
    }

    public DepositMenu(double amount) {
        this.amount = amount;
    }

    @Override
    public Menu getPreviousMenu() {
        return new TransactionsMenu();
    }

    @Override
    public String getTitle(Locale locale) {
        return messageManager.getMessage(MenuNamesPaths.DEPOSIT_MENU_NAME, locale, true);
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, true);

        int[] ores = TransactionsListener.getDiamondOres(p.getInventory(), DiorePlugin.getInstance());

        int diamondOres = ores[0];
        int deepslateDiamondOres = ores[1];
        int diamondNuggets = ores[2];

        double rawOresAmount = diamondOres + deepslateDiamondOres + (diamondNuggets / 10.0);

        int DEPOSIT_ALL_SLOT = 10;
        int DEPOSIT_HALF_SLOT = 12;
        int DEPOSIT_QUARTER_SLOT = 14;

        double oresAmount = round(rawOresAmount);
        inventory.setItem(DEPOSIT_ALL_SLOT, ItemBuilder.builder()
                .setMaterial(Material.CHEST)
                .setAmount(64)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.DEPOSIT_ALL, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.DEPOSIT_ALL, locale, true, Map.of(
                        "{amount}", api.getAccountInfoFormatter().getFormattedAmount(oresAmount, locale)
                )))
                .addPersistentData(key, PersistentDataType.DOUBLE, oresAmount)
                .build());


        double halfOres = round(oresAmount / 2);
        inventory.setItem(DEPOSIT_HALF_SLOT, ItemBuilder.builder()
                .setMaterial(Material.CHEST)
                .setAmount(32)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.DEPOSIT_HALF, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.DEPOSIT_HALF, locale, true, Map.of(
                                "{amount}", api.getAccountInfoFormatter().getFormattedAmount(halfOres, locale)
                )))
                .addPersistentData(key, PersistentDataType.DOUBLE, halfOres)
                .build());

        double quarterOres = round(oresAmount / 4);
        inventory.setItem(DEPOSIT_QUARTER_SLOT, ItemBuilder.builder()
                .setMaterial(Material.CHEST)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.DEPOSIT_QUARTER, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.DEPOSIT_QUARTER, locale, true,  Map.of(
                        "{amount}", api.getAccountInfoFormatter().getFormattedAmount(quarterOres, locale)
                )))
                .addPersistentData(key, PersistentDataType.DOUBLE, quarterOres)
                .build());

        boolean condition = amount <= 0;

        inventory.setItem(DEPOSIT_SIGN_SLOT, ItemBuilder.builder()
                .setMaterial(Material.CHEST)
                .displayNameSelection(messageManager.getMessage(MenuItemNamesPaths.DEPOSIT_CUSTOM, locale, true),
                        messageManager.getMessage(MenuItemNamesPaths.DEPOSIT_CUSTOM_AMOUNT, locale, true),
                        condition)
                .loreSelection(messageManager.getList(MenuItemLorePaths.DEPOSIT_CUSTOM, locale, true),
                        messageManager.getList(MenuItemLorePaths.DEPOSIT_CUSTOM_AMOUNT, locale, true,  Map.of(
                                "{amount}", api.getAccountInfoFormatter().getFormattedAmount(amount, locale)
                        )),
                        condition)
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
                if (e.getClick().equals(ClickType.RIGHT)) {
                    callSign(p, locale);
                    return;
                }
            }
        }
        if(container.has(key, PersistentDataType.DOUBLE)) {
            Double sumToDeposit = container.get(key, PersistentDataType.DOUBLE);
            if(sumToDeposit == null || sumToDeposit <= 0) {
                return;
            }

            api.getAccountController().deposit(p, acc, locale, sumToDeposit);
            update(p);
        }
    }

    private void callSign(Player p, Locale locale) {
        SignGUI signGUI = SignGUI.builder()
                .setType(Material.DARK_OAK_SIGN)

                .setColor(DyeColor.LIGHT_BLUE)
                .setLine(1, "↑")
                .setLine(2, "|")
                .setLine(3, "Write amount to deposit!")
                .setHandler(((player, signGUIResult) -> {
                    String line = signGUIResult.getLine(0);

                    double doubleFromLine;
                    try {
                        doubleFromLine = Double.parseDouble(line);
                    } catch(Exception exc) {
                        return List.of(SignGUIAction.runSync(DiorePlugin.getInstance().getJavaPlugin(), () -> new DepositMenu().open(p)),
                                SignGUIAction.run(() -> player.sendMessage(messageManager.getMessage(CommandMessagesPaths.BALANCE_INVALID_AMOUNT, locale, true))));
                    }

                    return List.of(SignGUIAction.runSync(DiorePlugin.getInstance().getJavaPlugin(), () -> new DepositMenu(doubleFromLine).open(p)));
                }))
                .build();

        signGUI.open(p);
    }

}

