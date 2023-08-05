package fr.bebedlastreat.discord.velocity.implementations;

import fr.bebedlastreat.discord.common.interfaces.IConsoleExecutor;
import fr.bebedlastreat.discord.velocity.DiscordSyncVelocity;

public class VelocityConsoleExecutor implements IConsoleExecutor {
    @Override
    public void execute(String command) {
        DiscordSyncVelocity.getServer().getCommandManager().executeAsync(DiscordSyncVelocity.getServer().getConsoleCommandSource(), command);
    }
}
