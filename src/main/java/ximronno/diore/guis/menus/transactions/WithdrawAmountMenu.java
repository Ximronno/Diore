package ximronno.diore.guis.menus.transactions;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
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
    public void handleMenu(Player p, Account acc, Language language, PersistentDataContainer container) {

        if(container.has(key, PersistentDataType.STRING)) {

            String func = container.get(key, PersistentDataType.STRING);
            if(func == null) return;

            switch(func) {

                case "back":
                    if(target == null) new BalanceMenu(key).open(p);
                    else new TransferPlayerSelectorMenu(key).open(p);
                    break;
                case "custom":
                    FileConfiguration config = language.getConfig();

                    List<String> lines = configManager.getFormattedList(config, "withdraw-menu-sign");
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
                    if(target == null) AccountUtils.tryWithdraw(p, acc, language.getConfig(), amount);
                    else AccountUtils.tryTransfer(p, acc, target, language.getConfig(), amount);
                    new BalanceMenu(key).open(p);
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

        for(int i = 0; i < getSize(); i++) {
            switch (i) {
                case 4:
                    if(target == null) {
                        inventory.setItem(i, getMenuBlank());
                    }
                    else {
                        inventory.setItem(i, getPlayerSkull(Bukkit.getPlayer(target.getOwner()), language.getConfig()));
                    }
                    break;
                case 10:
                    inventory.setItem(i, getWithdrawAll(language.getConfig(), acc.getBalance()));
                    break;
                case 12:
                    inventory.setItem(i, getWithdrawHalf(language.getConfig(), acc.getBalance()));
                    break;
                case 14:
                    inventory.setItem(i, getWithdrawQuarter(language.getConfig(), acc.getBalance()));
                    break;
                case 16:
                    inventory.setItem(i, getWithdrawCustom(language.getConfig(), acc.getBalance()));
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
    private ItemStack getWithdrawAll(FileConfiguration config, double balance) {
        return ItemBuilder.builder()
                .setMaterial(Material.DROPPER)
                .setAmount(64)
                .setDisplayName(configManager.getFormattedString(config, "withdraw-menu-all"))
                .setLore(getLore(config, balance, balance))
                .addPersistentData(key, String.valueOf(balance))
                .build();
    }
    private ItemStack getWithdrawHalf(FileConfiguration config, double balance) {
        double amount = balance / 2;
        return ItemBuilder.builder()
                .setMaterial(Material.DROPPER)
                .setAmount(32)
                .setDisplayName(configManager.getFormattedString(config, "withdraw-menu-half"))
                .setLore(getLore(config, balance, amount))
                .addPersistentData(key, String.valueOf(amount))
                .build();
    }
    private ItemStack getWithdrawQuarter(FileConfiguration config, double balance) {
        double amount = balance / 4;
        return ItemBuilder.builder()
                .setMaterial(Material.DROPPER)
                .setDisplayName(configManager.getFormattedString(config, "withdraw-menu-quarter"))
                .setLore(getLore(config, balance, amount))
                .addPersistentData(key, String.valueOf(amount))
                .build();
    }
    private ItemStack getWithdrawCustom(FileConfiguration config, double balance) {
        return ItemBuilder.builder()
                .setMaterial(Material.OAK_SIGN)
                .setDisplayName(configManager.getFormattedString(config, "withdraw-menu-custom"))
                .setLore(getLore(config, balance, 0))
                .addPersistentData(key, "custom")
                .build();
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
