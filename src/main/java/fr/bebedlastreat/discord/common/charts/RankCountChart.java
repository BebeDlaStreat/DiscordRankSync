package fr.bebedlastreat.discord.common.charts;

import fr.bebedlastreat.discord.common.DiscordCommon;
import org.bstats.charts.SingleLineChart;

public class RankCountChart extends SingleLineChart {
    public RankCountChart() {
        super("rank_count", () -> {
            return DiscordCommon.getInstance().getRanks().size();
        });
    }
}
