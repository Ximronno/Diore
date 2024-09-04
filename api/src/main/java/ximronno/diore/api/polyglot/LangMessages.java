package ximronno.diore.api.polyglot;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

public record LangMessages(Locale locale, Map<Path, String> replacements, URL textureURL) {
    @Nullable
    public String replace(Path path) {
        return replacements.get(Path.of(path.path()));
    }

}
