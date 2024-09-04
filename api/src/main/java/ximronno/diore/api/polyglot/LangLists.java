package ximronno.diore.api.polyglot;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public record LangLists(Locale locale, HashMap<Path, List<String>> replacements) {
    @Nullable
    public List<String> replace(Path path) {
        return replacements.get(Path.of(path.path()));
    }

}
