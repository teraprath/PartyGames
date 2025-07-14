package dev.teraprath.partygames.core.game;

import dev.teraprath.partygames.api.module.PartyModule;
import dev.teraprath.partygames.core.PartyGamesCore;
import dev.teraprath.partygames.core.module.ModuleLoader;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameStateManager {

    private final PartyGamesCore plugin;
    private final ModuleLoader moduleLoader;

    private BukkitTask currentTask;
    private int currentModuleIndex = 0;

    private static final int WAITING_SECONDS = 10;
    private static final int ENDING_SECONDS = 10;

    public GameStateManager(PartyGamesCore plugin, ModuleLoader moduleLoader) {
        this.plugin = plugin;
        this.moduleLoader = moduleLoader;
    }

    public void startWaitingPhase() {
        stop();
        plugin.getLogger().info("Waiting phase started!");
        AtomicInteger timeLeft = new AtomicInteger(WAITING_SECONDS);
        currentTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int seconds = timeLeft.getAndDecrement();
            if (seconds <= 0) {
                currentTask.cancel();
                startNextModule();
            }
        }, 0L, 20L);
    }

    private void startNextModule() {
        List<PartyModule> modules = moduleLoader.getModules();
        if (modules.isEmpty()) {
            plugin.getLogger().warning("No modules loaded!");
            startEndingPhase();
            return;
        }

        if (currentModuleIndex >= modules.size()) {
            plugin.getLogger().info("All modules played. Starting ending phase.");
            startEndingPhase();
            return;
        }

        PartyModule module = modules.get(currentModuleIndex++);
        plugin.getLogger().info("Starting module: " + module.getDescription().getName());

        module.startGame(() -> {
            plugin.getLogger().info("Module finished: " + module.getDescription().getName());
            startNextModule();
        });
    }

    private void startEndingPhase() {
        stop();
        plugin.getLogger().info("Game over! Ending phase started.");
        AtomicInteger timeLeft = new AtomicInteger(ENDING_SECONDS);
        currentTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int seconds = timeLeft.getAndDecrement();
            if (seconds <= 0) {
                currentTask.cancel();
                Bukkit.shutdown();
            }
        }, 0L, 20L);
    }

    public void stop() {
        if (currentTask != null) currentTask.cancel();
    }
}