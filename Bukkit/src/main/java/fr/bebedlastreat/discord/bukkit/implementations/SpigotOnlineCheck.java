package fr.bebedlastreat.discord.bukkit.implementations;

import fr.bebedlastreat.discord.common.interfaces.IOnlineCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotOnlineCheck implements IOnlineCheck {
    @Override
    public boolean isOnline(UUID uuid) {
        return Bukkit.getPlayer(uuid) != null;
    }

    @Override
    public UUID getUuid(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            return player.getUniqueId();
        } else {
            return null;
        }
    }
}
