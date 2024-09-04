package ximronno.diore.api.message;

import ximronno.diore.api.polyglot.Path;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface MessageManager {


    MessageProvider getMessageProvider();

    boolean hasMessage(Path path, Locale locale);

    boolean hasList(Path path, Locale locale);

    boolean hasDefMessage(Path path);

    boolean hasDefList(Path path);

    String getMessage(Path path, Locale locale, boolean useChatColor);

    String getMessage(Path path, Locale locale, boolean useChatColor, Map<String, String> replacements);

    URL getTexturesURL(Locale locale);

    URL getDefaultTexturesURL();

    String getDefaultMessage(Path path, boolean useChatColor);

    String getDefaultMessage(Path path, boolean useChatColor, Map<String, String> replacements);

    List<String> getList(Path path, Locale locale, boolean useChatColor);

    List<String> getList(Path path, Locale locale, boolean useChatColor, Map<String, String> replacements);

    List<String> getDefaultList(Path path, boolean useChatColor);

    List<String> getDefaultList(Path path, boolean useChatColor, Map<String, String> replacements);

    String getFormattedTime(Locale locale, long time);

}
