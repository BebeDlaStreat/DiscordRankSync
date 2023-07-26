package fr.bebedlastreat.discord.spigot.implementations;

import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
import org.bukkit.command.CommandSender;
public class SpigotCommandSender implements ICommonCommandSender<CommandSender> {

    private CommandSender sender;

    public SpigotCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }
}
