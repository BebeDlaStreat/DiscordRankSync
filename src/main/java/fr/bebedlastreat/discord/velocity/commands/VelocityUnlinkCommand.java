package fr.bebedlastreat.discord.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.velocity.implementations.VelocityPlayer;

public class VelocityUnlinkCommand implements SimpleCommand {

    private final DiscordCommon common;

    public VelocityUnlinkCommand(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        String[] args = invocation.arguments();
        common.getUnlinkCommand().execute(new VelocityPlayer(player), args);
    }
}
