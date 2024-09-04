package ximronno.bukkit.config;

import org.bukkit.configuration.file.FileConfiguration;
import ximronno.bukkit.message.type.DirectoriesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.config.ConfigLoader;
import ximronno.diore.api.config.ConfigSaver;
import ximronno.diore.api.polyglot.Path;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class DioreConfigSaver implements ConfigSaver {

    private final ConfigLoader loader;

    private final HashMap<File, FileConfiguration> configsToSave = new HashMap<>();

    private Logger logger;

    public DioreConfigSaver(DioreAPI api, Logger logger) {
        this.loader = api.getConfigLoader();

        if(api.getMainConfig().useLogger()) {
            this.logger = logger;
        }
    }

    @Override
    public void saveMissingPath(Path path, Locale locale) {

        File file = loader.getFile(Path.of(DirectoriesPaths.MISSING_MESSAGES.path() + locale.toString() + ".yml"));

        FileConfiguration config;
        if(configsToSave.containsKey(file)) {
            config = configsToSave.get(file);
        }
        else {
            config = loader.getFileConfiguration(file);
        }

        if(saveToConfig(config, path, "")) {

            configsToSave.put(file, config);

        }

    }

    @Override
    public void saveMissingListPath(Path path, Locale locale) {

        File file = loader.getFile(Path.of(DirectoriesPaths.MISSING_MESSAGES.path() + locale.toString() + ".yml"));

        FileConfiguration config;
        if(configsToSave.containsKey(file)) {
            config = configsToSave.get(file);
        }
        else {
            config = loader.getFileConfiguration(file);
        }

        if(saveToConfig(config, path, List.of(""))) {

            configsToSave.put(file, config);

        }
    }

    @Override
    public boolean saveToConfig(FileConfiguration config, Path path, Object value) {
        return saveToConfig(config, path.path(), value);
    }

    private boolean saveToConfig(FileConfiguration config, String path, Object value) {
        if(!config.contains(path)) {
            config.set(path, value);
            return true;
        }
        return false;
    }

    @Override
    public void saveConfig(FileConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            if(logger != null) {
                logger.severe("Failed to save config" + config.getName() + " to " + file.getAbsolutePath());
            }
        }
    }

    @Override
    public void saveAllStoredConfigs() {

        for(File file : configsToSave.keySet()) {
            saveConfig(configsToSave.get(file), file);
        }

    }

}
