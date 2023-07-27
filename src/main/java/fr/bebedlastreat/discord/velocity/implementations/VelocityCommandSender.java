package fr.bebedlastreat.discord.velocity.implementations;

import com.velocitypowered.api.command.CommandSource;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
import net.kyori.adventure.text.Component;

public class VelocityCommandSender implements ICommonCommandSender<CommandSource> {

    private CommandSource sender;

    public VelocityCommandSender(CommandSource sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(Component.text(message));
    }
}
