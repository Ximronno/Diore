package ximronno.diore.api;


import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.polyglot.Path;

import java.util.Locale;
import java.util.UUID;

public interface DiorePlugin {


    String getMessage(Path path, Locale locale, boolean useChatColor);

    DioreAPI getAPI();

    Account getAccount(UUID id);

    JavaPlugin getJavaPlugin();

    NamespacedKey getNamespacedKey();

    boolean usingPlaceholderAPI();

    static NamespacedKey getKey() {
        return DioreKeysProvider.getKey();
    }

    static DiorePlugin getInstance() {
        return DiorePluginProvider.getInstance();
    }

}
