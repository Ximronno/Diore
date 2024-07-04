package ximronno.diore.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ximronno.api.command.SubCommand;
import ximronno.diore.commands.managers.BalanceNew;
import ximronno.diore.impl.Languages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BalanceTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completions = new ArrayList<>();

        if(strings.length == 1) {

            for(SubCommand subCommand : BalanceNew.getSubCommands()) {
                completions.add(subCommand.getName());
            }

        }



        return completions;
    }
}
