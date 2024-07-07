package ximronno.diore.guis.menus.transactions;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.api.item.ItemBuilder;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.guis.menus.BalanceMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.utils.AccountUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WithdrawAmountMenu extends DioreMenu {

    protected Account target = null;
    public WithdrawAmountMenu(NamespacedKey key) {
        super(key);
    }
    public WithdrawAmountMenu(NamespacedKey key, Account target) {
        super(key);
        this.target = target;
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public String getTitle() {
        if(target == null) return configManager.getFormattedString("withdraw-menu-title");
        else return configManager.getFormattedString("transfer-menu-title");
    }

    @Override
    public void handleMenu(Player p, Account acc, Languages language, PersistentDataContainer container) {

        if(container.has(key, PersistentDataType.STRING)) {

            String func = container.get(key, PersistentDataType.STRING);
            if(func == null) return;

            switch(func) {

                case "back":
                    if(target == null) new BalanceMenu(key).open(p);
                    else new TransferPlayerSelectorMenu(key).open(p);
                    break;
                case "custom":
                    FileConfiguration config = language.getCFG();

                    List<String> lines = new ArrayList<>();

                    config.getStringList("withdraw-menu-sign")
                            .forEach(loreLine -> lines.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

                    lines.add(0, "");

                    SignGUI gui = SignGUI.builder()
                            .setLines(lines.toArray(new String[0]))
                            .setType(Material.OAK_SIGN)
                            .setHandler((player, result) -> {

                                String line0 = result.getLine(0);

                                if(line0.isEmpty() || line0.isBlank()) {

                                    return List.of(SignGUIAction.openInventory(plugin, getInventory()));

                                }
                                else {

                                    double amount = Double.parseDouble(line0);
                                    if(target == null) AccountUtils.tryWithdraw(p, acc, config, amount);
                                    else AccountUtils.tryTransfer(p, acc, target, config, amount);
                                    new BalanceMenu(key).open(p);

                                    return Collections.emptyList();

                                }

                            })
                            .callHandlerSynchronously(plugin)
                            .build();

                    gui.open(p);
                    break;
                default:
                    double amount = Double.parseDouble(func);
                    if(target == null) AccountUtils.tryWithdraw(p, acc, language.getCFG(), amount);
                    else AccountUtils.tryTransfer(p, acc, target, language.getCFG(), amount);
                    new BalanceMenu(key).open(p);
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

        for(int i = 0; i < getSize(); i++) {
            switch (i) {
                case 4:
                    if(target == null) {
                        inventory.setItem(i, getMenuBlank());
                    }
                    else {
                        inventory.setItem(i, getPlayerSkull(Bukkit.getPlayer(target.getOwner()), language.getCFG()));
                    }
                    break;
                case 10:
                    inventory.setItem(i, getWithdrawAll(language.getCFG(), acc.getBalance()));
                    break;
                case 12:
                    inventory.setItem(i, getWithdrawHalf(language.getCFG(), acc.getBalance()));
                    break;
                case 14:
                    inventory.setItem(i, getWithdrawQuarter(language.getCFG(), acc.getBalance()));
                    break;
                case 16:
                    inventory.setItem(i, getWithdrawCustom(language.getCFG(), acc.getBalance()));
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
    private ItemStack getWithdrawAll(FileConfiguration config, double balance) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.DROPPER)
                .setAmount(64)
                .setDisplayName(configManager.getFormattedString(config, "withdraw-menu-all"))
                .setLore(getLore(config, balance, balance))
                .build();

        ItemBuilder.addPersistentData(item, key, String.valueOf(balance));

        return item;
    }
    private ItemStack getWithdrawHalf(FileConfiguration config, double balance) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.DROPPER)
                .setAmount(32)
                .setDisplayName(configManager.getFormattedString(config, "withdraw-menu-half"))
                .setLore(getLore(config, balance, balance / 2))
                .build();

        ItemBuilder.addPersistentData(item, key, String.valueOf(balance / 2));

        return item;
    }
    private ItemStack getWithdrawQuarter(FileConfiguration config, double balance) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.DROPPER)
                .setDisplayName(configManager.getFormattedString(config, "withdraw-menu-quarter"))
                .setLore(getLore(config, balance, balance / 4))
                .build();

        ItemBuilder.addPersistentData(item, key, String.valueOf(balance / 4));

        return item;
    }
    private ItemStack getWithdrawCustom(FileConfiguration config, double balance) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.OAK_SIGN)
                .setDisplayName(configManager.getFormattedString(config, "withdraw-menu-custom"))
                .setLore(getLore(config, balance, 0))
                .build();

        ItemBuilder.addPersistentData(item, key, "custom");

        return item;
    }
    private ItemStack getPlayerSkull(Player p, FileConfiguration config) {
        return ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(configManager.getFormattedString(config, "transfer-menu-sending-to")
                        .replace("<player>", p.getDisplayName()))
                .setProfile(p.getPlayerProfile())
                .build();
    }
    private List<String> getLore(FileConfiguration config, double balance, double amount) {
        Map<String, String> replacements =
                Map.of("<balance>", accountManager.formatBalance(balance),
                        "<amount>", accountManager.formatBalance(amount));

        if (target == null) {
            return configManager.getFormattedList(config, "withdraw-menu-lore-format", replacements);
        }
        return configManager.getFormattedList(config, "transfer-menu-lore-format", replacements);

    }
}
