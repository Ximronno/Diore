package ximronno.diore.api.command;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public abstract class SubCommand {

    public abstract String getName();

    public abstract Permission getPermission();

    public abstract String getDescription(@Nullable Locale locale);

    public abstract String getSyntax();

    public abstract List<String> getCompletion(Player p, String[] args, List<String> completion);

    public abstract boolean perform(Player p, String[] args);
}
