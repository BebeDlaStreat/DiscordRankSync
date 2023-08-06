package fr.bebedlastreat.discord.redisbungee.data;

import lombok.Data;

@Data
public class PubSubMessage {

    private final MessageType messageType;
    private final Object content;
}
