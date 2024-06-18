package ximronno.diore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import ximronno.diore.api.events.DepositEvent;
import ximronno.diore.api.events.TransferEvent;
import ximronno.diore.api.events.WithdrawEvent;
import ximronno.diore.items.Items;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionsListener implements Listener {

    @EventHandler
    private void onTransfer(TransferEvent e) {

        Player p = Bukkit.getPlayer(e.getFrom().getOwner());
        Player p2 = Bukkit.getPlayer(e.getTo().getOwner());

        p.sendMessage("You have sent " + e.getAmount() + " to " + p.getName());
        p2.sendMessage("You have received " + e.getAmount() + " from " + p2.getName());


    }
    @EventHandler
    private void onWithdraw(WithdrawEvent e) {

        Player p = Bukkit.getPlayer(e.getAccount().getOwner());

        if(p == null) return;

        ArrayList<ItemStack> items = new ArrayList<>();

        double amount = e.getAmount();
        int nuggets = (int) ((amount * 10) % 10);
        int ores = (int) amount;

        items.add(Items.getDiamondOreNugget(nuggets));
        items.add(new ItemStack(Material.DIAMOND_ORE, ores));

        p.getInventory().addItem(items.toArray(new ItemStack[0]));

        p.sendMessage("Successfully withdrawn " + e.getAmount() + " from your account.");

    }
    @EventHandler
    private void onDeposit(DepositEvent e) {

        Player p = Bukkit.getPlayer(e.getAccount().getOwner());

        if (p == null) return;

        double amount = e.getAmount();
        int nuggets = (int) ((amount * 10) % 10);
        int ores = (int) amount;

        ArrayList<ItemStack> itemsToRemove = new ArrayList<>();

        if (nuggets > 0) {

            ItemStack nuggetStack = Items.getDiamondOreNugget(nuggets);
            itemsToRemove.add(nuggetStack);

        }

        int diamondOreCount = 0;
        int deepslateDiamondOreCount = 0;
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == Material.DIAMOND_ORE) {
                    diamondOreCount += item.getAmount();
                } else if (item.getType() == Material.DEEPSLATE_DIAMOND_ORE) {
                    deepslateDiamondOreCount += item.getAmount();
                }
            }
        }

        if (diamondOreCount + deepslateDiamondOreCount >= ores) {
            if (diamondOreCount > 0) {

                itemsToRemove.add(new ItemStack(Material.DIAMOND_ORE, Math.min(diamondOreCount, ores)));
                ores -= Math.min(diamondOreCount, ores);

            }
            if (ores > 0 && deepslateDiamondOreCount > 0) {

                itemsToRemove.add(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, ores));

            }
        } else {

            e.setCancelled(true);
            p.sendMessage("You don't have enough diamond ore and/or deepslate diamond ore in your inventory to deposit that much.");
            return;

        }

        ItemStack[] itemsArray = itemsToRemove.toArray(new ItemStack[0]);

        HashMap<Integer, ItemStack> remainingItems = p.getInventory().removeItem(itemsArray);

        if (remainingItems.isEmpty()) {

            p.sendMessage("You successfully deposited " + amount + " to your account.");

        } else {

            e.setCancelled(true);
            p.sendMessage("You don't have enough diamond ore and/or nuggets in your inventory to deposit that much.");
            p.getInventory().addItem(itemsArray);

        }
    }


}
