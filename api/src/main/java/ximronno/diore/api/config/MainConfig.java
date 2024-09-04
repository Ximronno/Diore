package ximronno.diore.api.config;

import java.util.Locale;

public interface MainConfig {

    Locale getDefaultLanguage();

    boolean useLogger();

    boolean useLocateClientLocale();

    boolean enableAutoSave();

    boolean loadSavedAccounts();

    boolean saveMissingPaths();

    int getAutoSaveTicks();

    int getMaxSavedRecentTransactions();

    String getDiamondNuggetName();

}
