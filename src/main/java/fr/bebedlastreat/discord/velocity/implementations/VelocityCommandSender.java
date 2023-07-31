package fr.bebedlastreat.discord.velocity.implementations;

import com.velocitypowered.api.command.CommandSource;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class VelocityCommandSender implements ICommonCommandSender<CommandSource> {

    private final CommandSource sender;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public VelocityCommandSender(CommandSource sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(mm.deserialize(message));
    }
}
