package fr.bebedlastreat.discord.common.objects;

import lombok.Data;
import net.dv8tion.jda.api.entities.Role;

@Data
public class DiscordRank {

    public final String discordId;
    private final String permission;
    private Role role;

    public DiscordRank(String discordId, String permission) {
        this.discordId = discordId;
        this.permission = permission;
    }
}
