package fr.bebedlastreat.discord.spigot;

import fr.bebedlastreat.discord.common.charts.*;
import fr.bebedlastreat.discord.common.enums.DatabaseType;
import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.logger.DefaultLogger;
import fr.bebedlastreat.discord.common.enums.ServerType;
import fr.bebedlastreat.discord.common.objects.DiscordActivity;
import fr.bebedlastreat.discord.common.objects.DiscordRank;
import fr.bebedlastreat.discord.spigot.commands.SpigotClaimBoostCommand;
import fr.bebedlastreat.discord.spigot.commands.SpigotLinkCommand;
import fr.bebedlastreat.discord.spigot.commands.SpigotStopbotCommand;
import fr.bebedlastreat.discord.spigot.commands.SpigotUnlinkCommand;
import fr.bebedlastreat.discord.spigot.implementations.SpigotRunner;
import fr.bebedlastreat.discord.spigot.implementations.SpigotConsoleExecutor;
import fr.bebedlastreat.discord.spigot.implementations.SpigotOnlineCheck;
import fr.bebedlastreat.discord.spigot.listeners.SpigotJoinListener;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Activity;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;
import java.util.logging.Level;

@Getter
@Setter
public class DiscordSyncSpigot extends JavaPlugin {

    @Getter
    private static DiscordSyncSpigot instance;

    private DiscordCommon common;
    private Metrics metrics;

    private BukkitAudiences adventure;

    public @NonNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.adventure = BukkitAudiences.create(this);

        String token = getConfig().getString("bot-token");
        String guildId = getConfig().getString("guild-id");
        boolean rename = getConfig().getBoolean("rename");

        List<DiscordRank> ranks = new ArrayList<>();

        for (String key : getConfig().getConfigurationSection("ranks").getKeys(false)) {
            ranks.add(new DiscordRank(getConfig().getString("ranks." + key + ".id"), getConfig().getString("ranks." + key + ".permission")));
        }

        String db = getConfig().getString("database");
        DatabaseType databaseType = DatabaseType.getByName(db);
        Map<String, Object> credentials = new HashMap<>();
        switch (databaseType) {
            case SQL: {
                ConfigurationSection section = getConfig().getConfigurationSection("sql");
                credentials.put("ip", section.getString("ip"));
                credentials.put("port", section.getInt("port"));
                credentials.put("user", section.getString("user"));
                credentials.put("password", section.getString("password"));
                credentials.put("database", section.getString("database"));
                credentials.put("table", section.getString("table"));
                credentials.put("properties", section.getString("properties"));
                break;
            }
        }

        Map<String, String> messages = new HashMap<>();
        ConfigurationSection messagesSection = getConfig().getConfigurationSection("messages");
        for (String key : messagesSection.getKeys(false)) {
            messages.put(key, messagesSection.getString(key));
        }

        DiscordCommon.setLogger(new DefaultLogger(getLogger()));
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            DiscordCommon.getLogger().log(Level.INFO, "Configurating the bot...");
            try {
                common = new DiscordCommon(token, guildId, rename, databaseType, ranks, credentials, messages,
                        new SpigotOnlineCheck(), new SpigotRunner(), new SpigotConsoleExecutor(),
                        ServerType.SPIGOT, getConfig().getStringList("reward-command"), getConfig().getStringList("boost-reward"), getConfig().getString("date-format"),
                        new DiscordActivity(getConfig().getBoolean("activity.enable", false), Activity.ActivityType.valueOf(getConfig().getString("activity.type", "PLAYING")), getConfig().getString("activity.message", "DiscordRankSync")),
                        getConfig().getInt("join-message-delay", 0));

                getCommand("link").setExecutor(new SpigotLinkCommand(common));
                getCommand("unlink").setExecutor(new SpigotUnlinkCommand(common));
                getCommand("claimboost").setExecutor(new SpigotClaimBoostCommand(common));
                getCommand("stopbot").setExecutor(new SpigotStopbotCommand(common));
                PluginManager pm = Bukkit.getPluginManager();
                pm.registerEvents(new SpigotJoinListener(common), this);

                metrics = new Metrics(this, DiscordCommon.METRICS_ID);
                metrics.addCustomChart(new AllTimeLinkCountChart());
                metrics.addCustomChart(new DiscordCountChart());
                metrics.addCustomChart(new DiscordOnlineChart());
                metrics.addCustomChart(new LinkCountChart());
                metrics.addCustomChart(new RankCountChart());
                metrics.addCustomChart(new RenameChart());
                metrics.addCustomChart(new ServerTypeChart());
            } catch (InterruptedException e) {
                DiscordCommon.getLogger().log(Level.SEVERE, "Failed to enable discord bot");
                e.printStackTrace();
            } finally {
                DiscordCommon.getLogger().log(Level.INFO, "Discord bot successfully enabled");
            }
        });
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }
}
