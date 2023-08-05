package fr.bebedlastreat.discord.velocity.implementations;

import com.velocitypowered.api.proxy.Player;
import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.UUID;

public class VelocityPlayer implements ICommonPlayer<Player> {

    private final Player player;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public VelocityPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(mm.deserialize(message));
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
