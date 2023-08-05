package fr.bebedlastreat.discord.bukkit.papi;

import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import fr.bebedlastreat.discord.common.DiscordCommon;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class DiscordPapi {

    public void init() {
        new DiscordPlaceHolderExpansion(DiscordSyncBukkit.getInstance(), DiscordCommon.getInstance()).register();
        Bukkit.getPluginManager().registerEvents(new PapiListener(DiscordCommon.getInstance()), DiscordSyncBukkit.getInstance());
        new PapiUpdateTask(DiscordCommon.getInstance()).runTaskTimerAsynchronously(DiscordSyncBukkit.getInstance(), DiscordCommon.getInstance().getPapiDelay()*20L, DiscordCommon.getInstance().getPapiDelay()*20L);
    }
}
