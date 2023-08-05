package fr.bebedlastreat.discord.bungeecord.commands;

import fr.bebedlastreat.discord.bungeecord.implementations.BungeePlayer;
import fr.bebedlastreat.discord.common.DiscordCommon;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeClaimBoostCommand extends Command {

    private final DiscordCommon common;

    public BungeeClaimBoostCommand(DiscordCommon common) {
        super("claimboost");
        this.common = common;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        common.getClaimBoostCommand().execute(new BungeePlayer(player), args);

    }
}
