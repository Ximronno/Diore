package ximronno.diore.api.hook;

import org.jetbrains.annotations.ApiStatus;

public class HookManagerProvider {

    private static HookManager instance = null;

    public static HookManager getInstance() {
        HookManager instance = HookManagerProvider.instance;
        if(instance == null) {
            throw new IllegalStateException("DiorePlugin is not initialized");
        }
        return instance;
    }

    @ApiStatus.Internal
    static void register(HookManager manager) {
        HookManagerProvider.instance = manager;
    }

    @ApiStatus.Internal
    static void unregister() {
        HookManagerProvider.instance = null;
    }

    @ApiStatus.Internal
    public HookManagerProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }




}
