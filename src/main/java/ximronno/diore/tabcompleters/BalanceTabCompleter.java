package ximronno.diore.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ximronno.api.command.SubCommand;
import ximronno.diore.commands.managers.Balance;

import java.util.ArrayList;
import java.util.List;

public class BalanceTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completions = new ArrayList<>();

        if(strings.length == 1) {

            for(SubCommand subCommand : Balance.getSubCommands()) {
                completions.add(subCommand.getName());
            }

        }



        return completions;
    }
}
