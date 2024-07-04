package ximronno.diore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ximronno.diore.Diore;
import ximronno.api.interfaces.Account;

public class PlayerJoinListener implements Listener {


    private final Diore plugin;

    public PlayerJoinListener(Diore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {

        final Player p = e.getPlayer();

        final Account pAccount = plugin.getAccountManager().getAccount(p.getUniqueId()).orElse(null);

        if(pAccount == null) {

            Diore.getInstance().getAccountManager().getOrCreateAccount(p);

        }


    }


}
