package fr.bebedlastreat.discord.bungeecord.implementations;

import fr.bebedlastreat.discord.bungeecord.DiscordSyncBungee;
import fr.bebedlastreat.discord.common.interfaces.IAsyncRunner;
import net.md_5.bungee.api.ProxyServer;

public class BungeeAsyncRunner implements IAsyncRunner {
    @Override
    public void runAsync(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(DiscordSyncBungee.getInstance(), runnable);
    }
}
