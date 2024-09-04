package ximronno.bukkit.util;

import org.bukkit.NamespacedKey;
import ximronno.diore.api.DioreKeysProvider;

import java.lang.reflect.Method;

public class KeyRegistrationUtil {

    private static final Method REGISTER_METHOD;

    private static final Method UNREGISTER_METHOD;

    static {
        try {
            REGISTER_METHOD = DioreKeysProvider.class.getDeclaredMethod("register", NamespacedKey.class);
            REGISTER_METHOD.setAccessible(true);

            UNREGISTER_METHOD = DioreKeysProvider.class.getDeclaredMethod("unregister");
            UNREGISTER_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(NamespacedKey key) {
        try {
            REGISTER_METHOD.invoke(null, key);
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
