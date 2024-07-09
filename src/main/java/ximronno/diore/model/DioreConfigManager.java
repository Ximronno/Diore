package ximronno.diore.model;

import org.bukkit.plugin.java.JavaPlugin;
import ximronno.api.ConfigManager;
import ximronno.api.interfaces.Language;
import ximronno.diore.impl.Languages;


public class DioreConfigManager extends ConfigManager {


    public DioreConfigManager(JavaPlugin plugin) {
        super(plugin);
    }
    public void loadLanguageCFGS() {

        for(Language language : Languages.values()) {
            language.reloadConfig();
        }

    }


}
