package dev.teraprath.another;

import dev.teraprath.partygames.api.module.PartyModule;
import net.kyori.adventure.text.Component;

public class AnotherModule extends PartyModule {

    @Override
    public void onLoad() {
        getPlugin().getLogger().info("name:" + getDescription().getName());
        getPlugin().getLogger().info("version:" + getDescription().getVersion());
        getPlugin().getLogger().info("main:" + getDescription().getMainClass());
        getPlugin().getLogger().info("author:" + getDescription().getAuthor());
        getPlugin().getLogger().info("description:" + getDescription().getDescription());
        getPlugin().getLogger().info("website:" + getDescription().getWebsite());
    }

    @Override
    public void onWaitingStart() {
        getPlugin().getServer().broadcast(Component.text("Module Waiting started!"));
    }

    @Override
    public void onGameStart() {
        getPlugin().getServer().broadcast(Component.text("Module Start!"));
    }

    @Override
    public void onTick(int timeLeft) {
        getPlugin().getServer().broadcast(Component.text(timeLeft));
    }

    @Override
    public void onGameEnd() {
        getPlugin().getServer().broadcast(Component.text("Module End!"));
    }
}
