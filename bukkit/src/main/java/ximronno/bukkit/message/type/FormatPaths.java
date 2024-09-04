package ximronno.bukkit.message.type;

import ximronno.diore.api.polyglot.Path;

public enum FormatPaths implements Path {

    AMOUNT_FORMAT,
    NEGATIVE_AMOUNT_FORMAT,
    SECONDS_FORMAT,
    MINUTES_FORMAT,
    HOURS_FORMAT,
    DAYS_FORMAT,
    DATE_FORMAT,
    PRIVATE_FORMAT,
    PUBLIC_FORMAT,;

    private final String path;

    FormatPaths() {
        path = "format." + name().toLowerCase();
    }

    @Override
    public String path() {
        return path;
    }

}
