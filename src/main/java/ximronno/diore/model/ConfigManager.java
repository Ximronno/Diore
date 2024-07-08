package ximronno.diore.model;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ximronno.api.interfaces.Language;
import ximronno.diore.Diore;
import ximronno.diore.impl.Languages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    private final Diore plugin;

    public ConfigManager(Diore plugin) {
        this.plugin = plugin;
    }
    public void loadLanguageCFGS() {

        for(Language language : Languages.values()) {
            language.reloadConfig();
        }

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
        String string = plugin.getConfig().getString(path);
        if(string == null) {
            string = "&c" + path;
            plugin.getLogger().severe("§cText not found: " + string + " in config.yml");
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public String getFormattedString(FileConfiguration config, String path) {
        String string = config.getString(path);
        if(string == null) {
            string = "&c" + path;
            plugin.getLogger().severe( "§cText not found: " + path + " in custom config");
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public List<String> getFormattedList(String path) {
        List<String> lore = new ArrayList<>();

        plugin.getConfig().getStringList(path)
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        return lore;
    }
    public List<String> getFormattedList(String path, Map<String, String> replacements) {
        List<String> lore = new ArrayList<>();

        plugin.getConfig().getStringList(path)
                .forEach(loreLine -> {
            String processedLine = ChatColor.translateAlternateColorCodes('&', loreLine);

            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                processedLine = processedLine.replace(entry.getKey(), entry.getValue());
            }

            lore.add(processedLine);
        });

        return lore;
    }
    public List<String> getFormattedList(FileConfiguration config, String path) {
        List<String> lore = new ArrayList<>();

        config.getStringList(path)
                .forEach(loreLine -> lore.add(ChatColor.translateAlternateColorCodes('&', loreLine)));

        return lore;
    }
    public List<String> getFormattedList(FileConfiguration config, String path, Map<String, String> replacements) {
        List<String> lore = new ArrayList<>();

        config.getStringList(path)
                .forEach(loreLine -> {
            String processedLine = ChatColor.translateAlternateColorCodes('&', loreLine);

            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                processedLine = processedLine.replace(entry.getKey(), entry.getValue());
            }

            lore.add(processedLine);
        });

        return lore;
    }





}
