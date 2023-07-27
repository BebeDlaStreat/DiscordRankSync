package fr.bebedlastreat.discord.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.velocity.implementations.VelocityPlayer;

public class VelocityJoinListener {

    private final DiscordCommon common;

    public VelocityJoinListener(DiscordCommon common) {
        this.common = common;
    }

    @Subscribe
    public void onJoin(PostLoginEvent e) {
        Player player = e.getPlayer();
        common.getJoinListener().execute(new VelocityPlayer(player));
    }
}
