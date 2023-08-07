package fr.bebedlastreat.discord.bukkit;

import fr.bebedlastreat.discord.bukkit.commands.SpigotUnlinkCommand;
import fr.bebedlastreat.discord.bukkit.implementations.SpigotConsoleExecutor;
import fr.bebedlastreat.discord.bukkit.implementations.SpigotOnlineCheck;
import fr.bebedlastreat.discord.bukkit.listeners.DiscordPluginMessageListener;
import fr.bebedlastreat.discord.bukkit.listeners.SpigotJoinListener;
import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.charts.*;
import fr.bebedlastreat.discord.common.enums.DatabaseType;
import fr.bebedlastreat.discord.common.enums.ServerType;
import fr.bebedlastreat.discord.common.logger.DefaultLogger;
import fr.bebedlastreat.discord.common.objects.DiscordActivity;
import fr.bebedlastreat.discord.common.objects.DiscordRank;
import fr.bebedlastreat.discord.bukkit.commands.SpigotClaimBoostCommand;
import fr.bebedlastreat.discord.bukkit.commands.SpigotLinkCommand;
import fr.bebedlastreat.discord.bukkit.commands.SpigotStopbotCommand;
import fr.bebedlastreat.discord.bukkit.implementations.SpigotRunner;
import fr.bebedlastreat.discord.bukkit.papi.DiscordPapi;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Getter
@Setter
public class DiscordSyncBukkit extends JavaPlugin {

    @Getter
    private static DiscordSyncBukkit instance;
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

        String database = getConfig().getString("database");
        DatabaseType databaseType = DatabaseType.getByName(database);
        Map<String, Object> credentials = new HashMap<>();
        if (databaseType == DatabaseType.SQL) {
            ConfigurationSection section = getConfig().getConfigurationSection("sql");
            credentials.put("ip", section.getString("ip"));
            credentials.put("port", section.getInt("port"));
            credentials.put("user", section.getString("user"));
            credentials.put("password", section.getString("password"));
            credentials.put("database", section.getString("database"));
            credentials.put("table", section.getString("table"));
            credentials.put("properties", section.getString("properties"));
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
                        new DiscordActivity(getConfig().getBoolean("activity.enable", false), getConfig().getString("activity.type", "PLAYING"), getConfig().getString("activity.message", "DiscordRankSync")),
                        getConfig().getInt("join-message-delay", 0), getConfig().getInt("refresh-delay", 30));

                registerCommands();
                PluginManager pluginManager = Bukkit.getPluginManager();
                pluginManager.registerEvents(new SpigotJoinListener(common), this);

                boolean standalone = getConfig().getBoolean("standalone", false);

                if (!standalone) {
                    initMetrics();
                } else {
                    DiscordCommon.getLogger().log(Level.INFO, "Moving on standalone mode");
                    common.getJda().shutdown();
                    common.setStandalone(true);
                    getServer().getMessenger().registerIncomingPluginChannel(this, DiscordCommon.PLUGIN_CHANNEL, new DiscordPluginMessageListener());
                }

                Bukkit.getScheduler().runTask(this, () -> {
                    if (pluginManager.getPlugin("PlaceholderAPI") != null) {
                        DiscordPapi.init();
                    }
                });
            } catch (InterruptedException e) {
                DiscordCommon.getLogger().log(Level.SEVERE, "Failed to enable discord bot");
                e.printStackTrace();
            } finally {
                DiscordCommon.getLogger().log(Level.INFO, "Discord bot successfully enabled");
            }
        });
    }

    private void registerCommands() {
        getCommand("link").setExecutor(new SpigotLinkCommand(common));
        getCommand("unlink").setExecutor(new SpigotUnlinkCommand(common));
        getCommand("claimboost").setExecutor(new SpigotClaimBoostCommand(common));
        getCommand("stopbot").setExecutor(new SpigotStopbotCommand(common));
    }

    private void initMetrics() {
        metrics = new Metrics(this, DiscordCommon.METRICS_ID);
        metrics.addCustomChart(new AllTimeLinkCountChart());
        metrics.addCustomChart(new DiscordCountChart());
        metrics.addCustomChart(new DiscordOnlineChart());
        metrics.addCustomChart(new LinkCountChart());
        metrics.addCustomChart(new RankCountChart());
        metrics.addCustomChart(new RenameChart());
        metrics.addCustomChart(new ServerTypeChart());
    }

    @Override
    public void onDisable() {
        if(this.adventure == null) return;
        this.adventure.close();
        this.adventure = null;
    }
}
