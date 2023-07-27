package fr.bebedlastreat.discord.common.charts;

import fr.bebedlastreat.discord.common.DiscordCommon;
import org.bstats.charts.SingleLineChart;

public class LinkCountChart extends SingleLineChart {
    public LinkCountChart() {
        super("link_count", () -> {
            return DiscordCommon.getInstance().getLinkCount();
        });
    }
}
