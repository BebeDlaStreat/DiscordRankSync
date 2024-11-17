package fr.bebedlastreat.discord.common.charts;

import fr.bebedlastreat.discord.common.DiscordCommon;
import net.dv8tion.jda.api.entities.Guild;
import org.bstats.charts.SingleLineChart;

public class DiscordCountChart extends SingleLineChart {
    public DiscordCountChart() {
        super("discord_count", () -> {
            return DiscordCommon.getInstance().getGuild().retrieveMetaData().map(Guild.MetaData::getApproximateMembers).onErrorMap((error) -> 0).complete();
        });
    }
}
