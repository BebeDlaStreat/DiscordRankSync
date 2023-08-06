package fr.bebedlastreat.discord.velocity.listeners;

import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import com.velocitypowered.api.event.Subscribe;
import fr.bebedlastreat.discord.redisbungee.RedisBungeeManager;

public class RedisBungeeListener {

    @Subscribe
    public void onPubSub(PubSubMessageEvent e) {
        RedisBungeeManager.handlePubSub(e);
    }
}
