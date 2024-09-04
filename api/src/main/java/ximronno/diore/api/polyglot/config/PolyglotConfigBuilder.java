package ximronno.diore.api.polyglot.config;

import ximronno.diore.api.polyglot.LangLists;
import ximronno.diore.api.polyglot.LangMessages;
import ximronno.diore.api.polyglot.Path;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class PolyglotConfigBuilder {


    Locale defaultLanguage;
    Set<Locale> providedLanguages;
    Path messageDirectory;
    String messageFileName;
    HashMap<Locale, LangMessages> messages;
    HashMap<Locale, LangLists> lists;

    public PolyglotConfigBuilder() {
        this.defaultLanguage = new Locale("en");
        this.providedLanguages = new HashSet<>(Set.of(new Locale("en")));
        this.messageDirectory = Path.of("messages");
        this.messageFileName = "{language}.yml";
        this.messages = new HashMap<>();
        this.lists = new HashMap<>();
    }

    public PolyglotConfigBuilder defaultLanguage(Locale defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
        return this;
    }
    public PolyglotConfigBuilder providedLanguages(Set<Locale> providedLanguages) {
        this.providedLanguages = providedLanguages;
        return this;
    }
    public PolyglotConfigBuilder messageDirectory(Path messageDirectory) {
        this.messageDirectory = messageDirectory;
        return this;
    }
    public PolyglotConfigBuilder messageFileName(String messageFileName) {
        this.messageFileName = messageFileName;
        return this;
    }
    public PolyglotConfigBuilder messages(HashMap<Locale, LangMessages> messages) {
        this.messages = messages;
        return this;
    }
    public PolyglotConfigBuilder lists(HashMap<Locale, LangLists> lists) {
        this.lists = lists;
        return this;
    }
    public PolyglotConfig build() {
        return new PolyglotConfig(defaultLanguage, providedLanguages, messageDirectory, messageFileName, messages, lists);
    }
}
