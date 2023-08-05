package fr.bebedlastreat.discord.common.charts;

import fr.bebedlastreat.discord.common.DiscordCommon;
import org.bstats.charts.SimplePie;

public class RenameChart extends SimplePie {
    public RenameChart() {
        super("rename", () -> {
            return DiscordCommon.getInstance().isRename() ? "yes" : "no";
        });
    }
}
