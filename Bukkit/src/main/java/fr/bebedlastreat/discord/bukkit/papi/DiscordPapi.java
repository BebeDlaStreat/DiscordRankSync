package fr.bebedlastreat.discord.bukkit.papi;

import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import fr.bebedlastreat.discord.common.DiscordCommon;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;

@UtilityClass
public class DiscordPapi {

    @Getter
    @Setter
    private int memberCount;

    public void init() {
        if (!DiscordCommon.getInstance().isStandalone()) {
            memberCount =  DiscordCommon.getInstance().getGuild().retrieveMetaData().map(metaData -> metaData.getApproximateMembers()).onErrorMap((error) -> 0).complete();
        }
        new DiscordPlaceHolderExpansion(DiscordSyncBukkit.getInstance(), DiscordCommon.getInstance()).register();
        if (!DiscordCommon.getInstance().isStandalone()) {
            Bukkit.getPluginManager().registerEvents(new PapiListener(DiscordCommon.getInstance()), DiscordSyncBukkit.getInstance());
            new PapiUpdateTask(DiscordCommon.getInstance()).runTaskTimerAsynchronously(DiscordSyncBukkit.getInstance(), DiscordCommon.getInstance().getRefreshDelay()*20L, DiscordCommon.getInstance().getRefreshDelay()*20L);
        }
    }
}
