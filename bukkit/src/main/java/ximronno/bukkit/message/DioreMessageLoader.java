package ximronno.bukkit.message;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import ximronno.bukkit.message.type.DirectoriesPaths;
import ximronno.bukkit.message.type.LanguagePath;
import ximronno.bukkit.message.type.YmlPaths;
import ximronno.bukkit.message.type.menu.MenuItemNamesPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.message.MessageLoader;
import ximronno.diore.api.polyglot.LangLists;
import ximronno.diore.api.polyglot.LangMessages;
import ximronno.diore.api.polyglot.Path;
import ximronno.diore.api.polyglot.config.PolyglotConfig;

import java.io.File;
import java.net.URL;
import java.util.*;

public class DioreMessageLoader implements MessageLoader {

    private final DioreAPI api;

    public DioreMessageLoader(DioreAPI api) {
        this.api = api;
    }

    @Override
    public PolyglotConfig generatePolyglotConfig() {
        File file = api.getConfigLoader().getFile(DirectoriesPaths.MESSAGES);
        if (!file.exists()) return null;

        File[] files = file.listFiles();
        if (files == null) return null;

        HashMap<Locale, LangMessages> messages = new HashMap<>();

        HashMap<Locale, LangLists> lists = new HashMap<>();

        for (File f : files) {

            Locale fLocale = new Locale(f.getName().replace(".yml", ""));

            FileConfiguration config = null;

            String filePath = f.getPath().replace("plugins/Diore/", "");

            for(YmlPaths ymlPath : YmlPaths.values()) {

                if(filePath.equals(ymlPath.path())) {

                    config = api.getConfigLoader().getWithDefConfiguration(ymlPath);
                    config.options().copyDefaults(true);
                    break;

                }

            }
            if(config == null) {

                config = api.getConfigLoader().getFileConfiguration(f);

            }

            String url = null;

            HashMap<Path, String> replacements = new HashMap<>();

            HashMap<Path, List<String>> listReplacements = new HashMap<>();

            for (String strPath : config.getKeys(true)) {

                for(Path path : getReplacement(Path.of(strPath), config, new HashSet<>())) {

                    if(path.equals(LanguagePath.FLAG_URL)) {
                        url = config.getString(path.path());
                    }
                    else if(config.isList(path.path())) {
                        listReplacements.put(path, config.getStringList(path.path()));
                    }
                    else {
                        replacements.put(path, config.getString(path.path()));
                    }

                }

            }

            messages.put(fLocale, new LangMessages(fLocale, replacements, getURL(url)));

            lists.put(fLocale, new LangLists(fLocale, listReplacements));

        }

        return PolyglotConfig.builder()
                .defaultLanguage(api.getMainConfig().getDefaultLanguage())
                .providedLanguages(messages.keySet())
                .messageDirectory(DirectoriesPaths.MESSAGES)
                .messageFileName("{language}.yml")
                .messages(messages)
                .lists(lists)
                .build();
    }

    private Set<Path> getReplacement(Path currentPath, FileConfiguration configuration, Set<Path> paths) {
        if(configuration.isConfigurationSection(currentPath.path())) {

            ConfigurationSection section = configuration.getConfigurationSection(currentPath.path());
            if(section == null) return paths;

            for(String key : section.getKeys(false)) {

                paths.add(Path.of(currentPath.path() + "." + key));

                getReplacement(Path.of(currentPath.path() + "." + key), configuration, paths);

            }

        }
        else {

            paths.add(Path.of(currentPath.path()));

        }
        return paths;
    }



    private URL getURL(String url) {
        URL urlObject = null;
        try {
            urlObject = new URL(url);
        } catch (Exception ignored) {
        }
        return urlObject;
    }

}
