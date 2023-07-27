package fr.bebedlastreat.discord.common;

import fr.bebedlastreat.discord.common.commands.LinkCommand;
import fr.bebedlastreat.discord.common.commands.StopbotCommand;
import fr.bebedlastreat.discord.common.commands.UnlinkCommand;
import fr.bebedlastreat.discord.common.enums.DatabaseType;
import fr.bebedlastreat.discord.common.enums.ServerType;
import fr.bebedlastreat.discord.common.interfaces.IAsyncRunner;
import fr.bebedlastreat.discord.common.interfaces.IDatabaseFetch;
import fr.bebedlastreat.discord.common.interfaces.IOnlineCheck;
import fr.bebedlastreat.discord.common.listeners.SlashCommandListener;
import fr.bebedlastreat.discord.common.objects.DiscordRank;
import fr.bebedlastreat.discord.common.objects.WaitingLink;
import fr.bebedlastreat.discord.common.sql.SqlCredentials;
import fr.bebedlastreat.discord.common.sql.SqlFetch;
import fr.bebedlastreat.discord.common.sql.SqlHandler;
import lombok.Data;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.*;
import java.util.logging.Level;

@Data
public class DiscordCommon {

    @Getter
    private static DiscordCommon instance;
    public static final int METRICS_ID = 19271;

    private final String token;
    private final String guildId;
    private final boolean rename;
    private final JDA jda;
    private final Guild guild;
    private final DatabaseType databaseType;
    private final Map<String, Object> credentials;
    private final Map<String, String> messages;
    private final IDatabaseFetch databaseFetch;
    private final IOnlineCheck onlineCheck;
    private final Map<String, WaitingLink> waitingLinks = new HashMap<>();
    private final List<DiscordRank> ranks;
    private final IAsyncRunner asyncRunner;
    private final LinkCommand linkCommand;
    private final UnlinkCommand unlinkCommand;
    private final StopbotCommand stopbotCommand;
    private final ServerType serverType;
    private int linkCount = 0;

    private SqlHandler sqlHandler;

    public DiscordCommon(String token, String guildId, boolean rename, DatabaseType databaseType, List<DiscordRank> ranks, Map<String, Object> credentials, Map<String, String> messages, IOnlineCheck onlineCheck, IAsyncRunner asyncRunner, ServerType serverType) throws InterruptedException {
        this.token = token;
        this.guildId = guildId;
        this.rename = rename;
        this.messages = messages;
        this.onlineCheck = onlineCheck;
        this.asyncRunner = asyncRunner;
        this.serverType = serverType;
        instance = this;
        this.databaseType = databaseType;
        this.credentials = credentials;
        this.ranks = ranks;
        this.linkCommand = new LinkCommand(this);
        this.unlinkCommand = new UnlinkCommand(this);
        this.stopbotCommand = new StopbotCommand(this);

        switch (databaseType) {
            case SQL: {
                sqlHandler = new SqlHandler(
                        new SqlCredentials((String) credentials.get("ip"), (String) credentials.get("user"), (String) credentials.get("password"), (String) credentials.get("database"), (int) credentials.get("port"), (String) credentials.get("properties")),
                        10, 10, 1800000, 0, 5000, (String) credentials.get("table")
                );
                sqlHandler.createDefault();
                databaseFetch = new SqlFetch(sqlHandler);
                break;
            }
            default: {
                databaseFetch = null;
                break;
            }
        }

        if (databaseFetch == null) {
            throw new RuntimeException("No database storage found, please verify you properly configured the plugin");
        }

        this.jda = JDABuilder.createDefault(token)
                .setAutoReconnect(true)
                .addEventListeners(new SlashCommandListener(this))
                .build();
        jda.upsertCommand("ping", messages.get("ping-command"))
                .queue(command -> DiscordLogger.log(Level.INFO, command.getName() + " command added"));

        jda.upsertCommand("link", messages.get("link-command"))
                .addOption(OptionType.STRING, "name", messages.get("link-command-name"), true)
                .queue(command -> DiscordLogger.log(Level.INFO, command.getName() + " command added"));

        jda.upsertCommand("unlink", messages.get("unlink-command"))
                .queue(command -> DiscordLogger.log(Level.INFO, command.getName() + " command added"));

        jda.upsertCommand("minecraft", messages.get("minecraft-command"))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOption(OptionType.USER, "name", messages.get("minecraft-command-name"), true)
                .queue(command -> DiscordLogger.log(Level.INFO, command.getName() + " command added"));

        jda.upsertCommand("discord", messages.get("discord-command"))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOption(OptionType.STRING, "name", messages.get("discord-command-name"), true)
                .queue(command -> DiscordLogger.log(Level.INFO, command.getName() + " command added"));
        jda.awaitReady();
        guild = jda.getGuildById(guildId);

        for (DiscordRank rank : ranks) {
            Role role = guild.getRoleById(rank.getDiscordId());
            if (role == null) {
                DiscordLogger.log(Level.WARNING, "Rank " + rank.getDiscordId() + " is null");
                continue;
            }
            rank.setRole(role);
        }
        linkCount = databaseFetch.count();
    }

    public void addRole(String discordId, DiscordRank rank) {
        if (rank.getRole() == null) return;
        Member member = guild.retrieveMemberById(discordId).complete();
        if (member == null) return;
        if (member.getRoles().contains(rank.getRole())) return;
        try {
            guild.addRoleToMember(member, rank.getRole()).queue();
        } catch (InsufficientPermissionException | HierarchyException ex) {
            if (ex instanceof InsufficientPermissionException) {
                DiscordLogger.log(Level.WARNING, "the bot has not the permission to manage roles");
            }
        }
    }

    public void removeRole(String discordId, DiscordRank rank) {
        if (rank.getRole() == null) return;
        Member member = guild.retrieveMemberById(discordId).complete();
        if (member == null) return;
        if (!member.getRoles().contains(rank.getRole())) return;
        try {
            guild.removeRoleFromMember(member, rank.getRole()).queue();
        } catch (InsufficientPermissionException | HierarchyException ex) {
            if (ex instanceof InsufficientPermissionException) {
                DiscordLogger.log(Level.WARNING, "the bot has not the permission to manage roles");
            }
        }
    }

    public void rename(String discordId, String name) {
        if (!rename) return;
        if (guild == null) return;
        Member member = guild.retrieveMemberById(discordId).complete();
        if (member == null) return;
        try {
            member.modifyNickname(name).queue();
        } catch (InsufficientPermissionException | HierarchyException ex) {
            if (ex instanceof InsufficientPermissionException) {
                DiscordLogger.log(Level.WARNING, "the bot has not the permission to rename a user");
            } else {
                DiscordLogger.log(Level.WARNING, "the bot can't rename " + member.getUser().getName());
            }
        }
    }

}
