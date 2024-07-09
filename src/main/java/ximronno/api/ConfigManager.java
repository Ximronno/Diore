package ximronno.api;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigManager {

    protected final JavaPlugin plugin;
    protected final Logger logger;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    public File getFile(String path) {
        File file = new File(plugin.getDataFolder(), "/" + path);

        if(!file.exists()) {
            plugin.saveResource(path, true);
        }

        return file;
    }
    public FileConfiguration getConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
    public String getFormattedString(String path) {
        return getFormattedString(plugin.getConfig(), path);
    }
    public String getFormattedString(String path, Map<String, String> replacements) {
        return getFormattedString(plugin.getConfig(), path, replacements);
    }
    public String getFormattedString(FileConfiguration config, String path) {
        String string = config.getString(path);
        if(string == null) {
            string = "&c" + path;
            logger.severe("§cText not found: " + string);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public String getFormattedString(FileConfiguration config, String path, Map<String, String> replacements) {
        return replaceString(getFormattedString(config, path), replacements);
    }
    public List<String> getFormattedList(String path) {
        return formatList(plugin.getConfig().getStringList(path), null);
    }
    public List<String> getFormattedList(String path, Map<String, String> replacements) {
        return formatList(plugin.getConfig().getStringList(path), replacements);
    }
    public List<String> getFormattedList(FileConfiguration config, String path) {
        return formatList(config.getStringList(path), null);
    }
    public List<String> getFormattedList(FileConfiguration config, String path, Map<String, String> replacements) {
        return formatList(config.getStringList(path), replacements);
    }
    public List<String> formatList(List<String> lines, Map<String, String> replacements) {
        return lines.stream()
                .map(line -> replaceString(ChatColor.translateAlternateColorCodes('&', line), replacements))
                .toList();
    }
    public String replaceString(String line, Map<String, String> replacements) {
        if (replacements != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                line = line.replace(entry.getKey(), entry.getValue());
            }
        }
        return line;
    }

}

