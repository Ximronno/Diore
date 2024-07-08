package ximronno.diore.guis.menus.transactions;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
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
import ximronno.diore.items.Items;
import ximronno.diore.utils.AccountUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DepositAmountMenu extends DioreMenu {
    public DepositAmountMenu(NamespacedKey key) {
        super(key);
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public String getTitle() {
        return configManager.getFormattedString("deposit-menu-title");
    }

    @Override
    public void handleMenu(Player p, Account acc, Language language, PersistentDataContainer container) {

        if(container.has(key, PersistentDataType.STRING)) {

            String func = container.get(key, PersistentDataType.STRING);
            if(func == null) return;


            switch(func) {
                case "back":
                    new BalanceMenu(key).open(p);
                    break;
                case "custom":
                    FileConfiguration config = language.getConfig();

                    List<String> lines = new ArrayList<>();

                    config.getStringList("deposit-menu-sign")
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

                                    double amount = Math.floor(Double.parseDouble(line0)) ;
                                    new BalanceMenu(key).open(p);
                                    AccountUtils.tryDeposit(p, acc, config, amount);
                                    return Collections.emptyList();

                                }

                            })
                            .callHandlerSynchronously(plugin)
                            .build();

                    gui.open(p);
                    break;
                default:
                    double amount = Double.parseDouble(func);
                    AccountUtils.tryDeposit(p, acc, language.getConfig(), amount);
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
                case 10:
                    inventory.setItem(i, getDepositAll(p, language.getConfig(), acc.getBalance()));
                    break;
                case 12:
                    inventory.setItem(i, getDepositHalf(p, language.getConfig(), acc.getBalance()));
                    break;
                case 14:
                    inventory.setItem(i, getDepositQuarter(p, language.getConfig(), acc.getBalance()));
                    break;
                case 16:
                    inventory.setItem(i, getDepositCustom(language.getConfig(), acc.getBalance()));
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
    private ItemStack getDepositAll(Player p, FileConfiguration config, double balance) {
        double amount = getDiamondOres(p);

        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.CHEST)
                .setAmount(64)
                .setDisplayName(configManager.getFormattedString(config, "deposit-menu-all"))
                .setLore(getLore(config, balance, amount))
                .build();

        ItemBuilder.addPersistentData(item, key, String.valueOf(amount));

        return item;
    }
    private ItemStack getDepositHalf(Player p, FileConfiguration config, double balance) {
        double amount = Math.floor(getDiamondOres(p) / 2);

        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.CHEST)
                .setAmount(32)
                .setDisplayName(configManager.getFormattedString(config, "deposit-menu-half"))
                .setLore(getLore(config, balance, amount))
                .build();

        ItemBuilder.addPersistentData(item, key, String.valueOf(amount));

        return item;
    }
    private ItemStack getDepositQuarter(Player p, FileConfiguration config, double balance) {
        double amount = Math.floor(getDiamondOres(p) / 4);

        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.CHEST)
                .setDisplayName(configManager.getFormattedString(config, "deposit-menu-quarter"))
                .setLore(getLore(config, balance, amount))
                .build();

        ItemBuilder.addPersistentData(item, key, String.valueOf(amount));

        return item;
    }
    private ItemStack getDepositCustom(FileConfiguration config, double balance) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.OAK_SIGN)
                .setDisplayName(configManager.getFormattedString(config, "deposit-menu-custom"))
                .setLore(getLore(config, balance, 0))
                .build();

        ItemBuilder.addPersistentData(item, key, "custom");

        return item;
    }
    private double getDiamondOres(Player p) {

        int diamondOreCount = 0;
        int diamondOreNuggetCount = 0;

        for (ItemStack items : p.getInventory().getContents()) {
            if (items != null) {
                if (items.getType() == Material.DIAMOND_ORE) {
                    diamondOreCount += items.getAmount();
                } else if (items.getType() == Material.DEEPSLATE_DIAMOND_ORE) {
                    diamondOreCount += items.getAmount();
                } else if (items.getItemMeta().getPersistentDataContainer().has(Items.getDioreItemsKey(), PersistentDataType.STRING) && items.getItemMeta().getPersistentDataContainer().get(Items.getDioreItemsKey(), PersistentDataType.STRING).equals("diore_nugget")) {
                    diamondOreNuggetCount += items.getAmount();
                }
            }
        }

        return (diamondOreCount + ((double) diamondOreNuggetCount / 10));

    }
    private List<String> getLore(FileConfiguration config, double balance, double amount) {

        return configManager.getFormattedList(config, "deposit-menu-lore-format",
                Map.of("<balance>", accountManager.formatBalance(balance),
                        "<amount>", accountManager.formatBalance(amount)));
    }

}
