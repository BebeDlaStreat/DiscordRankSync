package fr.bebedlastreat.discord.common.logger;

import fr.bebedlastreat.discord.velocity.DiscordSyncVelocity;
import org.slf4j.Logger;

import java.util.logging.Level;

public class VelocityLogger implements IDiscordLogger {
    private final Logger logger = DiscordSyncVelocity.getInstance().getLogger();
    @Override
    public void log(Level level, String message) {
        switch (level.intValue()) {
            case 800: {
                logger.info(message);
                break;
            }
            case 900: {
                logger.warn(message);
                break;
            }
            case 1000: {
                logger.error(message);
                break;
            }
            case 700: {
                logger.debug( message);
                break;
            }
            case 400: {
                logger.trace(message);
                break;
            }
        }
    }
}
