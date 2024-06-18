package ximronno.diore.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ximronno.diore.Diore;

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
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if(params.equalsIgnoreCase("total_accounts")) {
            return String.valueOf(Diore.getInstance().getAccountManager().getAccounts().size());
        }



        return null;
    }

}
