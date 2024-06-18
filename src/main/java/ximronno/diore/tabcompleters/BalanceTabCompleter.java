package ximronno.diore.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
        }
        if(strings.length == 2 && strings[0].equals("transfer")) {
            Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));
        }


        return completions;
    }
}
