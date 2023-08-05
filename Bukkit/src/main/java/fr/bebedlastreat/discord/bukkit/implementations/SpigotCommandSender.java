package fr.bebedlastreat.discord.bukkit.implementations;

import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
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
        DiscordSyncBukkit.getInstance().adventure().sender(sender).sendMessage(mm.deserialize(message));
    }
}
