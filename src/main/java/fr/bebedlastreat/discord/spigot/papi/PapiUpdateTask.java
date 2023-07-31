package fr.bebedlastreat.discord.spigot.papi;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.spigot.DiscordSyncSpigot;
import net.dv8tion.jda.api.entities.Member;
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
                Member member = common.getGuild().retrieveMemberById(discord).complete();
                name = member.getEffectiveName();
                boosting = member.isBoosting();
            }
            if (player.isOnline()) {
                player.setMetadata("discord_linked", new FixedMetadataValue(DiscordSyncSpigot.getInstance(), linked));
                player.setMetadata("discord_name", new FixedMetadataValue(DiscordSyncSpigot.getInstance(), name));
                player.setMetadata("discord_boosting", new FixedMetadataValue(DiscordSyncSpigot.getInstance(), boosting));
            }
        }
    }
}
