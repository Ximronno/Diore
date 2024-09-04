package ximronno.diore.api.hook;

public interface HookManager {

    void registerVault();

    void unregisterVault();

    static HookManager getInstance() {
        return HookManagerProvider.getInstance();
    }

}
