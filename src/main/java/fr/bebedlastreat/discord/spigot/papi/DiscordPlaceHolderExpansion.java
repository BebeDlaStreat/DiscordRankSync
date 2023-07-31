package fr.bebedlastreat.discord.spigot.papi;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.spigot.DiscordSyncSpigot;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordPlaceHolderExpansion extends PlaceholderExpansion {

    private final DiscordSyncSpigot main;
    private final DiscordCommon common;

    public DiscordPlaceHolderExpansion(DiscordSyncSpigot main, DiscordCommon common) {
        this.main = main;
        this.common = common;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "discordranksync";
    }

    @Override
    public @NotNull String getAuthor() {
        return "BebeDlaStreat";
    }

    @Override
    public @NotNull String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String identifier) {
        if (offlinePlayer == null) return null;
        Player player = offlinePlayer.getPlayer();
        if (player == null) return null;

        if (identifier.equalsIgnoreCase("discord") || identifier.equalsIgnoreCase("is_linked") || identifier.equalsIgnoreCase("is_boost")) {
            boolean linked = false;
            for (MetadataValue value : player.getMetadata("discord_linked")) {
                if (value.getOwningPlugin().equals(main)) {
                    linked = value.asBoolean();
                }
            }
            if (identifier.equalsIgnoreCase("is_linked")) {
                return linked ? "true" : "false";
            }
            if (identifier.equalsIgnoreCase("discord")) {
                if (!linked) return common.getMessages().get("papi-not-linked");
                for (MetadataValue value : player.getMetadata("discord_name")) {
                    if (value.getOwningPlugin().equals(main)) {
                        return value.asString();
                    }
                }

                return "none";
            }
            if (identifier.equalsIgnoreCase("is_boost")) {
                if (!linked) return common.getMessages().get("papi-not-linked");
                for (MetadataValue value : player.getMetadata("discord_boosting")) {
                    if (value.getOwningPlugin().equals(main)) {
                        return value.asBoolean() ? "true" : "false";
                    }
                }
                return "false";
            }
        }

        return null;
    }
}
