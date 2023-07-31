package fr.bebedlastreat.discord.bungeecord.implementations;

import fr.bebedlastreat.discord.bungeecord.DiscordSyncBungee;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.CommandSender;

public class BungeeCommandSender implements ICommonCommandSender<CommandSender> {

    private final CommandSender sender;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public BungeeCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        DiscordSyncBungee.getInstance().adventure().sender(sender).sendMessage(mm.deserialize(message));
    }
}
