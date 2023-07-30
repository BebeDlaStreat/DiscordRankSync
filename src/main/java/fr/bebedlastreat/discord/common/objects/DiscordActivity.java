package fr.bebedlastreat.discord.common.objects;


import lombok.Data;
import net.dv8tion.jda.api.entities.Activity;

@Data
public class DiscordActivity {

    private final boolean enable;
    private final Activity.ActivityType activityType;
    private final String message;

    public DiscordActivity(boolean enable, Activity.ActivityType activityType, String message) {
        this.enable = enable;
        this.activityType = activityType;
        this.message = message;
    }
}
