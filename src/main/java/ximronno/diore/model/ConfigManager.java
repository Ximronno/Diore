package ximronno.diore.model;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ximronno.diore.Diore;
import ximronno.diore.impl.Languages;

import java.io.File;

public class ConfigManager {

    private final Diore plugin;

    public ConfigManager(Diore plugin) {
        this.plugin = plugin;
    }
    public void loadLanguageCFGS() {

        for(Languages language : Languages.values()) {
            language.getCFG();
        }

    }
    public FileConfiguration getLanguageConfig(String language) {

        File file = new File(plugin.getDataFolder() + "/languages/", language + ".yml");
        FileConfiguration config = null;

        if(!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("languages/" + language + ".yml", false);
        }

        try {
            config = new YamlConfiguration();
            config.load(file);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cFailed to load" + language + " language file: " + e.getMessage());
        }

        return config;

    }
    public FileConfiguration getMainConfig() {
        return plugin.getConfig();
    }
    public String getFormattedString(String path) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(path));
    }
    public String getFormattedString(String path, FileConfiguration config) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }





}
