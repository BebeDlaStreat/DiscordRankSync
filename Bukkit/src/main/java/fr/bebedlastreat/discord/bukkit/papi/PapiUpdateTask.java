package fr.bebedlastreat.discord.bukkit.papi;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import fr.bebedlastreat.discord.common.objects.DiscordMember;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class PapiUpdateTask extends BukkitRunnable {
    private final DiscordCommon common;

    public PapiUpdateTask(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
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
        }
    }
}
