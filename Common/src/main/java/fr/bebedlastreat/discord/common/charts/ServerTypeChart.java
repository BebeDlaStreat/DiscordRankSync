package fr.bebedlastreat.discord.common.charts;

import fr.bebedlastreat.discord.common.DiscordCommon;
import org.bstats.charts.SimplePie;

public class ServerTypeChart extends SimplePie {
    public ServerTypeChart() {
        super("server_type", () -> {
            return DiscordCommon.getInstance().getServerType().name();
        });
    }
}
