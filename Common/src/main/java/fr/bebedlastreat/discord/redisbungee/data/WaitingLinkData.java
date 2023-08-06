package fr.bebedlastreat.discord.redisbungee.data;

import fr.bebedlastreat.discord.common.objects.WaitingLink;
import lombok.Data;

@Data
public class WaitingLinkData {

    private final String proxy;
    private final String id;
    private final WaitingLink waitingLink;
}
