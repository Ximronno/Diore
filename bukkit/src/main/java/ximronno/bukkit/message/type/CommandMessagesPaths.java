package ximronno.bukkit.message.type;

import ximronno.bukkit.command.Commands;
import ximronno.diore.api.polyglot.Path;

public enum CommandMessagesPaths implements Path {

    ISSUES_LINK,
    BALANCE_HELP_TITLE(Commands.BALANCE, "help_title"),
    BALANCE_HELP_DESCRIPTION(Commands.BALANCE, "help_description"),
    BALANCE_HELP_FORMAT(Commands.BALANCE, "help_format"),
    BALANCE_INFO_DESCRIPTION(Commands.BALANCE, "info_description"),
    BALANCE_INFO_LIST(Commands.BALANCE, "info_list"),
    BALANCE_LOCALE_DESCRIPTION(Commands.BALANCE, "locale_description"),
    BALANCE_LOCALE_SET(Commands.BALANCE, "locale_set"),
    BALANCE_PRIVATE_BALANCE_DESCRIPTION(Commands.BALANCE, "private_balance_description"),
    BALANCE_PRIVATE_BALANCE_SET_(Commands.BALANCE, "private_balance_set_"),
    BALANCE_PRIVATE_BALANCE_SET(Commands.BALANCE, "private_balance_set"),
    BALANCE_INVALID_AMOUNT(Commands.BALANCE, "invalid_amount"),
    BALANCE_WITHDRAW_DESCRIPTION(Commands.BALANCE, "withdraw_description"),
    BALANCE_WITHDRAW_INSUFFICIENT(Commands.BALANCE, "withdraw_insufficient"),
    BALANCE_WITHDRAW_SUCCESS(Commands.BALANCE, "withdraw_success"),
    BALANCE_DEPOSIT_DESCRIPTION(Commands.BALANCE, "deposit_description"),
    BALANCE_DEPOSIT_SUCCESS(Commands.BALANCE, "deposit_success"),
    BALANCE_DEPOSIT_INSUFFICIENT(Commands.BALANCE, "deposit_insufficient"),
    BALANCE_TRANSFER_DESCRIPTION(Commands.BALANCE, "transfer_description"),
    BALANCE_TRANSFER_SUCCESS(Commands.BALANCE, "transfer_success"),
    BALANCE_TRANSFER_INSUFFICIENT(Commands.BALANCE, "transfer_insufficient"),
    BALANCE_TRANSFER_RECEIVED(Commands.BALANCE, "transfer_received"),;


    private final String path;

    CommandMessagesPaths() {
        path = "commands." + name().toLowerCase();
    }

    CommandMessagesPaths(Commands command, String path) {
        this.path = "commands." + command.name().toLowerCase() + "." + path;
    }

    @Override
    public String path() {
        return path;
    }

}
