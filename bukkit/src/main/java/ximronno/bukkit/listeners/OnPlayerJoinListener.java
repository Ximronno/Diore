package ximronno.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.account.Account;

import java.util.Locale;

public class OnPlayerJoinListener implements Listener {

    private final DioreAPI api;

    private final JavaPlugin plugin;

    public OnPlayerJoinListener(DioreAPI api, JavaPlugin plugin) {
        this.api = api;
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {

        Player target = e.getPlayer();

        if(!api.getAccountManager().hasAccount(target.getUniqueId())) {
            Account acc = api.getAccountLoader().getAccountFromCFG(target.getUniqueId());
            if(acc == null) {
                acc = api.getAccountManager().createNewAccount(target.getUniqueId());

                if (api.getMainConfig().useLocateClientLocale()) {
                    Account finalAcc = acc;

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Locale targetLocale = new Locale(target.getLocale());

                            if(api.getMessageManager().getMessageProvider().getProvidedLanguages().contains(targetLocale)) {
                                finalAcc.setLocale(targetLocale);
                            }
                        }
                    }.runTaskLater(plugin, 100L);
                }

            }

            api.getAccountManager().addAccount(acc);
        }


    }



}
