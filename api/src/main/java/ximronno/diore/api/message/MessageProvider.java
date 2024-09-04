package ximronno.diore.api.message;


import ximronno.diore.api.polyglot.LangLists;
import ximronno.diore.api.polyglot.LangMessages;
import ximronno.diore.api.polyglot.Path;
import ximronno.diore.api.polyglot.config.PolyglotConfig;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public record MessageProvider(PolyglotConfig polyglotConfig) {

    public Locale getDefaultLanguage() {
        return polyglotConfig.getDefaultLanguage();
    }

    public Set<Locale> getProvidedLanguages() {
        return polyglotConfig.getProvidedLanguages();
    }

    public Path getMessageDirectory() {
        return polyglotConfig.getMessageDirectory();
    }

    public String getMessageFileName() {
        return polyglotConfig.getMessageFileName();
    }

    public HashMap<Locale, LangMessages> getMessages() {
        return polyglotConfig.getMessages();
    }

    public LangMessages getMessages(Locale locale) {
        return polyglotConfig.getMessages().get(locale);
    }

    public LangLists getLists(Locale locale) {
        return polyglotConfig.getLists().get(locale);
    }

}
