package fr.bebedlastreat.discord.common.commands;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.objects.WaitingLink;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommand;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;

import java.util.Map;

public class LinkCommand implements ICommonCommand {

    private final DiscordCommon common;

    public LinkCommand(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public void execute(ICommonPlayer<?> player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(common.getMessages().get("link-usage"));
            return;
        }

        common.getAsyncRunner().runAsync(() -> {
            if (common.getDatabaseFetch().exist(player.getUniqueId())) {
                player.sendMessage(common.getMessages().get("you-already-link"));
                return;
            }
            String code = args[0];
            for (Map.Entry<String, WaitingLink> entry : common.getWaitingLinks().entrySet()) {
                String s = entry.getKey();
                WaitingLink waitingLink = entry.getValue();
                if (waitingLink.getExpiry() < System.currentTimeMillis()) {
                    common.getWaitingLinks().remove(s);
                    continue;
                }
                if (waitingLink.getUuid().equals(player.getUniqueId())) {
                    if (code.equalsIgnoreCase(waitingLink.getCode())) {
                        common.getDatabaseFetch().insert(waitingLink.getUuid(), player.getName(), waitingLink.getDiscordId());
                        common.setLinkCount(common.getLinkCount() + 1);
                        common.rename(waitingLink.getDiscordId(), player.getName());
                        player.sendMessage(common.getMessages().get("link-success"));
                        common.getWaitingLinks().remove(s);
                        if (common.getDatabaseFetch().firstLink(player.getUniqueId())) {
                            common.getDatabaseFetch().insertFirstLink(player.getUniqueId());
                            common.setAllTimeLinkCount(common.getAllTimeLinkCount() + 1);
                            if (!common.getRewardCommand().isEmpty()) {
                                common.getConsoleExecutor().execute(common.getRewardCommand().replace("{player}", player.getName()));
                            }
                        }
                    } else {
                        player.sendMessage(common.getMessages().get("invalid-code"));
                    }
                    return;
                }
            }
            player.sendMessage(common.getMessages().get("no-link"));
        });
    }

    @Override
    public void execute(ICommonCommandSender<?> sender, String[] args) {

    }
}
