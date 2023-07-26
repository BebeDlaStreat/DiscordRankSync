package fr.bebedlastreat.discord.common.interfaces;

public interface ICommonCommand {

    void execute(ICommonPlayer<?> player, String[] args);
    void execute(ICommonCommandSender<?> sender, String[] args);
}
