package fr.bebedlastreat.discord.velocity.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.velocity.implementations.VelocityPlayer;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class DiscordVelocityPluginMessage {

    public void sendData(Player player, boolean linked, String discordName, boolean boost) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeBoolean(linked);
        out.writeUTF(discordName);
        out.writeBoolean(boost);

        Optional<ServerConnection> currentServer = player.getCurrentServer();
        currentServer.ifPresent(serverConnection -> {
            try {
                serverConnection.sendPluginMessage(MinecraftChannelIdentifier.from(DiscordCommon.PLUGIN_CHANNEL), out.toByteArray());
            } catch (Exception ignore) {}
        });
    }

    public void sendData(Player player) {
        DiscordCommon.getInstance().getData(new VelocityPlayer(player), (linked, discordName, boosting) -> {
            DiscordVelocityPluginMessage.sendData(player, linked, discordName, boosting);
        });
    }
}
