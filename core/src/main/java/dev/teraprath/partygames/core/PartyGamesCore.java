package dev.teraprath.partygames.core;

import dev.teraprath.partygames.api.addon.Addon;
import dev.teraprath.partygames.core.addon.AddonLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PartyGamesCore extends JavaPlugin {

    private final AddonLoader addonLoader = new AddonLoader(this);

    @Override
    public void onEnable() {

        List<Addon> addons = addonLoader.loadAddons();

        for (Addon addon : addons) {
            getServer().getConsoleSender().sendMessage(addon.toString());
        }
    }

}
