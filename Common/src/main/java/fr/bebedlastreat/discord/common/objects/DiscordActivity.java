package fr.bebedlastreat.discord.common.objects;


import lombok.Data;
import net.dv8tion.jda.api.entities.Activity;

@Data
public class DiscordActivity {

    private final boolean enable;
    private final Activity.ActivityType activityType;
    private final String message;

    public DiscordActivity(boolean enable, String activityType, String message) {
        this.enable = enable;
        this.activityType = Activity.ActivityType.valueOf(activityType);
        this.message = message;
    }
}
