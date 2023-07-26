package fr.bebedlastreat.discord.spigot.implementations;

import fr.bebedlastreat.discord.common.interfaces.IAsyncRunner;
import fr.bebedlastreat.discord.spigot.DiscordSyncSpigot;
import org.bukkit.Bukkit;

public class SpigotAsyncRunner implements IAsyncRunner {
    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(DiscordSyncSpigot.getInstance(), runnable);
    }
}
