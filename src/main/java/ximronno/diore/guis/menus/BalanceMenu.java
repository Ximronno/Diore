package ximronno.diore.guis.menus;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.api.interfaces.Transaction;
import ximronno.api.item.ItemBuilder;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.guis.menus.transactions.DepositAmountMenu;
import ximronno.diore.guis.menus.transactions.TransferPlayerSelectorMenu;
import ximronno.diore.guis.menus.transactions.WithdrawAmountMenu;
import ximronno.diore.impl.Languages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BalanceMenu extends DioreMenu {
    public BalanceMenu(NamespacedKey key) {
        super(key);
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public String getTitle() {
        return configManager.getFormattedString("balance-menu-title");
    }
    @Override
    public void handleMenu(Player p, Account acc, Language language, PersistentDataContainer container) {

        if(container.has(key, PersistentDataType.STRING)) {

            String func = container.get(key, PersistentDataType.STRING);
            if(func == null) return;

            switch(func) {
                case "withdraw":
                    new WithdrawAmountMenu(key).open(p);
                    break;
                case "deposit":
                    new DepositAmountMenu(key).open(p);
                    break;
                case "transfer":
                    new TransferPlayerSelectorMenu(key).open(p);
                    break;
                case "transactions":
                    open(p);
                    break;
                case "back":
                    new AccountMenu(key).open(p);
                    break;
            }

        }

    }

    @Override
    public void setContents(Player p) {

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Language language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        for(int i = 0; i < inventory.getSize(); i++) {
            switch(i) {
                case 10:
                    inventory.setItem(i, getWithdrawItem(language.getConfig()));
                    break;
                case 12:
                    inventory.setItem(i, getDepositItem(language.getConfig()));
                    break;
                case 14:
                    inventory.setItem(i, getTransferItem(language.getConfig()));
                    break;
                case 16:
                    inventory.setItem(i, getTransactionItem(language.getConfig(), acc));
                    break;
                case 26:
                    inventory.setItem(i, getMenuBack(language.getConfig()));
                    break;
                default:
                    inventory.setItem(i, getMenuBlank());
                    break;
            }
        }

    }
    private ItemStack getWithdrawItem(FileConfiguration config) {
        return ItemBuilder.builder()
                .setMaterial(Material.DIAMOND_ORE)
                .setDisplayName(configManager.getFormattedString(config, "balance-menu-withdraw"))
                .setLore(configManager.getFormattedList(config, "balance-menu-withdraw-lore"))
                .addPersistentData(key, "withdraw")
                .build();
    }
    private ItemStack getDepositItem(FileConfiguration config) {
        return ItemBuilder.builder()
                .setMaterial(Material.DEEPSLATE_DIAMOND_ORE)
                .setDisplayName(configManager.getFormattedString(config, "balance-menu-deposit"))
                .setLore(configManager.getFormattedList(config, "balance-menu-deposit-lore"))
                .addPersistentData(key, "deposit")
                .build();
    }
    private ItemStack getTransferItem(FileConfiguration config) {
        return ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(configManager.getFormattedString(config, "balance-menu-transfer"))
                .setLore(configManager.getFormattedList(config, "balance-menu-transfer-lore"))
                .addPersistentData(key, "transfer")
                .build();
    }
    private ItemStack getTransactionItem(FileConfiguration config, Account acc) {
        List<String> lore = new ArrayList<>();

        for(Transaction t : acc.getTransactions()) {

            lore.add(configManager.getFormattedString(config, "balance-menu-transactions-format")
                    .replace("<amount>", accountManager.formatBalance(t.amount()))
                    .replace("<date>", formatTimeDifference(t.time(), System.currentTimeMillis(), config)));

        }

        Collections.reverse(lore);

        return ItemBuilder.builder()
                .setMaterial(Material.PAPER)
                .setDisplayName(configManager.getFormattedString(config, "balance-menu-transactions"))
                .setLore(lore)
                .addPersistentData(key, "transactions")
                .build();
    }
    private String formatTimeDifference(long startMillis, long endMillis, FileConfiguration config) {
        long durationMillis = endMillis - startMillis;

        long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis);

        if (hours > 0) {
            return configManager.getFormattedString(config, "menu-hours")
                    .replace("<hour>", String.valueOf(hours));
        }
        else if (minutes > 0) {
            return configManager.getFormattedString(config, "menu-minutes")
                    .replace("<minute>", String.valueOf(minutes));
        }
        else {
            return configManager.getFormattedString(config, "menu-seconds")
                    .replace("<second>", String.valueOf(seconds));
        }
    }
}
