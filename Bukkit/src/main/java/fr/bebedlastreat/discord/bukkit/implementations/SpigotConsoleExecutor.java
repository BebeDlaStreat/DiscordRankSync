package fr.bebedlastreat.discord.bukkit.implementations;

import fr.bebedlastreat.discord.common.interfaces.IConsoleExecutor;
import org.bukkit.Bukkit;

public class SpigotConsoleExecutor implements IConsoleExecutor {
    @Override
    public void execute(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
