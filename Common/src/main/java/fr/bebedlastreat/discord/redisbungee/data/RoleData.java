package fr.bebedlastreat.discord.redisbungee.data;

import fr.bebedlastreat.discord.common.objects.DiscordRank;
import lombok.Data;

@Data
public class RoleData {
    private final String discordId;
    private final String rankId;

    public RoleData(String discordId, DiscordRank rank) {
        this.discordId = discordId;
        this.rankId = rank.getDiscordId();
    }
}
