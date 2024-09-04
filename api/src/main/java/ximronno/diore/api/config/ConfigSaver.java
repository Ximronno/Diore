package ximronno.diore.api.config;

import org.bukkit.configuration.file.FileConfiguration;
import ximronno.diore.api.polyglot.Path;

import java.io.File;
import java.util.Locale;

public interface ConfigSaver {

    void saveMissingPath(Path path, Locale locale);

    void saveMissingListPath(Path path, Locale locale);

    boolean saveToConfig(FileConfiguration config, Path path, Object value);

    void saveConfig(FileConfiguration config, File file);

    void saveAllStoredConfigs();

}
