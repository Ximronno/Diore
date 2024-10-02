package ximronno.bukkit.menu.transactions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import ximronno.bukkit.menu.DioreMenu;
import ximronno.bukkit.menu.transactions.transfer.TransferSelectorMenu;
import ximronno.bukkit.menu.yap.AccountMenu;
import ximronno.bukkit.message.type.menu.MenuItemAdditionsPaths;
import ximronno.bukkit.message.type.menu.MenuItemLorePaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.Transaction;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;

import java.util.*;

public class TransactionsMenu extends DioreMenu {

    private final int DEPOSIT_SLOT = 10;

    private final int WITHDRAW_SLOT = 12;

    private final int TRANSFER_SLOT = 14;

    private final int RECENT_TRANSACTIONS_SLOT = 16;

    @Override
    public Menu getPreviousMenu() {
        return new AccountMenu();
    }

    @Override
    public String getTitle(Locale locale) {
        return messageManager.getMessage(MenuNamesPaths.TRANSACTIONS_MENU_NAME, locale, true);
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, true);

        inventory.setItem(DEPOSIT_SLOT, ItemBuilder.builder()
                .setMaterial(Material.CHEST)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.DEPOSIT, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.DEPOSIT, locale, true))
                .build());

        inventory.setItem(WITHDRAW_SLOT, ItemBuilder.builder()
                .setMaterial(Material.HOPPER)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.WITHDRAW, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.WITHDRAW, locale, true))
                .build());

        inventory.setItem(TRANSFER_SLOT, ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSFER, locale, true))
                .setLore(messageManager.getList(MenuItemLorePaths.TRANSFER, locale, true))
                .build());

        inventory.setItem(RECENT_TRANSACTIONS_SLOT, ItemBuilder.builder()
                .setMaterial(Material.PAPER)
                .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.RECENT_TRANSACTIONS, locale, true))
                .setLore(getRecentTransactionsList(acc, locale))
                .build());
    }

    private List<String> getRecentTransactionsList(Account acc, Locale locale) {
        List<String> list = new ArrayList<>();
        int amount = 5;

        List<Transaction> recentTransactions = acc.getRecentTransactions();

        List<Transaction> recentTransactionsCopy = new ArrayList<>(recentTransactions);
        Collections.reverse(recentTransactionsCopy);

        if(recentTransactionsCopy.isEmpty()) {

            list.add(messageManager.getMessage(MenuItemAdditionsPaths.NO_RECENT_TRANSACTIONS, locale, true));

        }
        else {
            int iterator = 0;
            for (Transaction transaction : recentTransactionsCopy) {
                if(iterator >= amount) break;
                iterator++;
                list.add(messageManager.getMessage(MenuItemAdditionsPaths.RECENT_TRANSACTIONS, locale, true,
                        Map.of("{formatted_amount}", api.getAccountInfoFormatter().getFormattedAmount(transaction.amount(), locale),
                                "{formatted_time}", messageManager.getFormattedTime(locale, transaction.time()))));
            }
        }


        return list;
    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot) {
        switch (slot) {
            case DEPOSIT_SLOT:
                close(p);
                new DepositMenu().open(p);
                break;
            case WITHDRAW_SLOT:
                close(p);
                new WithdrawMenu().open(p);
                break;
            case TRANSFER_SLOT:
                close(p);
                new TransferSelectorMenu().open(p);
                break;
            case RECENT_TRANSACTIONS_SLOT:
                close(p);
                new RecentTransactionsMenu().open(p);
                break;
        }

    }
}
