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

    double getMaxWithdraw();

    double stealPercentage();

    double minimumSteal();

    double getDefaultBalance();

    SQLConfig getSQLConfig();

    String getDiamondNuggetName();

    boolean useDiamonds();

    boolean useDiamondsNuggets();

    boolean stealOnKill();

    boolean enableGui();

    HooksConfig getHooksConfig();

}
