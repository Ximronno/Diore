package ximronno.diore.impl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
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
        public ItemStack getItemStack() {
            PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
            PlayerTextures textures = profile.getTextures();
            URL urlObject;
            try {
                urlObject = new URL("https://textures.minecraft.net/texture/4cac9774da1217248532ce147f7831f67a12fdcca1cf0cb4b3848de6bc94b4");
            } catch(Exception e) {
                return null;
            }
            textures.setSkin(urlObject);
            profile.setTextures(textures);

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            if(meta == null) return null;

            meta.setOwnerProfile(profile);

            skull.setItemMeta(meta);

            return skull;
        }
    },
    RUSSIAN {
        @Override
        public FileConfiguration getCFG() {
            return Diore.getInstance().getConfigManager().getLanguageConfig("rus");
        }

        @Override
        public ItemStack getItemStack() {
            PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
            PlayerTextures textures = profile.getTextures();
            URL urlObject;
            try {
                urlObject = new URL("https://textures.minecraft.net/texture/16eafef980d6117dabe8982ac4b4509887e2c4621f6a8fe5c9b735a83d775ad");
            } catch(Exception e) {
                return null;
            }
            textures.setSkin(urlObject);
            profile.setTextures(textures);

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            if(meta == null) return null;

            meta.setOwnerProfile(profile);

            skull.setItemMeta(meta);

            return skull;
        }
    };

    public abstract FileConfiguration getCFG();
    public abstract ItemStack getItemStack();
    public String getName() {
        return Diore.getInstance().getConfigManager().getFormattedString("name", getCFG());
    }
}
