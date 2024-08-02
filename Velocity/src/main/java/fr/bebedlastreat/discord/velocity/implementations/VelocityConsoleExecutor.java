package fr.bebedlastreat.discord.velocity.implementations;

import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;
import fr.bebedlastreat.discord.common.interfaces.IConsoleExecutor;
import fr.bebedlastreat.discord.velocity.DiscordSyncVelocity;
import fr.bebedlastreat.discord.velocity.utils.DiscordVelocityPluginMessage;

public class VelocityConsoleExecutor implements IConsoleExecutor {
    @Override
    public void execute(String command, ICommonPlayer<?> target) {
        if (command.startsWith("[FORWARD] ")) {
            command = command.substring(10);
            DiscordVelocityPluginMessage.sendCommand(((VelocityPlayer) target).getPlayer(), command);
        } else {
            DiscordSyncVelocity.getServer().getCommandManager().executeAsync(DiscordSyncVelocity.getServer().getConsoleCommandSource(), command);
        }
    }
}
