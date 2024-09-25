package ximronno.bukkit.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ximronno.bukkit.message.type.LanguagePath;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.SortingVariant;
import ximronno.diore.api.account.Account;

import java.util.Locale;

public class PlaceholderHook extends PlaceholderExpansion implements Relational {

    private final DioreAPI api;

    public PlaceholderHook(DioreAPI api) {
        this.api = api;
        this.register();
    }

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
        return "0.3.7";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if(player == null) return null;
        Account acc = api.getAccount(player.getUniqueId());
        if(acc == null) return null;
        Locale locale = acc.getLocale();
        return switch (params.toLowerCase()) {
            case "balance" -> api.getAccountInfoFormatter().getFormattedBalance(acc, locale);
            case "language" -> api.getMessageManager().getMessage(LanguagePath.NAME, locale, true);
            case "language_raw" -> locale.toString();
            case "place" -> String.valueOf(api.getAccountLeaderBoard().getPlace(acc, SortingVariant.NO_SORT));
            case "balance_status" -> api.getAccountInfoFormatter().getFormattedBalanceStatus(acc, locale);
            default -> null;
        };
    }

    @Override
    public String onPlaceholderRequest(Player one, Player two, String identifier) {
        if(one == null || two == null) {
            return null;
        }

        Account oneAcc = api.getAccount(one.getUniqueId());
        Account twoAcc = api.getAccount(two.getUniqueId());
        if(oneAcc == null || twoAcc == null) {
            return null;
        }

        Locale oneLocale = oneAcc.getLocale();

        if(identifier.equalsIgnoreCase("balance")) {

            return api.getAccountInfoFormatter().getFormattedBalance(oneAcc, twoAcc, oneLocale);

        }

        return null;
    }
}
