package fr.bebedlastreat.discord.bukkit.commands;

import fr.bebedlastreat.discord.bukkit.implementations.SpigotPlayer;
import fr.bebedlastreat.discord.common.DiscordCommon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotClaimBoostCommand implements CommandExecutor {

    private final DiscordCommon common;

    public SpigotClaimBoostCommand(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        common.getClaimBoostCommand().execute(new SpigotPlayer(player), args);

        return false;
    }
}
