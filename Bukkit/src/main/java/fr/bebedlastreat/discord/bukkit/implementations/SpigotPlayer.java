package fr.bebedlastreat.discord.bukkit.implementations;

import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;
import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotPlayer implements ICommonPlayer<Player> {

    private final Player player;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public SpigotPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void sendMessage(String message) {
        DiscordSyncBukkit.getInstance().adventure().player(player).sendMessage(mm.deserialize(message));
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
