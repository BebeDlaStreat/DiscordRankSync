package fr.bebedlastreat.discord.spigot.papi;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.spigot.DiscordSyncSpigot;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class DiscordPapi {

    public void init() {
        new DiscordPlaceHolderExpansion(DiscordSyncSpigot.getInstance(), DiscordCommon.getInstance()).register();
        Bukkit.getPluginManager().registerEvents(new PapiListener(DiscordCommon.getInstance()), DiscordSyncSpigot.getInstance());
        new PapiUpdateTask(DiscordCommon.getInstance()).runTaskTimerAsynchronously(DiscordSyncSpigot.getInstance(), 30*20, 30*20);
    }
}
