package fr.bebedlastreat.discord.bungeecord.listeners;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.objects.DiscordRank;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeJoinListener implements Listener {

    private final DiscordCommon common;

    public BungeeJoinListener(DiscordCommon common) {
        this.common = common;
    }

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
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
