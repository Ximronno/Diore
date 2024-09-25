package ximronno.diore.api.config;

public interface SQLConfig {

    boolean isEnabled();

    boolean alwaysLoadOnJoin();

    String getHost();

    String getPort();

    String getDataBase();

    String getUsername();

    String getPassword();

    long getLoadDelay();

}
