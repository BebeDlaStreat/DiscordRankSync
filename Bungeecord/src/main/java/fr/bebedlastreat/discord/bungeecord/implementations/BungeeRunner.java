package fr.bebedlastreat.discord.bungeecord.implementations;

import fr.bebedlastreat.discord.bungeecord.DiscordSyncBungee;
import fr.bebedlastreat.discord.common.interfaces.ICommonRunner;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

public class BungeeRunner implements ICommonRunner {
    @Override
    public void runAsync(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(DiscordSyncBungee.getInstance(), runnable);
    }

    @Override
    public void runLater(Runnable runnable, int ticks) {
        ProxyServer.getInstance().getScheduler().schedule(DiscordSyncBungee.getInstance(), runnable, ticks*50L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void runTask(Runnable runnable, int delay, int ticks) {
        ProxyServer.getInstance().getScheduler().schedule(DiscordSyncBungee.getInstance(), runnable, delay*50L, ticks*50L, TimeUnit.MILLISECONDS);
    }
}
