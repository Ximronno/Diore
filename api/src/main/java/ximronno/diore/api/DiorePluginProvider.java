package ximronno.diore.api;

import org.jetbrains.annotations.ApiStatus;

public final class DiorePluginProvider {

    private static DiorePlugin instance = null;

    public static DiorePlugin getInstance() {
        DiorePlugin instance = DiorePluginProvider.instance;
        if(instance == null) {
            throw new IllegalStateException("DiorePlugin is not initialized");
        }
        return instance;
    }

    @ApiStatus.Internal
    static void register(DiorePlugin plugin) {
        DiorePluginProvider.instance = plugin;
    }

    @ApiStatus.Internal
    static void unregister() {
        DiorePluginProvider.instance = null;
    }

    @ApiStatus.Internal
    public DiorePluginProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

}
