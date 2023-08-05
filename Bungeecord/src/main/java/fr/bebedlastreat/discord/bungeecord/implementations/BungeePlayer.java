package fr.bebedlastreat.discord.bungeecord.implementations;

import fr.bebedlastreat.discord.bungeecord.DiscordSyncBungee;
import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeePlayer implements ICommonPlayer<ProxiedPlayer> {

    private final ProxiedPlayer player;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public BungeePlayer(ProxiedPlayer player) {
        this.player = player;
    }

    @Override
    public void sendMessage(String message) {
        DiscordSyncBungee.getInstance().adventure().player(player).sendMessage(mm.deserialize(message));
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
}
