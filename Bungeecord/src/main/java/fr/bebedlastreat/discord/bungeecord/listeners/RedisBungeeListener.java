package fr.bebedlastreat.discord.bungeecord.listeners;

import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import fr.bebedlastreat.discord.redisbungee.RedisBungeeManager;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class RedisBungeeListener implements Listener {

    @EventHandler
    public void onPubSub(PubSubMessageEvent e) {
        RedisBungeeManager.handlePubSub(e);
    }
}
