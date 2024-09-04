package ximronno.diore.api;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;

public final class DioreKeysProvider {

    private static NamespacedKey key = null;

    public static NamespacedKey getKey() {
        NamespacedKey key = DioreKeysProvider.key;
        if(key == null) {
            throw new IllegalStateException("Diore keys is not initialized");
        }
        return key;
    }

    @ApiStatus.Internal
    static void register(NamespacedKey key) {
        DioreKeysProvider.key = key;
    }

    @ApiStatus.Internal
    static void unregister() {
        DioreKeysProvider.key = null;
    }

    @ApiStatus.Internal
    public DioreKeysProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

}
