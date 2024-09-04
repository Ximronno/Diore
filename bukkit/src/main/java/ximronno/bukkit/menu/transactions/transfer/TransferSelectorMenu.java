package ximronno.bukkit.menu.transactions.transfer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import ximronno.bukkit.menu.DiorePaginatedDataMenu;
import ximronno.bukkit.menu.transactions.TransactionsMenu;
import ximronno.bukkit.message.type.menu.MenuItemLorePaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.DiorePlugin;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class TransferSelectorMenu extends DiorePaginatedDataMenu {

    private final NamespacedKey key = DiorePlugin.getKey();

    @Override
    public int getMaxItemsPerPage() {
        return 28;
    }

    @Override
    public Menu getPreviousMenu() {
        return new TransactionsMenu();
    }

    @Override
    public String getTitle(Locale locale) {
        return messageManager.getMessage(MenuNamesPaths.TRANSFER_SELECTOR_MENU_NAME, locale, true);
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, false);

        List<Account> accounts = api.getAccountManager().getAccountsList();
        accounts.remove(acc);

        if(accounts.isEmpty()) {

            inventory.setItem(22, ItemBuilder.builder()
                    .setMaterial(Material.WITHER_SKELETON_SKULL)
                    .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSFER_SELECTOR_NO_ACCOUNTS, locale, true))
                    .setLore(messageManager.getList(MenuItemLorePaths.TRANSFER_SELECTOR_NO_ACCOUNTS, locale, true))
                    .build());

        }
        else {
            int iterator = 10;
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * currentPage + i;
                if(index >= accounts.size()) {
                    break;
                }
                if(accounts.get(index) != null) {
                    Account targetAccount = accounts.get(index);

                    OfflinePlayer target = Bukkit.getOfflinePlayer(targetAccount.getUuid());

                    PlayerProfile profile = null;
                    boolean isOnline = target.isOnline();
                    if(isOnline) {
                        profile = target.getPlayerProfile();
                    }

                    inventory.setItem(iterator, ItemBuilder.builder()
                            .materialSelection(Material.PLAYER_HEAD, Material.SKELETON_SKULL, isOnline)
                            .setDisplayName(messageManager.getMessage(MenuItemNamesPaths.TRANSFER_SELECTOR_HEAD, locale, true, Map.of(
                                    "{target_name}", target.getName()
                            )))
                            .setLore(messageManager.getList(MenuItemLorePaths.TRANSFER_SELECTOR_HEAD, locale, true, Map.of(
                                    "{target_balance}", api.getAccountInfoFormatter().getFormattedBalance(acc, targetAccount, locale)
                            )))
                            .setProfile(profile)
                            .addPersistentData(key, PersistentDataType.STRING, targetAccount.getUuid().toString())
                            .build());

                    if((iterator + 2) % 9 == 0) iterator += 2;
                    iterator++;
                }
            }
        }

        hasNextPage = !((index + 1) >= accounts.size());
        addButtons(locale, true);
    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot, PersistentDataContainer container) {
        if(container.has(key, PersistentDataType.STRING)) {
            String containerString = container.get(key, PersistentDataType.STRING);
            if(containerString == null) return;

            UUID uuid = UUID.fromString(containerString);

            Account targetAcc = api.getAccountManager().getAccount(uuid);
            if(targetAcc == null) return;

            OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);

            PlayerProfile profile = null;
            boolean isOnline = target.isOnline();
            if(isOnline) {
                profile = target.getPlayerProfile();
            }

            close(p);
            new TransferSumSelectorMenu(targetAcc, target, profile).open(p);
        }
    }
}
