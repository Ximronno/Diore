package ximronno.diore.guis.menus.transactions;

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
import ximronno.diore.guis.DiorePaginatedMenu;
import ximronno.diore.guis.menus.BalanceMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.impl.TopBalance;

import java.util.ArrayList;
import java.util.Map;
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
    public void handleMenu(Player p, Account acc, Language language, PersistentDataContainer container) {

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

        Language language = acc.getLanguage();
        if(language == null) language = Languages.ENGLISH;

        addMenuBorder(language.getConfig());


        ArrayList<Player> players = new ArrayList<>(getServer().getOnlinePlayers());

        if(!players.isEmpty()) {
            for(int i = 0; i < getMaxItemPerPage(); i++) {
                index = getMaxItemPerPage() * currentPage + i;
                if(index >= players.size()) break;
                if (players.get(index) != null){

                    Player target = players.get(index);

                    if(target.getUniqueId().equals(p.getUniqueId())) continue;

                    inventory.addItem(getPlayerSkull(target, language.getConfig()));

                }
            }
        }

    }
    private ItemStack getPlayerSkull(Player target, FileConfiguration config) {
        return ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(configManager.getFormattedString(config, "transfer-selector-menu-skull")
                        .replace("<player>", target.getName()))
                .setLore(configManager.getFormattedList(config, "transfer-selector-menu-lore-format",
                        Map.of("<balance>", accountManager.formatBalance(returnBalance(target.getUniqueId())))))
                .setProfile(target.getPlayerProfile())
                .addPersistentData(key, target.getUniqueId().toString())
                .build();
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
