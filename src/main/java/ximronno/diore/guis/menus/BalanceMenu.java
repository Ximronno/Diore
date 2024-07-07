package ximronno.diore.guis.menus;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.api.item.ItemBuilder;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.guis.menus.transactions.DepositAmountMenu;
import ximronno.diore.guis.menus.transactions.TransferPlayerSelectorMenu;
import ximronno.diore.guis.menus.transactions.WithdrawAmountMenu;
import ximronno.diore.impl.Languages;

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
    public void handleMenu(Player p, Account acc, Languages language, PersistentDataContainer container) {

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

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        for(int i = 0; i < inventory.getSize(); i++) {
            switch(i) {
                case 10:
                    inventory.setItem(i, getWithdrawItem(language.getCFG()));
                    break;
                case 13:
                    inventory.setItem(i, getDepositItem(language.getCFG()));
                    break;
                case 16:
                    inventory.setItem(i, getTransferItem(language.getCFG()));
                    break;
                case 26:
                    inventory.setItem(i, getMenuBack(language.getCFG()));
                    break;
                default:
                    inventory.setItem(i, getMenuBlank());
                    break;
            }
        }

    }
    private ItemStack getWithdrawItem(FileConfiguration config) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.DIAMOND_ORE)
                .setDisplayName(configManager.getFormattedString(config, "balance-menu-withdraw"))
                .setLore(configManager.getFormattedList(config, "balance-menu-withdraw-lore"))
                .build();

        ItemBuilder.addPersistentData(item, key, "withdraw");

        return item;
    }
    private ItemStack getDepositItem(FileConfiguration config) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.DEEPSLATE_DIAMOND_ORE)
                .setDisplayName(configManager.getFormattedString(config, "balance-menu-deposit"))
                .setLore(configManager.getFormattedList(config, "balance-menu-deposit-lore"))
                .build();

        ItemBuilder.addPersistentData(item, key, "deposit");

        return item;
    }
    private ItemStack getTransferItem(FileConfiguration config) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(configManager.getFormattedString(config, "balance-menu-transfer"))
                .setLore(configManager.getFormattedList(config, "balance-menu-transfer-lore"))
                .build();

        ItemBuilder.addPersistentData(item,key,"transfer");

        return item;
    }
}
