package dev.teraprath.partygames.core;

import dev.teraprath.partygames.core.module.ModuleLoader;
import dev.teraprath.partygames.core.game.GameStateManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PartyGamesCore extends JavaPlugin {

    private ModuleLoader moduleLoader;
    private GameStateManager gameStateManager;

    @Override
    public void onEnable() {
        this.moduleLoader = new ModuleLoader(this);
        this.moduleLoader.loadModules();

        this.gameStateManager = new GameStateManager(this, moduleLoader);
        this.gameStateManager.startWaitingPhase();
    }

    @Override
    public void onDisable() {
        if (gameStateManager != null) {
            gameStateManager.stop();
        }
    }

    public ModuleLoader getModuleLoader() {
        return moduleLoader;
    }
}