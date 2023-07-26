package fr.bebedlastreat.discord.spigot.commands;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.spigot.implementations.SpigotPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotUnlinkCommand implements CommandExecutor {

    private final DiscordCommon common;

    public SpigotUnlinkCommand(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        common.getUnlinkCommand().execute(new SpigotPlayer(player), args);

        return false;
    }
}
