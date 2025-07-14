package dev.teraprath.example;

import dev.teraprath.partygames.api.module.PartyModule;

public class ExampleModule extends PartyModule {

    @Override
    public void onEnable() {
        getPlugin().getLogger().info("name:" + getDescription().getName());
        getPlugin().getLogger().info("version:" + getDescription().getVersion());
        getPlugin().getLogger().info("main:" + getDescription().getMainClass());
        getPlugin().getLogger().info("author:" + getDescription().getAuthor());
        getPlugin().getLogger().info("description:" + getDescription().getDescription());
        getPlugin().getLogger().info("website:" + getDescription().getWebsite());
    }

    @Override
    public void onDisable() {

    }
}
