package fr.bebedlastreat.discord.common.commands;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommand;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;
import fr.bebedlastreat.discord.common.objects.DiscordRank;

import java.util.logging.Level;

public class UnlinkCommand implements ICommonCommand {

    private final DiscordCommon common;

    public UnlinkCommand(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public void execute(ICommonPlayer<?> player, String[] args) {
        common.getRunner().runAsync(() -> {
            if (!common.getDatabaseFetch().exist(player.getUniqueId())) {
                common.getRunner().run(() -> {
                    player.sendMessage(common.getMessages().get("not-linked"));
                });
                return;
            }
            String discord = common.getDatabaseFetch().discord(player.getUniqueId());
            for (DiscordRank rank : common.getRanks()) {
                common.removeRole(discord, rank);
            }

            common.getDatabaseFetch().delete(player.getUniqueId());
            common.setLinkCount(common.getLinkCount() - 1);

            if (!common.getUnlinkCommandList().isEmpty()) {
                common.getRunner().runLater(() -> {
                    for (String command : common.getUnlinkCommandList()) {
                        common.getConsoleExecutor().execute(command.replace("{player}", player.getName()).replace("{uuid}", player.getUniqueId().toString()), player);
                    }
                }, 0);
            }

            common.getRunner().run(() -> {
                player.sendMessage(common.getMessages().get("unlink-success"));
            });
        });
    }

    @Override
    public void execute(ICommonCommandSender<?> sender, String[] args) {

    }
}
