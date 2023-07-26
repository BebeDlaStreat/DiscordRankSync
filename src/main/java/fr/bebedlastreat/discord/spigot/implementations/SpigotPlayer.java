package fr.bebedlastreat.discord.spigot.implementations;

import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotPlayer implements ICommonPlayer<Player> {

    private final Player player;

    public SpigotPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }
}
