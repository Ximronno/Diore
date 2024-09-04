package ximronno.bukkit.message.type;

import ximronno.diore.api.polyglot.Path;

public enum DirectoriesPaths implements Path {

    ACCOUNTS("accounts_data/"),
    MESSAGES("messages/"),
    MISSING_MESSAGES("missing_messages/"),;


    private final String path;

    DirectoriesPaths(String path) {
        this.path = path;
    }

    @Override
    public String path() {
        return path;
    }
}
