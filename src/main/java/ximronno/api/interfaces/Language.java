package ximronno.api.interfaces;

import org.bukkit.configuration.file.FileConfiguration;

import java.net.URL;

public interface Language {

    FileConfiguration getConfig();
    URL getTextureURL();
    String getName();
    String getRawName();
    void reloadConfig();

}
