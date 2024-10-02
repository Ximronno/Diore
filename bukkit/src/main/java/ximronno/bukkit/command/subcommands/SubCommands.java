package ximronno.bukkit.command.subcommands;

public enum SubCommands {

    BALANCE_HELP("help"),
    BALANCE_INFO("info"),
    BALANCE_LOCALE("locale"),
    BALANCE_PRIVACY("privacy"),
    BALANCE_DEPOSIT("deposit"),
    BALANCE_TRANSFER("transfer"),
    BALANCE_WITHDRAW("withdraw"),
    BALANCE_LEADERBOARD("leaderboard"),
    BALANCE_ADMIN("admin"),
    ADMIN_ADD("add"),
    ADMIN_HELP("help"),
    ADMIN_SET("set");

    private final String name;

    SubCommands(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
