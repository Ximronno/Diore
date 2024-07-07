package ximronno.diore.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import ximronno.diore.Diore;

import java.net.URL;
import java.util.UUID;

public enum Languages {


    ENGLISH {
        @Override
        public FileConfiguration getCFG() {
            return Diore.getInstance().getConfigManager().getLanguageConfig("eng");
        }
        @Override
        public URL getTextureURL() {
            URL url;
            try {
                url = new URL("https://textures.minecraft.net/texture/4cac9774da1217248532ce147f7831f67a12fdcca1cf0cb4b3848de6bc94b4");
            } catch(Exception e) {
                return null;
            }
            return url;
        }
    },
    RUSSIAN {
        @Override
        public FileConfiguration getCFG() {
            return Diore.getInstance().getConfigManager().getLanguageConfig("rus");
        }
        @Override
        public URL getTextureURL() {
            URL url;
            try {
                url = new URL("https://textures.minecraft.net/texture/16eafef980d6117dabe8982ac4b4509887e2c4621f6a8fe5c9b735a83d775ad");
            } catch(Exception e) {
                return null;
            }
            return url;
        }
    },
    SPANISH {
        @Override
        public FileConfiguration getCFG() {
            return Diore.getInstance().getConfigManager().getLanguageConfig("spa");
        }

        @Override
        public URL getTextureURL() {
            URL url;
            try {
                url = new URL("https://textures.minecraft.net/texture/32bd4521983309e0ad76c1ee29874287957ec3d96f8d889324da8c887e485ea8");
            } catch(Exception e) {
                return null;
            }
            return url;
        }
    };

    public abstract FileConfiguration getCFG();
    public abstract URL getTextureURL();
    public String getName() {
        return Diore.getInstance().getConfigManager().getFormattedString(getCFG(), "name");
    }
}
