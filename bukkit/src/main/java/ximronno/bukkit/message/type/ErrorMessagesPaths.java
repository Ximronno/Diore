package ximronno.bukkit.message.type;

import ximronno.diore.api.polyglot.Path;

public enum ErrorMessagesPaths implements Path {


    SENDER_NO_ACCOUNT,
    TARGET_NO_ACCOUNT,
    LOCALE_NOT_FOUND,
    PLAYER_NOT_FOUND,
    SENDER_EQUALS_TARGET,
    UNKNOWN_ERROR;

    private final String path;

    ErrorMessagesPaths() {
        this.path = "error." + name().toLowerCase();
    }

    @Override
    public String path() {
        return path;
    }
}
