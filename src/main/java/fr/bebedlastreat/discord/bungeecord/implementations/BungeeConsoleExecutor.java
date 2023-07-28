package fr.bebedlastreat.discord.bungeecord.implementations;

import fr.bebedlastreat.discord.common.interfaces.IConsoleExecutor;
import net.md_5.bungee.api.ProxyServer;

public class BungeeConsoleExecutor implements IConsoleExecutor {
    @Override
    public void execute(String command) {
        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), command);
    }
}
