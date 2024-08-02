package fr.bebedlastreat.discord.bungeecord.implementations;

import fr.bebedlastreat.discord.bungeecord.utils.DiscordBungeePluginMessage;
import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;
import fr.bebedlastreat.discord.common.interfaces.IConsoleExecutor;
import net.md_5.bungee.api.ProxyServer;

public class BungeeConsoleExecutor implements IConsoleExecutor {
    @Override
    public void execute(String command, ICommonPlayer<?> target) {
        if (command.startsWith("[FORWARD] ")) {
            command = command.substring(10);
            DiscordBungeePluginMessage.sendCommand(((BungeePlayer) target).getPlayer(), command);
        } else {
            ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), command);
        }
    }
}
