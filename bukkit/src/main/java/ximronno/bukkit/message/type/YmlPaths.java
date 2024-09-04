package ximronno.bukkit.message.type;

import ximronno.diore.api.polyglot.Path;

public enum YmlPaths implements Path {

    MESSAGES_EN_US(DirectoriesPaths.MESSAGES,"en_us.yml"),
    MESSAGES_RU(DirectoriesPaths.MESSAGES,"ru_ru.yml"),;

    private final String path;

    YmlPaths(Path path, String strPath) {
        this.path = path.path() + strPath;
    }

    @Override
    public String path() {
        return path;
    }
}
