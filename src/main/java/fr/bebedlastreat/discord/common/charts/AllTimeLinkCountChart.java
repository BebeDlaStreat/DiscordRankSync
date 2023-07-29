package fr.bebedlastreat.discord.common.charts;

import fr.bebedlastreat.discord.common.DiscordCommon;
import org.bstats.charts.SingleLineChart;

public class AllTimeLinkCountChart extends SingleLineChart {
    public AllTimeLinkCountChart() {
        super("all_time_link_count", () -> {
            return DiscordCommon.getInstance().getAllTimeLinkCount();
        });
    }
}
