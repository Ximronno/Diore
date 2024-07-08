package ximronno.diore.impl;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ximronno.api.interfaces.Language;
import ximronno.diore.Diore;
import ximronno.diore.model.ConfigManager;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public enum Languages implements Language {


    ENGLISH("eng",
            getURL("https://textures.minecraft.net/texture/4cac9774da1217248532ce147f7831f67a12fdcca1cf0cb4b3848de6bc94b4")),
    SPANISH("spa",
            getURL("https://textures.minecraft.net/texture/32bd4521983309e0ad76c1ee29874287957ec3d96f8d889324da8c887e485ea8")),
    UKRAINIAN("ukr",
            getURL("https://textures.minecraft.net/texture/28b9f52e36aa5c7caaa1e7f26ea97e28f635e8eac9aef74cec97f465f5a6b51")),
    RUSSIAN("rus",
            getURL("https://textures.minecraft.net/texture/16eafef980d6117dabe8982ac4b4509887e2c4621f6a8fe5c9b735a83d775ad")),;

    FileConfiguration config;
    String unicode;
    File configFile;
    URL textureURL;
    Languages(String unicode, URL textureURL) {
        ConfigManager configManager = Diore.getInstance().getConfigManager();
        this.unicode = unicode;
        this.configFile = configManager.getFile("languages/" + this.unicode + ".yml");
        this.config = configManager.getConfig(configFile);
        this.textureURL = textureURL;
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public URL getTextureURL() {
        return textureURL;
    }

    @Override
    public String getName() {
        return Diore.getInstance().getConfigManager().getFormattedString(config, "name");
    }

    @Override
    public String getRawName() {
        return name();
    }
    @Override
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        InputStream defConfigStream = Diore.getInstance().getResource("languages/" + this.unicode + ".yml");
        if(defConfigStream != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        }
    }
    private static URL getURL(String url) {
        try {
            return new URL(url);
        } catch(Exception e) {
            return null;
        }
    }

}
