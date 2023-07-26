package fr.bebedlastreat.discord.spigot.commands;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.WaitingLink;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class LinkCommand implements CommandExecutor {

    private final DiscordCommon common;

    public LinkCommand(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage(common.getMessages().get("link-usage"));
            return false;
        }

        common.getAsyncRunner().runAsync(() -> {
            if (common.getDatabaseFetch().exist(player.getUniqueId())) {
                player.sendMessage(common.getMessages().get("you-already-link"));
                return;
            }
            String code = args[0];
            for (Map.Entry<String, WaitingLink> entry : common.getWaitingLinks().entrySet()) {
                String s = entry.getKey();
                WaitingLink waitingLink = entry.getValue();
                if (waitingLink.getExpiry() < System.currentTimeMillis()) {
                    common.getWaitingLinks().remove(s);
                    continue;
                }
                if (waitingLink.getUuid().equals(player.getUniqueId())) {
                    if (code.equalsIgnoreCase(waitingLink.getCode())) {
                        common.getDatabaseFetch().insert(waitingLink.getUuid(), player.getName(), waitingLink.getDiscordId());
                        common.rename(waitingLink.getDiscordId(), player.getName());
                        player.sendMessage(common.getMessages().get("link-success"));
                        common.getWaitingLinks().remove(s);
                    } else {
                        player.sendMessage(common.getMessages().get("invalid-code"));
                    }
                    return;
                }
            }
            player.sendMessage(common.getMessages().get("no-link"));
        });

        return false;
    }
}
