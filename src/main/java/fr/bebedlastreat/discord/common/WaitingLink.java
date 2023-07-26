package fr.bebedlastreat.discord.common;

import java.util.UUID;

public class WaitingLink {
    private String discordId;
    private long expiry;
    private UUID uuid;
    private String code;

    public WaitingLink(String discordId, long expiry, UUID uuid, String code) {
        this.discordId = discordId;
        this.expiry = expiry;
        this.uuid = uuid;
        this.code = code;
    }

    public String getDiscordId() {
        return discordId;
    }

    public long getExpiry() {
        return expiry;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getCode() {
        return code;
    }
}