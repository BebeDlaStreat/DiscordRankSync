package fr.bebedlastreat.discord.bukkit.papi;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.bukkit.DiscordSyncBukkit;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class DiscordPlaceHolderExpansion extends PlaceholderExpansion {

    private final DiscordSyncBukkit main;
    private final DiscordCommon common;

    public DiscordPlaceHolderExpansion(DiscordSyncBukkit main, DiscordCommon common) {
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
        final List<String> identifiers = Arrays.asList("discord", "is_linked", "is_boost", "members");
        if(!identifiers.contains(identifier.toLowerCase())) return null;

        boolean linked = false;
        for (MetadataValue value : player.getMetadata("discord_linked")) {
            if (value.getOwningPlugin().equals(main)) {
                linked = value.asBoolean();
            }
        }

        switch (identifier.toLowerCase()) {
            case "is_linked":
                return linked ? "true" : "false";

            case "discord":
                if (!linked) return common.getMessages().get("papi-not-linked");
                for (MetadataValue value : player.getMetadata("discord_name")) {
                    if (value.getOwningPlugin().equals(main)) {
                        return value.asString();
                    }
                }
                return "none";

            case "is_boost":
                if (!linked) return common.getMessages().get("papi-not-linked");
                for (MetadataValue value : player.getMetadata("discord_boosting")) {
                    if (value.getOwningPlugin().equals(main)) {
                        return value.asBoolean() ? "true" : "false";
                    }
                }
                return "false";
            case "": {
                return String.valueOf(DiscordPapi.getMemberCount());
            }
        }

        return null;
    }
}
