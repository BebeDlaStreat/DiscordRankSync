package fr.bebedlastreat.discord.bukkit.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import fr.bebedlastreat.discord.common.DiscordCommon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class DiscordPluginMessageListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if (s.equals(DiscordCommon.PLUGIN_CHANNEL)) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            player.setMetadata("discord_linked", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), in.readBoolean()));
            player.setMetadata("discord_name", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), in.readUTF()));
            player.setMetadata("discord_boosting", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), in.readBoolean()));
        } else if (s.equals(DiscordCommon.COMMAND_CHANNEL)) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), in.readUTF());
        }
    }
}
