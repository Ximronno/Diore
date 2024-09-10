package ximronno.bukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import ximronno.bukkit.Diore;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.DiorePlugin;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.account.AccountResponse;
import ximronno.diore.api.event.DepositEvent;
import ximronno.diore.api.event.WithdrawEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionsListener implements Listener {

    private final DiorePlugin plugin;

    private final DioreAPI api;

    public TransactionsListener(DiorePlugin plugin) {
        this.plugin = plugin;
        this.api = plugin.getAPI();
    }

    @EventHandler
    private void onWithdraw(WithdrawEvent e) {
        Account acc = e.getAccount();
        Player p = Bukkit.getPlayer(acc.getUuid());
        if (p == null) return;

        double amount = e.getAmount();
        int nuggets = (int) ((amount * 10) % 10);
        int ores = (int) amount;

        HashMap<Integer, ItemStack> itemsToDrop;

        if(api.getMainConfig().useDiamonds()) {
            itemsToDrop = addDiamonds(p.getInventory(), ores, nuggets);
        }
        else {
            itemsToDrop = addDiamondsOres(p.getInventory(), ores, nuggets);
        }

        if(!itemsToDrop.isEmpty()) {
            for(ItemStack item : itemsToDrop.values()) {
                while(item.getAmount() > 64) {
                    ItemStack newItem = new ItemStack(item.getType(), 64);
                    item.setAmount(item.getAmount() - 64);
                    p.getWorld().dropItem(p.getLocation(), newItem);
                }
                p.getWorld().dropItem(p.getLocation(), item);
            }

        }

    }

    @EventHandler
    private void onDeposit(DepositEvent e) {
        Account acc = e.getAccount();
        Player p = Bukkit.getPlayer(acc.getUuid());
        if (p == null) return;

        HashMap<Integer, ItemStack> itemsToDrop;
        if(api.getMainConfig().useDiamonds()) {

            int[] diamonds = getDiamonds(p.getInventory());

            int diamondsGems = diamonds[0];
            int diamondNuggets = diamonds[1];

            BigDecimal diamondsAmount = BigDecimal.valueOf(diamondsGems)
                    .add(BigDecimal.valueOf(diamondNuggets).divide(BigDecimal.TEN));

            BigDecimal amount = BigDecimal.valueOf(e.getAmount());

            if (diamondsAmount.compareTo(amount) < 0) {
                e.setResponse(AccountResponse.NOT_ENOUGH_FUNDS);
                e.setCancelled(true);
                return;
            }

            BigDecimal remainingAmount = diamondsAmount.subtract(amount);
            int diamondGemsToAdd = remainingAmount.intValue();

            BigDecimal fractionalPart = remainingAmount.subtract(BigDecimal.valueOf(diamondGemsToAdd));
            int diamondNuggetsToAdd = fractionalPart.multiply(BigDecimal.TEN).intValue();

            removeDiamonds(p.getInventory(), diamondsGems, diamondNuggets);
            itemsToDrop = addDiamonds(p.getInventory(), diamondGemsToAdd, diamondNuggetsToAdd);
        }
        else {

            int[] ores = getDiamondOres(p.getInventory());

            int diamondOres = ores[0];
            int deepslateDiamondOres = ores[1];
            int diamondNuggets = ores[2];

            BigDecimal oresAmount = BigDecimal.valueOf(diamondOres)
                    .add(BigDecimal.valueOf(deepslateDiamondOres)
                            .add(BigDecimal.valueOf(diamondNuggets).divide(BigDecimal.TEN)));

            BigDecimal amount = BigDecimal.valueOf(e.getAmount());

            if (oresAmount.compareTo(amount) < 0) {
                e.setResponse(AccountResponse.NOT_ENOUGH_FUNDS);
                e.setCancelled(true);
                return;
            }

            BigDecimal remainingAmount = oresAmount.subtract(amount);
            int diamondOresToAdd = remainingAmount.intValue();

            BigDecimal fractionalPart = remainingAmount.subtract(BigDecimal.valueOf(diamondOresToAdd));
            int diamondNuggetsToAdd = fractionalPart.multiply(BigDecimal.TEN).intValue();

            removeDiamondsOres(p.getInventory(), diamondOres, deepslateDiamondOres, diamondNuggets);
            itemsToDrop = addDiamondsOres(p.getInventory(), diamondOresToAdd, diamondNuggetsToAdd);
        }

        if(!itemsToDrop.isEmpty()) {

            for(ItemStack item : itemsToDrop.values()) {
                p.getWorld().dropItem(p.getLocation(), item);
            }

        }

    }

    private HashMap<Integer, ItemStack> removeDiamondsOres(Inventory inv, int diamondOres, int deepslateDiamondOres, int diamondNuggets) {
        List<ItemStack> itemsToRemove = new ArrayList<>();
        if (diamondNuggets > 0) itemsToRemove.add(Diore.getDiamondNugget(diamondNuggets));
        if (diamondOres > 0) itemsToRemove.add(new ItemStack(Material.DIAMOND_ORE, diamondOres));
        if (deepslateDiamondOres > 0) itemsToRemove.add(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, deepslateDiamondOres));

        return inv.removeItem(itemsToRemove.toArray(new ItemStack[0]));
    }

    private HashMap<Integer, ItemStack> addDiamondsOres(Inventory inv, int diamondOres, int diamondNuggets) {
        List<ItemStack> itemsToAdd = new ArrayList<>();
        if (diamondNuggets > 0) itemsToAdd.add(Diore.getDiamondNugget(diamondNuggets));
        if (diamondOres > 0) itemsToAdd.add(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, diamondOres));

        return inv.addItem(itemsToAdd.toArray(new ItemStack[0]));
    }

    private HashMap<Integer, ItemStack> removeDiamonds(Inventory inv, int diamonds, int diamondNuggets) {
        List<ItemStack> itemsToAdd = new ArrayList<>();
        if(diamondNuggets > 0) itemsToAdd.add(Diore.getDiamondNugget(diamondNuggets));
        if(diamonds > 0) itemsToAdd.add(new ItemStack(Material.DIAMOND, diamonds));

        return inv.removeItem(itemsToAdd.toArray(new ItemStack[0]));
    }

    private HashMap<Integer, ItemStack> addDiamonds(Inventory inv, int diamonds, int diamondNuggets) {
        List<ItemStack> itemsToAdd = new ArrayList<>();
        if(diamondNuggets > 0) itemsToAdd.add(Diore.getDiamondNugget(diamondNuggets));
        if(diamonds > 0) itemsToAdd.add(new ItemStack(Material.DIAMOND, diamonds));

        return inv.addItem(itemsToAdd.toArray(new ItemStack[0]));
    }

    public static int[] getDiamondOres(Inventory inv) {
        int diamondOres = 0;
        int deepslateDiamondOres = 0;
        int diamondNuggets = 0;

        for (ItemStack item : inv.getContents()) {
            if (item == null) continue;
            switch (item.getType()) {
                case DIAMOND_ORE:
                    diamondOres += item.getAmount();
                    break;
                case DEEPSLATE_DIAMOND_ORE:
                    deepslateDiamondOres += item.getAmount();
                    break;
                default:
                    if(item.equals(Diore.getDiamondNugget(item.getAmount()))) {
                        diamondNuggets += item.getAmount();
                    }
                    break;
            }
        }

        return new int[]{diamondOres, deepslateDiamondOres, diamondNuggets};
    }
    public static int[] getDiamonds(Inventory inv) {
        int diamonds = 0;
        int diamondNuggets = 0;

        for(ItemStack item : inv.getContents()) {
            if(item == null) continue;
            switch (item.getType()) {
                case DIAMOND:
                    diamonds += item.getAmount();
                    break;
                default:
                    if(item.equals(Diore.getDiamondNuggetRaw(item.getAmount()))) {
                        diamondNuggets += item.getAmount();
                    }
                    break;
            }
        }
        return new int[]{diamonds, diamondNuggets};
    }
}
