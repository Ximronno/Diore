package ximronno.bukkit.menu.transactions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import ximronno.bukkit.menu.DiorePaginatedMenu;
import ximronno.bukkit.message.type.menu.MenuItemAdditionsPaths;
import ximronno.bukkit.message.type.menu.MenuItemLorePaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.Transaction;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecentTransactionsMenu extends DiorePaginatedMenu {

    @Override
    public Menu getPreviousMenu() {
        return new TransactionsMenu();
    }

    @Override
    public int getMaxItemsPerPage() {
        return 28;
    }

    @Override
    public String getTitle(Locale locale) {
        return messageManager.getMessage(MenuNamesPaths.RECENT_TRANSACTIONS_MENU_NAME, locale, true);
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, false);

        List<Transaction> transactions = acc.getRecentTransactions();
        if(transactions.isEmpty()) {

            inventory.setItem(22, ItemBuilder.builder()
                    .setMaterial(Material.PAPER)
                    .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.NO_RECENT_TRANSACTIONS, locale, true))
                    .setLore(messageManager.getList(MenuItemLorePaths.NO_RECENT_TRANSACTIONS, locale, true))
                    .build());

        }
        else {
            int iterator = 10;
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * currentPage + i;
                if(index >= transactions.size()) {
                    break;
                }
                if(transactions.get(index) != null) {
                    Transaction transaction = transactions.get(index);

                    inventory.setItem(iterator, ItemBuilder.builder()
                            .setMaterial(Material.PAPER)
                            .setDisplayName(messageManager.getMessage(MenuItemAdditionsPaths.RECENT_TRANSACTIONS, locale, true,
                                    Map.of("{formatted_amount}", api.getAccountInfoFormatter().getFormattedAmount(transaction.amount(), locale),
                                            "{formatted_time}", messageManager.getFormattedTime(locale, transaction.time()))))
                            .build());

                    if((iterator + 2) % 9 == 0) iterator += 2;
                    iterator++;
                }
            }
        }
        hasNextPage = !((index + 1) >= transactions.size());
        addButtons(locale, true);
    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot) {
    }


}
