package fr.bebedlastreat.discord.common.charts;

import fr.bebedlastreat.discord.common.DiscordCommon;
import net.dv8tion.jda.api.entities.Guild;
import org.bstats.charts.SingleLineChart;

public class DiscordOnlineChart extends SingleLineChart {
    public DiscordOnlineChart() {
        super("discord_online", () -> {
            return DiscordCommon.getInstance().getGuild().retrieveMetaData().map(Guild.MetaData::getApproximatePresences).onErrorMap((error) -> 0).complete();
        });
    }
}
