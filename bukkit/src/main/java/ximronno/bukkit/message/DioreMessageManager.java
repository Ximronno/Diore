package ximronno.bukkit.message;

import org.bukkit.ChatColor;
import ximronno.bukkit.message.type.FormatPaths;
import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.message.MessageManager;
import ximronno.diore.api.message.MessageProvider;
import ximronno.diore.api.polyglot.Path;
import ximronno.diore.api.polyglot.config.PolyglotConfig;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DioreMessageManager implements MessageManager {

    private final MessageProvider messageProvider;

    private final DioreAPI api;

    private Logger logger;

    public DioreMessageManager(PolyglotConfig polyglotConfig, Logger logger, DioreAPI api) {

        messageProvider = new MessageProvider(polyglotConfig);

        this.api = api;

        if(api.getMainConfig().useLogger()) {
            this.logger = logger;
        }
    }

    @Override
    public MessageProvider getMessageProvider() {
        return messageProvider;
    }

    private boolean containsLocale(Locale locale) {
        return locale != null && messageProvider.getProvidedLanguages().contains(locale);
    }

    private boolean localeEqualsDefault(Locale locale) {
        if(locale == null) {
            return true;
        }
        return locale.equals(messageProvider.getDefaultLanguage());
    }

    @Override
    public boolean hasMessage(Path path, Locale locale) {
        if(containsLocale(locale)) {
            String message = messageProvider.getMessages(locale).replace(path);
            return message != null;
        }
        return false;
    }

    @Override
    public boolean hasList(Path path, Locale locale) {
        if(containsLocale(locale)) {
            List<String> list = messageProvider.getLists(locale).replace(path);
            return list != null;
        }
        return false;
    }

    @Override
    public boolean hasDefMessage(Path path) {
        return hasMessage(path, messageProvider.getDefaultLanguage());
    }

    @Override
    public boolean hasDefList(Path path) {
        return hasList(path, messageProvider.getDefaultLanguage());
    }

    @Override
    public String getMessage(Path path, Locale locale, boolean useChatColor) {
        if(localeEqualsDefault(locale)) {
            return getDefaultMessage(path, useChatColor);
        }
        if(!hasMessage(path, locale)) {
            if(logger != null) {
                logger.info("Path for message: '" + path.path() + "' for locale: "+ locale + " is blank, please fix the config!");
            }

            if(api.getMainConfig().saveMissingPaths()) {
                api.getConfigSaver().saveMissingPath(path, locale);
            }

            return getDefaultMessage(path, useChatColor);
        }

        return function(messageProvider.getMessages(locale).replace(path), useChatColor);
    }

    @Override public String getMessage(Path path, Locale locale, boolean useChatColor, Map<String, String> replacements) {
        return replace(getMessage(path, locale, useChatColor), replacements);
    }

    @Override
    public String getDefaultMessage(Path path, boolean useChatColor) {
        if(!hasDefMessage(path)) {
            if(logger != null) {
                logger.info("Path for message: '" + path.path() + "'for default locale is blank, please fix the config!");
            }

            if(api.getMainConfig().saveMissingPaths()) {
                api.getConfigSaver().saveMissingPath(path, messageProvider.getDefaultLanguage());
            }

            if(useChatColor) {
                return addChatColor("&c" + path.path());
            }

            return path.path();
        }

        Locale defLocale = messageProvider.getDefaultLanguage();

        return function(messageProvider.getMessages(defLocale).replace(path), useChatColor);
    }

    @Override
    public String getDefaultMessage(Path path, boolean useChatColor, Map<String, String> replacements) {
        return replace(getDefaultMessage(path, useChatColor), replacements);
    }

    @Override
    public URL getTexturesURL(Locale locale) {
        if(containsLocale(locale)) {
            return messageProvider.getMessages(locale).textureURL();
        }
        return getDefaultTexturesURL();
    }

    @Override
    public URL getDefaultTexturesURL() {
        Locale defLocale = messageProvider.getDefaultLanguage();

        if(containsLocale(defLocale)) {
            return messageProvider.getMessages(defLocale).textureURL();
        }

        return null;
    }

    private String replace(String message, Map<String, String> replacements) {
        for(Map.Entry<String, String> entry : replacements.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }

    private String function(String message, boolean useChatColor) {
        message = addChatColor(message);
        if(!useChatColor) {
            message = stripChatColor(message);
        }
        return message;
    }

    private String addChatColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String stripChatColor(String message) {
        return ChatColor.stripColor(message);
    }

    private List<String> function(List<String> list, boolean useChatColor) {
        list = list.stream().map(this::addChatColor).toList();
        if(!useChatColor) {
            list = list.stream().map(this::stripChatColor).toList();
        }
        return list;
    }

    @Override
    public List<String> getList(Path path, Locale locale, boolean useChatColor) {
        if(localeEqualsDefault(locale)) {
            return getDefaultList(path, useChatColor);
        }
        if(!hasList(path, locale)) {
            if(logger != null) {
                logger.info("Path for list: '" + path.path() + "' for locale: " + locale + " is blank, please fix the config!");
            }

            if(api.getMainConfig().saveMissingPaths()) {
                api.getConfigSaver().saveMissingListPath(path, locale);
            }

            return getDefaultList(path, useChatColor);
        }

        return function(messageProvider.getLists(locale).replace(path), useChatColor);
    }

    @Override
    public List<String> getList(Path path, Locale locale, boolean useChatColor, Map<String, String> replacements) {
        List<String> list = getList(path, locale, useChatColor);

        return list.stream()
                .map(line -> replace(line, replacements))
                .toList();
    }

    @Override
    public List<String> getDefaultList(Path path, boolean useChatColor) {
        if(!hasDefList(path)) {
            if(logger != null) {
                logger.info("Path for list: '" + path.path() + "' for default locale is blank, please fix the config!");
            }

            if(api.getMainConfig().saveMissingPaths()) {
                api.getConfigSaver().saveMissingListPath(path, messageProvider.getDefaultLanguage());
            }

            if(useChatColor) {
                return List.of(addChatColor("&c" + path.path()));
            }

            return List.of(path.path());
        }
        Locale defLocale = messageProvider.getDefaultLanguage();

        return function(messageProvider.getLists(defLocale).replace(path), useChatColor);
    }

    @Override
    public List<String> getDefaultList(Path path, boolean useChatColor, Map<String, String> replacements) {
        List<String> list = getDefaultList(path, useChatColor);

        return list.stream()
                .map(line -> replace(line, replacements))
                .toList();
    }

    @Override
    public String getFormattedTime(Locale locale, long time) {
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedTimeMillis = currentTimeMillis - time;

        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis);
        if (seconds < 60) {
            return getMessage(FormatPaths.SECONDS_FORMAT, locale, true, Map.of("{seconds}", String.valueOf(seconds)));
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis);
        if (minutes < 60) {
            return getMessage(FormatPaths.MINUTES_FORMAT, locale, true, Map.of("{minutes}", String.valueOf(minutes)));
        }

        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis);
        if (hours < 24) {
            return getMessage(FormatPaths.HOURS_FORMAT, locale, true, Map.of("{hours}", String.valueOf(hours)));
        }

        long days = TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis);
        System.out.println(days);
        if (days < 7) {
            return getMessage(FormatPaths.DAYS_FORMAT, locale, true,  Map.of("{days}", String.valueOf(days)));
        }

        return new SimpleDateFormat(getMessage(FormatPaths.DATE_FORMAT, locale, false)).format(time);
    }

}
