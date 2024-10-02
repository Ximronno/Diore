package ximronno.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

public class OnPlayerDeath implements Listener {

    private final DioreAPI api;

    public OnPlayerDeath(DioreAPI api) {
        this.api = api;
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e) {

        if(!api.getMainConfig().stealOnKill()) {
            return;
        }

        Player killed = e.getEntity();
        Player killer = e.getEntity().getKiller();

        if(killer == null) {
            return;
        }

        steal(killed, killer);
    }

    private void steal(Player killed, Player killer) {
        Account killedAcc = api.getAccount(killed.getUniqueId());
        if(killedAcc == null) {
            return;
        }
        Account killerAcc = api.getAccount(killer.getUniqueId());
        if(killerAcc == null) {
            return;
        }

        api.getAccountController().steal(killer, killed, killerAcc, killedAcc, killerAcc.getLocale(), killedAcc.getLocale());
    }

}
