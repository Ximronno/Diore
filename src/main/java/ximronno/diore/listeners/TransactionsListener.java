package ximronno.diore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import ximronno.diore.Diore;
import ximronno.diore.api.events.DepositEvent;
import ximronno.diore.api.events.TransferEvent;
import ximronno.diore.api.events.WithdrawEvent;
import ximronno.diore.api.interfaces.Account;
import ximronno.diore.items.Items;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionsListener implements Listener {

    private final Diore plugin;

    public TransactionsListener(Diore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onTransfer(TransferEvent e) {

        Account from = e.getFrom();
        Account to = e.getTo();

        Player p = Bukkit.getPlayer(from.getOwner());
        Player p2 = Bukkit.getPlayer(to.getOwner());

        String pMessage = plugin.getConfigManager().getFormattedString("sent-amount-to-player", from.getLanguage().getCFG())
                .replace("<amount>", plugin.getAccountManager().formatBalance(e.getAmount()))
                .replace("<player>", p2.getDisplayName());
        String p2Message = plugin.getConfigManager().getFormattedString("received-amount-from-player", to.getLanguage().getCFG())
                .replace("<amount>", plugin.getAccountManager().formatBalance(e.getAmount()))
                .replace("<player>", p.getDisplayName());;

        p.sendMessage(pMessage);
        p2.sendMessage(p2Message);


    }
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onWithdraw(WithdrawEvent e) {

        Account acc = e.getAccount();

        Player p = Bukkit.getPlayer(acc.getOwner());

        if(p == null) return;

        ArrayList<ItemStack> items = new ArrayList<>();

        double amount = e.getAmount();
        int nuggets = (int) ((amount * 10) % 10);
        int ores = (int) amount;

        items.add(Items.getDiamondOreNugget(nuggets));
        items.add(new ItemStack(Material.DIAMOND_ORE, ores));

        p.getInventory().addItem(items.toArray(new ItemStack[0]));

        String message = plugin.getConfigManager().getFormattedString("on-withdraw", acc.getLanguage().getCFG())
                        .replace("<amount>", plugin.getAccountManager().formatBalance(e.getAmount()));

        p.sendMessage(message);

    }
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDeposit(DepositEvent e) {

        Account acc = e.getAccount();
        Player p = Bukkit.getPlayer(acc.getOwner());

        if (p == null) return;

        double amount = e.getAmount();
        int nuggets = (int) ((amount * 10) % 10);
        int ores = (int) amount;

        ArrayList<ItemStack> itemsToRemove = new ArrayList<>();

        int diamondOreCount = 0;
        int deepslateDiamondOreCount = 0;
        int diamondOreNuggetCount = 0;

        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == Material.DIAMOND_ORE) {
                    diamondOreCount += item.getAmount();
                } else if (item.getType() == Material.DEEPSLATE_DIAMOND_ORE) {
                    deepslateDiamondOreCount += item.getAmount();
                } else if (item.getType() == Items.getDiamondOreNugget(1).getType()) {
                    diamondOreNuggetCount += item.getAmount();
                }
            }
        }

        if (amount < 1) {
            if (diamondOreNuggetCount < nuggets) {
                e.setCancelled(true);
                p.sendMessage(plugin.getConfigManager().getFormattedString("not-enough-ores", acc.getLanguage().getCFG()));
                return;
            }
            ItemStack nuggetStack = Items.getDiamondOreNugget(nuggets);
            itemsToRemove.add(nuggetStack);
        } else {
            int totalDiamondOreCount = diamondOreCount + (diamondOreNuggetCount / 10);

            if (totalDiamondOreCount + deepslateDiamondOreCount < ores || (totalDiamondOreCount + deepslateDiamondOreCount == 0 && nuggets > 0)) {
                e.setCancelled(true);
                p.sendMessage(plugin.getConfigManager().getFormattedString("not-enough-ores", acc.getLanguage().getCFG()));
                return;
            }

            if (nuggets > 0) {
                ItemStack nuggetStack = Items.getDiamondOreNugget(nuggets);
                itemsToRemove.add(nuggetStack);
            }

            if (diamondOreCount > 0) {
                itemsToRemove.add(new ItemStack(Material.DIAMOND_ORE, Math.min(diamondOreCount, ores)));
                ores -= Math.min(diamondOreCount, ores);
            }

            if (ores > 0 && deepslateDiamondOreCount > 0) {
                itemsToRemove.add(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, Math.min(deepslateDiamondOreCount, ores)));
                ores -= Math.min(deepslateDiamondOreCount, ores);
            }

            if (ores > 0 && diamondOreNuggetCount > 0) {
                int nuggetsNeeded = ores * 10;
                itemsToRemove.add(Items.getDiamondOreNugget(Math.min(diamondOreNuggetCount, nuggetsNeeded)));
            }
        }

        ItemStack[] itemsArray = itemsToRemove.toArray(new ItemStack[0]);
        HashMap<Integer, ItemStack> remainingItems = p.getInventory().removeItem(itemsArray);

        if (remainingItems.isEmpty()) {
            p.sendMessage(plugin.getConfigManager().getFormattedString("on-deposit", acc.getLanguage().getCFG())
                    .replace("<amount>", plugin.getAccountManager().formatBalance(e.getAmount())));
        } else {
            e.setCancelled(true);
            p.sendMessage(plugin.getConfigManager().getFormattedString("not-enough-ores", acc.getLanguage().getCFG()));
            p.getInventory().addItem(itemsArray);
        }

    }
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        if(e.getItemInHand().equals(Items.getDiamondOreNugget(e.getItemInHand().getAmount()))) {
            e.setCancelled(true);
        }
    }

}
