package ximronno.bukkit.util;

import ximronno.diore.api.hook.HookManager;
import ximronno.diore.api.hook.HookManagerProvider;

import java.lang.reflect.Method;

public class HookManagerRegistrationUtil {

    private static final Method REGISTER_METHOD;

    private static final Method UNREGISTER_METHOD;

    static {
        try {
            REGISTER_METHOD = HookManagerProvider.class.getDeclaredMethod("register", HookManager.class);
            REGISTER_METHOD.setAccessible(true);

            UNREGISTER_METHOD = HookManagerProvider.class.getDeclaredMethod("unregister");
            UNREGISTER_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(HookManager hookManager) {
        try {
            REGISTER_METHOD.invoke(null, hookManager);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void unregister() {
        try {
            UNREGISTER_METHOD.invoke(null);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }







}
