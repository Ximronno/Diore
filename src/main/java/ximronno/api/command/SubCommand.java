package ximronno.api.command;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public abstract class SubCommand {


    public abstract String getName();
    public abstract String getDescription(@Nullable FileConfiguration config);
    public abstract String getSyntax();
    public abstract void perform(Player p, String[] args);







}
