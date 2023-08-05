package fr.bebedlastreat.discord.bukkit.papi;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import fr.bebedlastreat.discord.common.objects.DiscordMember;
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
            String discord = common.getDatabaseFetch().discord(player.getUniqueId());
            boolean linked = discord != null && !discord.isEmpty();
            String name = "none";
            boolean boosting = false;
            if (linked) {
                DiscordMember member = common.getMember(discord);
                name = member.getEffectiveName();
                boosting = member.isBoosting();
            }
            if (player.isOnline()) {
                player.setMetadata("discord_linked", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), linked));
                player.setMetadata("discord_name", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), name));
                player.setMetadata("discord_boosting", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), boosting));
            }
        });
    }
}
