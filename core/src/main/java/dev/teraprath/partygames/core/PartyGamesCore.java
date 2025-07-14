package dev.teraprath.partygames.core;

import dev.teraprath.partygames.api.module.PartyModule;
import dev.teraprath.partygames.core.module.ModuleLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class PartyGamesCore extends JavaPlugin {

    private final ModuleLoader moduleLoader = new ModuleLoader(this);

    @Override
    public void onEnable() {

        moduleLoader.loadModules();

        for (PartyModule module : moduleLoader.getModules()) {
            module.onEnable();
        }

    }

    @Override
    public void onDisable() {
    }

}
