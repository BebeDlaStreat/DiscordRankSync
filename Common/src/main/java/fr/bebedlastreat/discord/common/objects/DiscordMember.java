package fr.bebedlastreat.discord.common.objects;

import lombok.Data;

@Data
public class DiscordMember {

    private final String id;
    private String effectiveName;
    private boolean boosting;

    public DiscordMember(String id, String effectiveName, boolean boosting) {
        this.id = id;
        this.effectiveName = effectiveName;
        this.boosting = boosting;
    }
}
