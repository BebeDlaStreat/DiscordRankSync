package fr.bebedlastreat.discord.spigot.implementations;

import fr.bebedlastreat.discord.common.interfaces.ICommonRunner;
import fr.bebedlastreat.discord.spigot.DiscordSyncSpigot;
import org.bukkit.Bukkit;

public class SpigotRunner implements ICommonRunner {
    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(DiscordSyncSpigot.getInstance(), runnable);
    }

    @Override
    public void runLater(Runnable runnable, int ticks) {
        Bukkit.getScheduler().runTaskLater(DiscordSyncSpigot.getInstance(), runnable, ticks);
    }
}
