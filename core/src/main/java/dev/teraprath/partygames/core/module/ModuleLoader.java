package dev.teraprath.partygames.core.module;

import dev.teraprath.partygames.api.module.PartyModule;
import dev.teraprath.partygames.api.module.ModuleDescription;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleLoader {

    private final JavaPlugin plugin;
    private final Path modulesDir;
    private final List<PartyModule> loadedModules = new ArrayList<>();

    public ModuleLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        this.modulesDir = plugin.getDataFolder().toPath().resolve("modules");
    }

    public List<PartyModule> getModules() {
        return Collections.unmodifiableList(loadedModules);
    }

    public void loadModules() {
        loadedModules.clear();

        if (!Files.exists(modulesDir)) {
            try {
                Files.createDirectories(modulesDir);
                plugin.getLogger().info("Created modules folder at 'plugins/PartyGames'");
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create modules directory!");
                return;
            }
        }

        try (DirectoryStream<Path> jarFiles = Files.newDirectoryStream(modulesDir, "*.jar")) {
            for (Path jarPath : jarFiles) {
                plugin.getLogger().info("Loading Module: " + jarPath.getFileName());

                try (JarFile jarFile = new JarFile(jarPath.toFile())) {
                    JarEntry configEntry = jarFile.getJarEntry("module.yml");
                    if (configEntry == null) {
                        plugin.getLogger().warning("No module.yml found in " + jarPath.getFileName());
                        continue;
                    }

                    Properties props = new Properties();
                    try (InputStream in = jarFile.getInputStream(configEntry)) {
                        props.load(in);
                    }

                    String name = props.getProperty("name");
                    String version = props.getProperty("version");
                    String main = props.getProperty("main");
                    String author = props.getProperty("author");
                    String description = props.getProperty("description", "");
                    String website = props.getProperty("website", "");

                    if (name == null || version == null || main == null || author == null) {
                        plugin.getLogger().warning("Missing required fields in module.yml for " + jarPath.getFileName());
                        continue;
                    }

                    ModuleDescription moduleDescription = new ModuleDescription(name, version, main, author, description, website);

                    URLClassLoader classLoader = new URLClassLoader(
                            new URL[]{jarPath.toUri().toURL()},
                            this.getClass().getClassLoader()
                    );

                    Class<?> clazz = classLoader.loadClass(main);
                    if (!PartyModule.class.isAssignableFrom(clazz)) {
                        plugin.getLogger().warning("Main class does not extend PartyModule: " + main);
                        continue;
                    }

                    Constructor<?> constructor = clazz.getConstructor();
                    PartyModule module = (PartyModule) constructor.newInstance();

                    module.initialize(moduleDescription, plugin);

                    plugin.getLogger().info("Successfully loaded module: " + name + " v" + version);
                    loadedModules.add(module);

                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to load module from " + jarPath.getFileName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to scan modules directory: " + e.getMessage());
        }
    }
}