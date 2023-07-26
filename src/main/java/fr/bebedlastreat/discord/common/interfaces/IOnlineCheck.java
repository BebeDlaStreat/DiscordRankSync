package fr.bebedlastreat.discord.common.interfaces;

import java.util.UUID;

public interface IOnlineCheck {
    boolean isOnline(UUID uuid);

    UUID getUuid(String name);
}