package fr.bebedlastreat.discord.bukkit.pubsub;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import fr.bebedlastreat.discord.common.DiscordCommon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import redis.clients.jedis.JedisPubSub;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class DataPubSub extends JedisPubSub {

    private final DiscordCommon common;

    public DataPubSub(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public void onMessage(String channel, String message) {
        if (DiscordCommon.DATA_CHANNEL.equals(channel)) {
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            Player player = Bukkit.getPlayer(UUID.fromString(in.readUTF()));
            if (player == null) {
                return;
            }
            player.setMetadata("discord_linked", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), in.readBoolean()));
            player.setMetadata("discord_name", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), in.readUTF()));
            player.setMetadata("discord_boosting", new FixedMetadataValue(DiscordSyncBukkit.getInstance(), in.readBoolean()));

        }
    }
}
