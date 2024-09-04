package ximronno.bukkit.util;



import ximronno.diore.api.DioreAPI;
import ximronno.diore.api.DioreProvider;

import java.lang.reflect.Method;

public class ApiRegistrationUtil {

    private static final Method REGISTER_METHOD;

    private static final Method UNREGISTER_METHOD;

    static {
        try {
            REGISTER_METHOD = DioreProvider.class.getDeclaredMethod("register", DioreAPI.class);
            REGISTER_METHOD.setAccessible(true);

            UNREGISTER_METHOD = DioreProvider.class.getDeclaredMethod("unregister");
            UNREGISTER_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(DioreAPI dioreAPI) {
        try {
            REGISTER_METHOD.invoke(null, dioreAPI);
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
