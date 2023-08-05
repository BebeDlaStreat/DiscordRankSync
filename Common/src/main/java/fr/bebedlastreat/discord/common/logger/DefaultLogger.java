package fr.bebedlastreat.discord.common.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultLogger implements IDiscordLogger{

    private final Logger logger;

    public DefaultLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(Level level, String message) {
        logger.log(level, message);
    }
}
