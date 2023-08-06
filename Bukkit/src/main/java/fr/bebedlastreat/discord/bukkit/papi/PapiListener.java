package fr.bebedlastreat.discord.bukkit.papi;

import fr.bebedlastreat.discord.bukkit.implementations.SpigotPlayer;
import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class PapiListener implements Listener {

    private final DiscordCommon common;

    public PapiListener(DiscordCommon common) {
        this.common = common;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        common.getRunner().runAsync(() -> {
            common.getData(new SpigotPlayer(player), (linked, discordName, boosting) -> {
                if (player.isOnline()) {
                    player.setMetadata("discord_linked", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), linked));
                    player.setMetadata("discord_name", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), discordName));
                    player.setMetadata("discord_boosting", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), boosting));
                }
            });
        });
    }
}
