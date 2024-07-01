package ximronno.diore.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ximronno.diore.Diore;
import ximronno.diore.api.interfaces.Account;
import ximronno.diore.impl.TopBalance;
import ximronno.diore.model.AccountManager;

import java.util.List;

public class PlaceholderHook extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "diore";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Ximronno";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1";
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {

        AccountManager am = Diore.getInstance().getAccountManager();

        if(offlinePlayer != null && offlinePlayer.isOnline()) {
            Player player = (Player) offlinePlayer;

            if(params.equalsIgnoreCase("balance")) {
                Account acc = am.getAccount(player.getUniqueId()).orElse(null);
                if(acc == null || !acc.isPublicBalance()) return null;
                return String.valueOf(am.formatBalance(acc.getBalance()));
            }
            if(params.equalsIgnoreCase("public_balance")) {
                Account acc = am.getAccount(player.getUniqueId()).orElse(null);
                if(acc == null) return null;
                return acc.isPublicBalance() ? "true" : "false";
            }
            if(params.equalsIgnoreCase("language")) {
                Account acc = am.getAccount(player.getUniqueId()).orElse(null);
                if(acc == null) return null;
                return acc.getLanguage().name();
            }
        }

        return null;
    }

}
