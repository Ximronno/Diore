package ximronno.diore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ximronno.diore.Diore;
import ximronno.diore.api.interfaces.Account;

public class PlayerJoinListener implements Listener {


    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {

        final Player p = e.getPlayer();

        final Account pAccount = Diore.getInstance().getAccountManager().getAccount(p.getUniqueId()).orElse(null);

        if(pAccount == null) {

            Diore.getInstance().getAccountManager().getOrCreateAccount(p);

        }


    }


}
