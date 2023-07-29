package fr.bebedlastreat.discord.spigot;

import fr.bebedlastreat.discord.common.charts.*;
import fr.bebedlastreat.discord.common.enums.DatabaseType;
import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.DiscordLogger;
import fr.bebedlastreat.discord.common.enums.ServerType;
import fr.bebedlastreat.discord.common.objects.DiscordRank;
import fr.bebedlastreat.discord.spigot.commands.SpigotClaimBoostCommand;
import fr.bebedlastreat.discord.spigot.commands.SpigotLinkCommand;
import fr.bebedlastreat.discord.spigot.commands.SpigotStopbotCommand;
import fr.bebedlastreat.discord.spigot.commands.SpigotUnlinkCommand;
import fr.bebedlastreat.discord.spigot.implementations.SpigotAsyncRunner;
import fr.bebedlastreat.discord.spigot.implementations.SpigotConsoleExecutor;
import fr.bebedlastreat.discord.spigot.implementations.SpigotOnlineCheck;
import fr.bebedlastreat.discord.spigot.listeners.SpigotJoinListener;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

@Getter
@Setter
public class DiscordSyncSpigot extends JavaPlugin {

    @Getter
    private static DiscordSyncSpigot instance;

    private DiscordCommon common;
    private Metrics metrics;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

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

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            DiscordLogger.log(Level.INFO, "Configurating the bot...");
            try {
                common = new DiscordCommon(token, guildId, rename, databaseType, ranks, credentials, messages, new SpigotOnlineCheck(), new SpigotAsyncRunner(), new SpigotConsoleExecutor(), ServerType.SPIGOT, getConfig().getString("reward-command"), getConfig().getString("boost-reward"), getConfig().getString("date-format"));

                getCommand("link").setExecutor(new SpigotLinkCommand(common));
                getCommand("unlink").setExecutor(new SpigotUnlinkCommand(common));
                getCommand("claimboost").setExecutor(new SpigotClaimBoostCommand(common));
                getCommand("stopbot").setExecutor(new SpigotStopbotCommand(common));
                PluginManager pm = Bukkit.getPluginManager();
                pm.registerEvents(new SpigotJoinListener(common), this);

                metrics = new Metrics(this, DiscordCommon.METRICS_ID);
                metrics.addCustomChart(new DiscordCountChart());
                metrics.addCustomChart(new DiscordOnlineChart());
                metrics.addCustomChart(new LinkCountChart());
                metrics.addCustomChart(new RankCountChart());
                metrics.addCustomChart(new RenameChart());
                metrics.addCustomChart(new ServerTypeChart());
            } catch (InterruptedException e) {
                DiscordLogger.log(Level.SEVERE, "Failed to enable discord bot");
                e.printStackTrace();
            } finally {
                DiscordLogger.log(Level.INFO, "Discord bot successfully enabled");
            }
        });
    }
}
