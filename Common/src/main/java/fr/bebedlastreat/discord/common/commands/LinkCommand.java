package fr.bebedlastreat.discord.common.commands;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommand;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;
import fr.bebedlastreat.discord.common.objects.WaitingLink;
import fr.bebedlastreat.discord.redisbungee.RedisBungeeManager;

import java.util.Map;
import java.util.logging.Level;

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

        common.getRunner().runAsync(() -> {
            if (common.getDatabaseFetch().exist(player.getUniqueId())) {
                common.getRunner().run(() -> {
                    player.sendMessage(common.getMessages().get("you-already-link"));
                });
                return;
            }
            String code = args[0];
            for (Map.Entry<String, WaitingLink> entry : common.getWaitingLinks().entrySet()) {
                String s = entry.getKey();
                WaitingLink waitingLink = entry.getValue();
                if (waitingLink.getExpiry() < System.currentTimeMillis()) {
                    common.getWaitingLinks().remove(s);
                    if (common.isRedisBungee()) {
                        RedisBungeeManager.removeWaitingLink(s);
                    }
                    continue;
                }
                if (waitingLink.getUuid().equals(player.getUniqueId())) {
                    if (code.equalsIgnoreCase(waitingLink.getCode())) {
                        common.getDatabaseFetch().insert(waitingLink.getUuid(), player.getName(), waitingLink.getDiscordId());
                        common.setLinkCount(common.getLinkCount() + 1);
                        common.rename(waitingLink.getDiscordId(), player.getName());
                        common.getRunner().run(() -> {
                            player.sendMessage(common.getMessages().get("link-success"));
                        });
                        common.getWaitingLinks().remove(s);
                        if (common.isRedisBungee()) {
                            RedisBungeeManager.removeWaitingLink(s);
                        }
                        DiscordCommon.getLogger().log(Level.INFO, "Player " + player.getName() + " linked with " + waitingLink.getDiscordId());
                        boolean firstLink = common.getDatabaseFetch().firstLink(player.getUniqueId());
                        boolean oneTimeReward = common.isOneTimeReward();
                        if (firstLink) {
                            DiscordCommon.getLogger().log(Level.INFO, "Player " + player.getName() + " linked for the first time");
                            common.getDatabaseFetch().insertFirstLink(player.getUniqueId());
                            common.setAllTimeLinkCount(common.getAllTimeLinkCount() + 1);
                        }
                        if (firstLink || !oneTimeReward) {
                            common.getRunner().runLater(() -> {
                                DiscordCommon.getLogger().log(Level.INFO, "Rewards: " + common.getRewardCommand());
                                if (!common.getRewardCommand().isEmpty()) {
                                    for (String command : common.getRewardCommand()) {
                                        //DiscordCommon.getLogger().log(Level.INFO, "Executing command: " + command.replace("{player}", player.getName()).replace("{uuid}", player.getUniqueId().toString()));
                                        common.getConsoleExecutor().execute(command.replace("{player}", player.getName()).replace("{uuid}", player.getUniqueId().toString()), player);
                                    }
                                }
                            }, 0);
                        }
                    } else {
                        common.getRunner().run(() -> {
                            player.sendMessage(common.getMessages().get("invalid-code"));
                        });
                    }
                    return;
                }
            }
            common.getRunner().run(() -> {
                player.sendMessage(common.getMessages().get("no-link"));
            });
        });
    }

    @Override
    public void execute(ICommonCommandSender<?> sender, String[] args) {

    }
}
