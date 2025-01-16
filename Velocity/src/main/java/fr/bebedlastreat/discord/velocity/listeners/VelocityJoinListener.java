package fr.bebedlastreat.discord.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.velocity.DiscordSyncVelocity;
import fr.bebedlastreat.discord.velocity.implementations.VelocityPlayer;
import fr.bebedlastreat.discord.velocity.utils.DiscordVelocityPluginMessage;

import java.util.concurrent.TimeUnit;

public class VelocityJoinListener {

    private final DiscordCommon common;

    public VelocityJoinListener(DiscordCommon common) {
        this.common = common;
    }

    @Subscribe
    public void onJoin(PostLoginEvent e) {
        Player player = e.getPlayer();
        common.getJoinListener().execute(new VelocityPlayer(player));
        if (common.isRedisBungee()) return;
        DiscordSyncVelocity.getServer().getScheduler().buildTask(DiscordSyncVelocity.getInstance(), () -> {
            DiscordVelocityPluginMessage.sendData(player);
        }).delay(100, TimeUnit.MILLISECONDS).schedule();
    }

    @Subscribe
    public void onServer(ServerConnectedEvent e) {
        Player player = e.getPlayer();
        if (common.isRedisBungee()) return;
        DiscordSyncVelocity.getServer().getScheduler().buildTask(DiscordSyncVelocity.getInstance(), () -> {
            DiscordVelocityPluginMessage.sendData(player);
        }).delay(100, TimeUnit.MILLISECONDS).schedule();
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getIdentifier().getId().equals(DiscordCommon.PLUGIN_CHANNEL) || event.getIdentifier().getId().equals(DiscordCommon.PLUGIN_CHANNEL)) {
            event.setResult(PluginMessageEvent.ForwardResult.handled());
            if (!(event.getSource() instanceof ServerConnection)) return;
            // Handle server message here!
        }
    }
}
