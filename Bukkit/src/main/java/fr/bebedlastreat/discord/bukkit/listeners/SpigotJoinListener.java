package fr.bebedlastreat.discord.bukkit.listeners;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.bukkit.implementations.SpigotPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpigotJoinListener implements Listener {

    private final DiscordCommon common;

    public SpigotJoinListener(DiscordCommon common) {
        this.common = common;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        common.getJoinListener().execute(new SpigotPlayer(player));
    }
}
