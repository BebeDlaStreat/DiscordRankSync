package fr.bebedlastreat.discord.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.charts.*;
import fr.bebedlastreat.discord.common.enums.DatabaseType;
import fr.bebedlastreat.discord.common.enums.ServerType;
import fr.bebedlastreat.discord.common.logger.VelocityLogger;
import fr.bebedlastreat.discord.common.objects.DiscordActivity;
import fr.bebedlastreat.discord.common.objects.DiscordRank;
import fr.bebedlastreat.discord.velocity.commands.VelocityClaimBoostCommand;
import fr.bebedlastreat.discord.velocity.commands.VelocityLinkCommand;
import fr.bebedlastreat.discord.velocity.commands.VelocityStopbotCommand;
import fr.bebedlastreat.discord.velocity.commands.VelocityUnlinkCommand;
import fr.bebedlastreat.discord.velocity.implementations.VelocityRunner;
import fr.bebedlastreat.discord.velocity.implementations.VelocityConsoleExecutor;
import fr.bebedlastreat.discord.velocity.implementations.VelocityOnlineCheck;
import fr.bebedlastreat.discord.velocity.listeners.VelocityJoinListener;
import fr.bebedlastreat.discord.velocity.squishyyaml.ConfigurationSection;
import fr.bebedlastreat.discord.velocity.squishyyaml.YamlConfiguration;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Activity;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Plugin(
        id = "discordranksync",
        name = "DiscordRankSync",
        version = "${project.version}",
        authors = "BebeDlaStreat",
        description = "Allow user to link their discord to their minecraft account"
)
@Getter
@Setter
public class DiscordSyncVelocity {
    @Getter
    private static DiscordSyncVelocity instance;
    private DiscordCommon common;
    private Metrics metrics;
    private YamlConfiguration config;


    @Getter
    private static ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final Metrics.Factory metricsFactory;

    @Inject
    public DiscordSyncVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        DiscordSyncVelocity.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onEnable(ProxyInitializeEvent event) {
        instance = this;
        saveDefaultConfig();

        String token = getConfig().getString("bot-token");
        String guildId = getConfig().getString("guild-id");
        boolean rename = getConfig().getBoolean("rename");

        List<DiscordRank> ranks = new ArrayList<>();

        for (String key : getConfig().getSection("ranks").getKeys()) {
            ranks.add(new DiscordRank(getConfig().getString("ranks." + key + ".id"), getConfig().getString("ranks." + key + ".permission")));
        }

        String db = getConfig().getString("database");
        DatabaseType databaseType = DatabaseType.getByName(db);
        Map<String, Object> credentials = new HashMap<>();
        switch (databaseType) {
            case SQL: {
                ConfigurationSection section = getConfig().getSection("sql");
                credentials.put("ip", section.getString("ip"));
                credentials.put("port", section.getInteger("port"));
                credentials.put("user", section.getString("user"));
                credentials.put("password", section.getString("password"));
                credentials.put("database", section.getString("database"));
                credentials.put("table", section.getString("table"));
                credentials.put("properties", section.getString("properties"));
                break;
            }
        }

        Map<String, String> messages = new HashMap<>();
        ConfigurationSection messagesSection = getConfig().getSection("messages");
        for (String key : messagesSection.getKeys()) {
            messages.put(key, messagesSection.getString(key));
        }

        DiscordCommon.setLogger(new VelocityLogger(logger));
        server.getScheduler().buildTask(this, () -> {
            DiscordCommon.getLogger().log(Level.INFO, "Configurating the bot...");
            try {
                common = new DiscordCommon(token, guildId, rename, databaseType, ranks, credentials, messages,
                        new VelocityOnlineCheck(), new VelocityRunner(), new VelocityConsoleExecutor(),
                        ServerType.VELOCITY, config.getListString("reward-command"), config.getListString("boost-reward"), config.getString("date-format"),
                        new DiscordActivity(config.getBoolean("activity.enable", false), Activity.ActivityType.valueOf(config.getString("activity.type", "PLAYING")), config.getString("activity.message", "DiscordRankSync")),
                        config.getInteger("join-message-delay", 0));

                EventManager eventManager = server.getEventManager();
                CommandManager commandManager = server.getCommandManager();


                CommandMeta link = commandManager.metaBuilder("link").build();
                commandManager.register(link, new VelocityLinkCommand(common));
                CommandMeta unlink = commandManager.metaBuilder("unlink").build();
                commandManager.register(unlink, new VelocityUnlinkCommand(common));
                CommandMeta claimBoost = commandManager.metaBuilder("claimboost").build();
                commandManager.register(claimBoost, new VelocityClaimBoostCommand(common));
                CommandMeta stopbot = commandManager.metaBuilder("stopbot").aliases("stopdiscord").build();
                commandManager.register(stopbot, new VelocityStopbotCommand(common));

                eventManager.register(this, new VelocityJoinListener(common));

                metrics = metricsFactory.make(this, DiscordCommon.METRICS_ID);
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
        }).schedule();
    }

    private void saveDefaultConfig() {
        File dataFile = dataDirectory.toFile();
        if (!dataFile.exists())
            dataFile.mkdir();
        File file = new File(dataFile, "config.yml");
        this.config = new YamlConfiguration(file);
        config.load();
    }
}
