package ximronno.diore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ximronno.api.interfaces.Account;
import ximronno.diore.Diore;
import ximronno.diore.model.AccountManager;

public class PlayerJoinListener implements Listener {


    private final AccountManager accountManager;

    public PlayerJoinListener(Diore plugin) {
        this.accountManager = plugin.getAccountManager();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {

        final Player p = e.getPlayer();

        final Account pAccount = accountManager.getAccount(p.getUniqueId()).orElse(null);

        if(pAccount == null) {

            accountManager.getOrCreateAccount(p);

        }


    }


}
