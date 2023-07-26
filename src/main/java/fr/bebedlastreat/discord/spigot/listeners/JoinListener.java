package fr.bebedlastreat.discord.spigot.listeners;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.DiscordRank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final DiscordCommon common;

    public JoinListener(DiscordCommon common) {
        this.common = common;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        common.getAsyncRunner().runAsync(() -> {
            String discord = common.getDatabaseFetch().discord(player.getUniqueId());
            if (discord != null && discord.length() > 0) {
                for (DiscordRank rank : common.getRanks()) {
                    if (player.hasPermission(rank.getPermission())) {
                        common.addRole(discord, rank);
                    } else {
                        common.removeRole(discord, rank);
                    }
                }
            }
        });
    }
}
