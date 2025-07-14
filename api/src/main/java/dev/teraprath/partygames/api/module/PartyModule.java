package dev.teraprath.partygames.api.module;

import dev.teraprath.partygames.api.game.GameStateManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class PartyModule implements Listener {

    private ModuleDescription description;
    private Plugin plugin;
    private GameStateManager gameStateManager;

    public final void initialize(ModuleDescription description, Plugin plugin) {
        this.description = description;
        this.plugin = plugin;
    }

    public final void startGame(Runnable onEnd) {
        this.gameStateManager = new GameStateManager(this, plugin, onEnd);
        this.gameStateManager.startWaiting();
    }

    public ModuleDescription getDescription() { return description; }
    public Plugin getPlugin() { return plugin; }

    public abstract void onLoad();
    public abstract void onWaitingStart();
    public abstract void onGameStart();
    public abstract void onTick(int timeLeft);
    public abstract void onGameEnd();
}