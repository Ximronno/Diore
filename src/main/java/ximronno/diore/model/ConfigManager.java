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
    public String getFormattedString(String path) {
        String string = plugin.getConfig().getString(path);
        if(string == null) {
            string = "&c" + path;
            plugin.getLogger().severe("Text not found: " + string + " in config.yml");
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public String getFormattedString(String path, FileConfiguration config) {
        String string = config.getString(path);
        if(string == null) {
            string = "&c" + path;
            plugin.getLogger().severe( "Text not found: " + path + " in custom config");
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }





}
