package fr.bebedlastreat.discord.spigot.commands;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.spigot.implementations.SpigotPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotLinkCommand implements CommandExecutor {

    private final DiscordCommon common;

    public SpigotLinkCommand(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        common.getLinkCommand().execute(new SpigotPlayer(player), args);

        return false;
    }
}
