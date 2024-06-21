package ximronno.diore.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
            completions.add("withdraw");
            completions.add("deposit");
            completions.add("transfer");
            completions.add("top");
            completions.add("language");
            completions.add("public");
        }
        if(strings.length == 2 && strings[0].equals("transfer")) {

            Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));

        }
        else if(strings.length == 2 && strings[0].equals("language")) {

            Arrays.stream(Languages.values())
                    .map(Enum::name)
                    .map(String::toUpperCase)
                    .forEach(completions::add);

        }
        else if(strings.length == 2 && strings[0].equals("public")) {

            completions.add("true");
            completions.add("false");

        }


        return completions;
    }
}
