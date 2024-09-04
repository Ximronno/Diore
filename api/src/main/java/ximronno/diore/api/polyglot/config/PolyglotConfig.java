package ximronno.diore.api.polyglot.config;


import ximronno.diore.api.polyglot.LangLists;
import ximronno.diore.api.polyglot.LangMessages;
import ximronno.diore.api.polyglot.Path;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class PolyglotConfig {

    private final Locale defaultLanguage;
    private final Set<Locale> providedLanguages;
    private final Path messageDirectory;
    private final String messageFileName;
    private final HashMap<Locale, LangMessages> messages;
    private final HashMap<Locale, LangLists> lists;

    public PolyglotConfig(Locale defaultLanguage, Set<Locale> providedLanguages, Path messageDirectory, String messageFileName, HashMap<Locale, LangMessages> messages, HashMap<Locale, LangLists> lists) {
        this.defaultLanguage = defaultLanguage;
        this.providedLanguages = providedLanguages;
        this.messageDirectory = messageDirectory;
        this.messageFileName = messageFileName;
        this.messages = messages;
        this.lists = lists;
    }
    public static PolyglotConfigBuilder builder() {
        return new PolyglotConfigBuilder();
    }

    public Locale getDefaultLanguage() {
        return defaultLanguage;
    }

    public Set<Locale> getProvidedLanguages() {
        return providedLanguages;
    }

    public Path getMessageDirectory() {
        return messageDirectory;
    }

    public String getMessageFileName() {
        return messageFileName;
    }

    public HashMap<Locale, LangMessages> getMessages() {
        return messages;
    }

    public HashMap<Locale, LangLists> getLists() {
        return lists;
    }

}
