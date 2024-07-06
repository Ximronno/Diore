package ximronno.diore.guis.menus.transactions;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.guis.menus.BalanceMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.utils.AccountUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public void handleMenu(InventoryClickEvent e) {

        ItemStack item = e.getCurrentItem();
        if(item == null) return;

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;

        Player p = (Player) e.getWhoClicked();

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Languages language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        PersistentDataContainer container = meta.getPersistentDataContainer();

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

                                    return Collections.EMPTY_LIST;

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
        ItemStack item = new ItemStack(Material.DROPPER, 64);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("withdraw-menu-all", config));

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, String.valueOf(balance));

        meta.setLore(getLore(config, balance, balance));

        item.setItemMeta(meta);

        return item;
    }
    private ItemStack getWithdrawHalf(FileConfiguration config, double balance) {
        ItemStack item = new ItemStack(Material.DROPPER, 32);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("withdraw-menu-half", config));

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, String.valueOf(balance / 2));

        meta.setLore(getLore(config, balance, balance / 2));

        item.setItemMeta(meta);

        return item;
    }
    private ItemStack getWithdrawQuarter(FileConfiguration config, double balance) {
        ItemStack item = new ItemStack(Material.DROPPER);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("withdraw-menu-quarter", config));

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, String.valueOf(balance / 4));

        meta.setLore(getLore(config, balance, balance / 4));

        item.setItemMeta(meta);

        return item;
    }
    private ItemStack getWithdrawCustom(FileConfiguration config, double balance) {
        ItemStack item = new ItemStack(Material.OAK_SIGN);

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(configManager.getFormattedString("withdraw-menu-custom", config));

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "custom");

        meta.setLore(getLore(config, balance, 0));

        item.setItemMeta(meta);

        return item;
    }
    private List<String> getLore(FileConfiguration config, double balance, double amount) {

        List<String> lore = new ArrayList<>();

        if (target == null) {
            config.getStringList("withdraw-menu-lore-format")
                    .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)
                            .replace("<balance>", accountManager.formatBalance(balance))
                            .replace("<amount>", accountManager.formatBalance(amount))));
        }
        else {
            config.getStringList("transfer-menu-lore-format")
                    .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)
                            .replace("<balance>", accountManager.formatBalance(balance))
                            .replace("<amount>", accountManager.formatBalance(amount))));
        }

        return lore;
    }
    private ItemStack getPlayerSkull(Player p, FileConfiguration config) {

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta playerMeta = (SkullMeta) skull.getItemMeta();
        if(playerMeta == null) return null;

        playerMeta.setDisplayName(configManager.getFormattedString("transfer-menu-sending-to", config)
                .replace("<player>", p.getDisplayName()));

        playerMeta.setOwningPlayer(p);

        skull.setItemMeta(playerMeta);

        return skull;
    }
}
