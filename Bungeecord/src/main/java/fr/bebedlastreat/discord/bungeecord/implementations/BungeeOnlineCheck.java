package fr.bebedlastreat.discord.bungeecord.implementations;

import fr.bebedlastreat.discord.common.interfaces.IOnlineCheck;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeeOnlineCheck implements IOnlineCheck {
    @Override
    public boolean isOnline(UUID uuid) {
        return ProxyServer.getInstance().getPlayer(uuid) != null;
    }

    @Override
    public UUID getUuid(String name) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
        if (player != null) {
            return player.getUniqueId();
        } else {
            return null;
        }
    }
}
