package fr.bebedlastreat.discord.common.interfaces;

import java.util.UUID;

public interface IDatabaseFetch {

    void insert(UUID uuid, String name, String discord);

    void update(UUID uuid, String name);

    boolean firstLink(UUID uuid);

    void insertFirstLink(UUID uuid);

    void delete(UUID uuid);

    void delete(String discord);

    boolean exist(String discord);

    boolean exist(UUID uuid);

    String discord(UUID uuid);

    String discord(String name);

    String uuid(String discord);

    String name(UUID uuid);

    int count();

    void insertBoost(UUID uuid, long time);

    void deleteBoost(UUID uuid);

    boolean canBoost(UUID uuid);

    long getNextBoost(UUID uuid);
}
