package ximronno.bukkit.message.type;

import ximronno.diore.api.polyglot.Path;

public enum AccountConfigPaths implements Path {

    UUID,
    LOCALE,
    BALANCE,
    PRIVATE_BALANCE,
    RECENT_TRANSACTIONS,;

    private final String path;
    AccountConfigPaths() {
        path = "account." + name().toLowerCase();
    }

    @Override
    public String path() {
        return path;
    }
}
