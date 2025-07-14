package dev.teraprath.partygames.api.game;

import dev.teraprath.partygames.api.module.PartyModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

public class GameStateManager {

    private final PartyModule module;
    private final Plugin plugin;
    private final Runnable onEnd;
    private BukkitTask currentTask;

    private static final int WAITING_SECONDS = 10;
    private static final int TRANSITION_SECONDS = 10;

    public GameStateManager(PartyModule module, Plugin plugin, Runnable onEnd) {
        this.module = module;
        this.plugin = plugin;
        this.onEnd = onEnd;
    }

    public void startWaiting() {
        module.onWaitingStart();
        AtomicInteger timeLeft = new AtomicInteger(WAITING_SECONDS);
        currentTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int seconds = timeLeft.getAndDecrement();
            if (seconds <= 0) {
                currentTask.cancel();
                startGame();
            }
        }, 0L, 20L);
    }

    public void startGame() {
        module.onGameStart();
        int gameDuration = 20; // module.getDescription().getGameDuration();
        AtomicInteger timeLeft = new AtomicInteger(gameDuration);
        currentTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int seconds = timeLeft.getAndDecrement();
            module.onTick(seconds);
            if (seconds <= 0) {
                currentTask.cancel();
                startTransition();
            }
        }, 0L, 20L);
    }

    public void startTransition() {
        module.onGameEnd();
        AtomicInteger timeLeft = new AtomicInteger(TRANSITION_SECONDS);
        currentTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int seconds = timeLeft.getAndDecrement();
            if (seconds <= 0) {
                currentTask.cancel();
                onEnd.run();
            }
        }, 0L, 20L);
    }

    public void stop() {
        if (currentTask != null) currentTask.cancel();
    }
}