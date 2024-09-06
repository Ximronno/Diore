package ximronno.diore.api.config;

public interface SQLConfig {

    boolean isEnabled();

    String getHost();

    String getPort();

    String getDataBase();

    String getUsername();

    String getPassword();

}
