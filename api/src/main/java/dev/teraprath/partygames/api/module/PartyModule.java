package dev.teraprath.partygames.api.module;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class PartyModule implements Listener {

    private ModuleDescription description;
    private Plugin plugin;

    public final void initialize(ModuleDescription description, Plugin plugin) {
        this.description = description;
        this.plugin = plugin;
    }

    public ModuleDescription getDescription() { return description; }
    public Plugin getPlugin() { return plugin; }

    public abstract void onEnable();

    public abstract void onDisable();
}