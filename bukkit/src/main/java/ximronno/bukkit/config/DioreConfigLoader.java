package ximronno.bukkit.config;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ximronno.bukkit.message.type.YmlPaths;
import ximronno.diore.api.config.ConfigLoader;
import ximronno.diore.api.config.MainConfig;
import ximronno.diore.api.polyglot.Path;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DioreConfigLoader implements ConfigLoader {

    private final JavaPlugin plugin;

    public DioreConfigLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public File getFileFromResource(Path path) {
        return getFileFromResource(path.path());
    }

    private File getFileFromResource(String path) {
        File file = new File(plugin.getDataFolder(), path);

        if(!file.exists()) {
            plugin.saveResource(path, false);
        }

        return file;
    }

    @Override
    public File getFile(Path path) {
        return new File(plugin.getDataFolder(), path.path());
    }

    @Override
    public FileConfiguration getFileConfiguration(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public FileConfiguration getWithDefConfiguration(Path path) {
        FileConfiguration config = getFileConfiguration(getFileFromResource(path));
        InputStream defConfigStream = plugin.getResource(path.path());
        if(defConfigStream != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        }
        return config;
    }

    @Override
    public FileConfiguration reloadConfig(Path path) {
        return getWithDefConfiguration(path);
    }

    @Override
    public void loadMessagesConfigs() {

        for(YmlPaths path : YmlPaths.values()) {
            getFileFromResource(path);
        }

    }

    @Override
    public MainConfig loadMainConfig() {
        return new DioreMainConfig(plugin);
    }
}
