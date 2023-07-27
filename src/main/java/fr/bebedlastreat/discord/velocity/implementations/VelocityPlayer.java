package fr.bebedlastreat.discord.velocity.implementations;

import com.velocitypowered.api.proxy.Player;
import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class VelocityPlayer implements ICommonPlayer<Player> {

    private final Player player;

    public VelocityPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(Component.text(message));
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getUsername();
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
}
