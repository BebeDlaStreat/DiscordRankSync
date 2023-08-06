package fr.bebedlastreat.discord.bukkit.papi;

import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import fr.bebedlastreat.discord.common.DiscordCommon;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class DiscordPapi {

    public void init() {
        new DiscordPlaceHolderExpansion(DiscordSyncBukkit.getInstance(), DiscordCommon.getInstance()).register();
        if (!DiscordCommon.getInstance().isStandalone()) {
            Bukkit.getPluginManager().registerEvents(new PapiListener(DiscordCommon.getInstance()), DiscordSyncBukkit.getInstance());
            new PapiUpdateTask(DiscordCommon.getInstance()).runTaskTimerAsynchronously(DiscordSyncBukkit.getInstance(), DiscordCommon.getInstance().getRefreshDelay()*20L, DiscordCommon.getInstance().getRefreshDelay()*20L);
        }
    }
}
