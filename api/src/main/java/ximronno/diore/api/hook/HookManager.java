package ximronno.diore.api.hook;

public interface HookManager {

    void registerVault();

    void unregisterVault();

    void registerPlaceholders();

    void unregisterPlaceholders();

    static HookManager getInstance() {
        return HookManagerProvider.getInstance();
    }

}
