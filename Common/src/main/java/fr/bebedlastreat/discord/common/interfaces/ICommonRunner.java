package fr.bebedlastreat.discord.common.interfaces;

public interface ICommonRunner {
    void runAsync(Runnable runnable);
    void runLater(Runnable runnable, int ticks);
    void runTask(Runnable runnable, int delay, int ticks);
}