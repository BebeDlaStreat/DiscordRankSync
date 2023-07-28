package fr.bebedlastreat.discord.common.commands;

import fr.bebedlastreat.discord.common.DiscordCommon;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommand;
import fr.bebedlastreat.discord.common.interfaces.ICommonCommandSender;
import fr.bebedlastreat.discord.common.interfaces.ICommonPlayer;
import net.dv8tion.jda.api.entities.Member;

import java.time.YearMonth;
import java.util.Date;

public class ClaimBoostCommand implements ICommonCommand {

    private final DiscordCommon common;

    public ClaimBoostCommand(DiscordCommon common) {
        this.common = common;
    }

    @Override
    public void execute(ICommonPlayer<?> player, String[] args) {
        if (common.getRewardCommand().isEmpty()) return;
        common.getAsyncRunner().runAsync(() -> {
            if (!common.getDatabaseFetch().exist(player.getUniqueId())) {
                player.sendMessage(common.getMessages().get("not-linked"));
                return;
            }
            String discord = common.getDatabaseFetch().discord(player.getUniqueId());
            Member member = common.getGuild().retrieveMemberById(discord).complete();
            if (member.isBoosting()) {
                if (common.getDatabaseFetch().canBoost(player.getUniqueId())) {
                    common.getDatabaseFetch().deleteBoost(player.getUniqueId());
                    common.getDatabaseFetch().insertBoost(player.getUniqueId(), System.currentTimeMillis() + (1000L*60*60*24*YearMonth.now().lengthOfMonth()));
                    common.getConsoleExecutor().execute(common.getBoostReward().replace("{player}", player.getName()));
                    player.sendMessage(common.getMessages().get("boost-claim"));
                } else {
                    player.sendMessage(common.getMessages().get("boost-countdown").replace("{date}", common.getSdf().format(new Date(common.getDatabaseFetch().getNextBoost(player.getUniqueId())))));
                }
            } else {
                player.sendMessage(common.getMessages().get("not-boosting"));
            }
        });
    }

    @Override
    public void execute(ICommonCommandSender<?> sender, String[] args) {

    }
}
