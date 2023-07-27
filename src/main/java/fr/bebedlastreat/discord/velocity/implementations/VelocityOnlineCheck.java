package fr.bebedlastreat.discord.velocity.implementations;

import com.velocitypowered.api.proxy.Player;
import fr.bebedlastreat.discord.common.interfaces.IOnlineCheck;
import fr.bebedlastreat.discord.velocity.DiscordSyncVelocity;

import java.util.Optional;
import java.util.UUID;

public class VelocityOnlineCheck implements IOnlineCheck {
    @Override
    public boolean isOnline(UUID uuid) {
        return DiscordSyncVelocity.getServer().getPlayer(uuid).isPresent();
    }

    @Override
    public UUID getUuid(String name) {
        Optional<Player> player = DiscordSyncVelocity.getServer().getPlayer(name);
        return player.map(Player::getUniqueId).orElse(null);
    }
}
