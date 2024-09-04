package ximronno.bukkit.util;


import ximronno.diore.api.DiorePlugin;
import ximronno.diore.api.DiorePluginProvider;

import java.lang.reflect.Method;

public class PluginRegistrationUtil {

    private static final Method REGISTER_METHOD;

    private static final Method UNREGISTER_METHOD;

    static {
        try {
            REGISTER_METHOD = DiorePluginProvider.class.getDeclaredMethod("register", DiorePlugin.class);
            REGISTER_METHOD.setAccessible(true);

            UNREGISTER_METHOD = DiorePluginProvider.class.getDeclaredMethod("unregister");
            UNREGISTER_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(DiorePlugin diorePlugin) {
        try {
            REGISTER_METHOD.invoke(null, diorePlugin);
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
