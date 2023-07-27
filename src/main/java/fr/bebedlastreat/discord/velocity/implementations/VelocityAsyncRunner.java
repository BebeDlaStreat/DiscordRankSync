package fr.bebedlastreat.discord.velocity.implementations;

import fr.bebedlastreat.discord.common.interfaces.IAsyncRunner;
import fr.bebedlastreat.discord.velocity.DiscordSyncVelocity;

public class VelocityAsyncRunner implements IAsyncRunner {
    @Override
    public void runAsync(Runnable runnable) {
        DiscordSyncVelocity.getServer().getScheduler().buildTask(DiscordSyncVelocity.getInstance(), runnable).schedule();
    }
}
