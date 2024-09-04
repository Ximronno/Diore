package ximronno.diore.api;

import org.jetbrains.annotations.ApiStatus;

public final class DioreProvider {

    private static DioreAPI instance = null;

    public static DioreAPI getInstance() {
        DioreAPI instance = DioreProvider.instance;
        if(instance == null) {
            throw new IllegalStateException("DioreAPI is not initialized");
        }
        return instance;
    }

    @ApiStatus.Internal
    static void register(DioreAPI dioreAPI) {
        DioreProvider.instance = dioreAPI;
    }

    @ApiStatus.Internal
    static void unregister() {
        DioreProvider.instance = null;
    }

    @ApiStatus.Internal
    public DioreProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
