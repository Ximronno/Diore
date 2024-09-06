package ximronno.diore.api.config;

import org.bukkit.configuration.file.FileConfiguration;
import ximronno.diore.api.polyglot.Path;

import java.io.File;
import java.util.UUID;

public interface ConfigLoader {


    File getFileFromResource(Path path);

    File getFile(Path path);

    FileConfiguration getFileConfiguration(File file);

    FileConfiguration getWithDefConfiguration(Path path);

    FileConfiguration reloadConfig(Path path);

    void loadMessagesConfigs();

}
