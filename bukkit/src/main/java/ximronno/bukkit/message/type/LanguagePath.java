package ximronno.bukkit.message.type;

import ximronno.diore.api.polyglot.Path;

public enum LanguagePath implements Path {


    NAME,
    FLAG_URL;

    private final String path;

    LanguagePath() {
        this.path = "language." + name().toLowerCase();
    }

    @Override
    public String path() {
        return path;
    }



}
