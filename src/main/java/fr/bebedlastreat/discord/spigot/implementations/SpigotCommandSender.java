package fr.bebedlastreat.discord.spigot.implementations;

import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
import fr.bebedlastreat.discord.spigot.DiscordSyncSpigot;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
public class SpigotCommandSender implements ICommonCommandSender<CommandSender> {

    private final CommandSender sender;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public SpigotCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        DiscordSyncSpigot.getInstance().adventure().sender(sender).sendMessage(mm.deserialize(message));
    }
}
