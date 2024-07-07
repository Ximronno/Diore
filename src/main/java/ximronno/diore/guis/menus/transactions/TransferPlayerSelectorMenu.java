package ximronno.diore.guis.menus.transactions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.diore.guis.DiorePaginatedMenu;
import ximronno.diore.guis.menus.BalanceMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.impl.TopBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class TransferPlayerSelectorMenu extends DiorePaginatedMenu {
    public TransferPlayerSelectorMenu(NamespacedKey key) {
        super(key);
    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public String getTitle() {
        return configManager.getFormattedString("transfer-player-selector-menu-title");
    }
    @Override
    public void handleMenu(Player p, Account acc, Languages language, PersistentDataContainer container) {

        if(container.has(key, PersistentDataType.STRING)) {

            ArrayList<Player> players = new ArrayList<>(getServer().getOnlinePlayers());

            String func = container.get(key, PersistentDataType.STRING);
            if(func == null) return;

            switch(func) {
                case "left":
                    if(currentPage > 0) {
                        currentPage--;
                        open(p);
                    }
                    break;
                case "right":
                    if (!((index + 1) >= players.size())) {
                        currentPage++;
                        open(p);
                    }
                    break;
                case "back":
                    new BalanceMenu(key).open(p);
                    break;
                default:
                    UUID targetUUID = UUID.fromString(func);

                    Player target = getPlayer(targetUUID);
                    if (target == null) break;

                    Account targetAcc = accountManager.getAccount(targetUUID).orElse(null);
                    if(targetAcc == null) break;

                    new WithdrawAmountMenu(key, targetAcc).open(p);

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

        addMenuBorder(language.getCFG());


        ArrayList<Player> players = new ArrayList<>(getServer().getOnlinePlayers());

        if(!players.isEmpty()) {
            for(int i = 0; i < getMaxItemPerPage(); i++) {
                index = getMaxItemPerPage() * currentPage + i;
                if(index >= players.size()) break;
                if (players.get(index) != null){

                    Player target = players.get(index);

                    if(target.getUniqueId().equals(p.getUniqueId())) continue;

                    ItemStack playerItem = new ItemStack(Material.PLAYER_HEAD);

                    SkullMeta playerMeta = (SkullMeta) playerItem.getItemMeta();
                    if(playerMeta == null) return;

                    playerMeta.setDisplayName(configManager.getFormattedString(language.getCFG(), "transfer-selector-menu-skull")
                            .replace("<player>", target.getName()));

                    List<String> lore = new ArrayList<>();

                    language.getCFG().getStringList("transfer-selector-menu-lore-format")
                            .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)
                                    .replace("<balance>", accountManager.formatBalance(returnBalance(target.getUniqueId())))));


                    playerMeta.setLore(lore);

                    playerMeta.setOwningPlayer(target);

                    playerMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, target.getUniqueId().toString());

                    playerItem.setItemMeta(playerMeta);

                    inventory.addItem(playerItem);

                }
            }
        }

    }
    private double returnBalance(UUID target) {
        double balance = 0;
        for(TopBalance topBalance : accountManager.getTopBalances()) {
            if(topBalance.account().getOwner().equals(target)) {
                balance = topBalance.balance();
            }
        }
        return balance;
    }
}
