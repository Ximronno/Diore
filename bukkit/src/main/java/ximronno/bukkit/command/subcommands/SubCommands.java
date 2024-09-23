package ximronno.bukkit.command.subcommands;

public enum SubCommands {

    BALANCE_HELP("help"),
    BALANCE_INFO("info"),
    BALANCE_LOCALE("locale"),
    BALANCE_PRIVACY("privacy"),
    BALANCE_DEPOSIT("deposit"),
    BALANCE_TRANSFER("transfer"),
    BALANCE_WITHDRAW("withdraw"),
    BALANCE_LEADERBOARD("leaderboard");

    private final String name;

    SubCommands(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
