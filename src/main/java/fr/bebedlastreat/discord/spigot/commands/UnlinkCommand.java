package fr.bebedlastreat.discord.spigot.commands;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.DiscordRank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnlinkCommand implements CommandExecutor {

    private final DiscordCommon common;

    public UnlinkCommand(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        common.getAsyncRunner().runAsync(() -> {
            if (!common.getDatabaseFetch().exist(player.getUniqueId())) {
                player.sendMessage(common.getMessages().get("not-linked"));
                return;
            }
            String discord = common.getDatabaseFetch().discord(player.getUniqueId());
            for (DiscordRank rank : common.getRanks()) {
                common.removeRole(discord, rank);
            }

            common.getDatabaseFetch().delete(player.getUniqueId());
            player.sendMessage(common.getMessages().get("unlink-success"));
        });

        return false;
    }
}
