package fr.bebedlastreat.discord.common;

import lombok.experimental.UtilityClass;

import java.util.logging.Level;
import java.util.logging.Logger;

@UtilityClass
public class DiscordLogger {

    private final Logger logger = Logger.getLogger("DiscordRankSync");

    public void log(Level level, String message) {
        logger.log(level, "[DiscordRankSync] " + message);
    }
}
