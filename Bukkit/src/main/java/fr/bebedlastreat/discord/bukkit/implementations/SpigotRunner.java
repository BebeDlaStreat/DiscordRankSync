package fr.bebedlastreat.discord.bukkit.implementations;

import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import fr.bebedlastreat.discord.common.interfaces.ICommonRunner;
import org.bukkit.Bukkit;

public class SpigotRunner implements ICommonRunner {
    @Override
    public void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(DiscordSyncBukkit.getInstance(), runnable);
    }
    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(DiscordSyncBukkit.getInstance(), runnable);
    }

    @Override
    public void runLater(Runnable runnable, int ticks) {
        Bukkit.getScheduler().runTaskLater(DiscordSyncBukkit.getInstance(), runnable, ticks);
    }

    @Override
    public void runTask(Runnable runnable, int delay, int ticks) {
        Bukkit.getScheduler().runTaskTimer(DiscordSyncBukkit.getInstance(), runnable, delay, ticks);
    }
}
