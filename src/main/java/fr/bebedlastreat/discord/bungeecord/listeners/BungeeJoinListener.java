package fr.bebedlastreat.discord.bungeecord.listeners;

import fr.bebedlastreat.discord.bungeecord.implementations.BungeePlayer;
import fr.bebedlastreat.discord.common.DiscordCommon;
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
        common.getJoinListener().execute(new BungeePlayer(player));
    }
}
