package ximronno.diore.impl;

import org.bukkit.configuration.file.FileConfiguration;
import ximronno.diore.Diore;

public enum Languages {


    ENGLISH {
        @Override
        public FileConfiguration getCFG() {
            return Diore.getInstance().getConfigManager().getLanguageConfig("eng");
        }
    },
    RUSSIAN {
        @Override
        public FileConfiguration getCFG() {
            return Diore.getInstance().getConfigManager().getLanguageConfig("rus");
        }
    };


    public abstract FileConfiguration getCFG();


}
