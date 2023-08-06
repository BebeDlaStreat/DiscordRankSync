package fr.bebedlastreat.discord.redisbungee.implementations;

import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import fr.bebedlastreat.discord.common.interfaces.IOnlineCheck;

import java.util.UUID;

public class RedisBungeeOnlineCheck implements IOnlineCheck {
    @Override
    public boolean isOnline(UUID uuid) {
        return RedisBungeeAPI.getRedisBungeeApi().isPlayerOnline(uuid);
    }

    @Override
    public UUID getUuid(String name) {
        return RedisBungeeAPI.getRedisBungeeApi().getUuidFromName(name);
    }
}
