package fr.bebedlastreat.discord.bungeecord.implementations;

import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
import net.md_5.bungee.api.CommandSender;

public class BungeeCommandSender implements ICommonCommandSender<CommandSender> {

    private CommandSender sender;

    public BungeeCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }
}
