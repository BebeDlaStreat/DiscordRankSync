package fr.bebedlastreat.discord.common.interfaces;

import java.util.UUID;

public interface ICommonPlayer <T> {

    void sendMessage(String message);

    UUID getUniqueId();

    String getName();

    boolean hasPermission(String permission);

    T getPlayer();
}
