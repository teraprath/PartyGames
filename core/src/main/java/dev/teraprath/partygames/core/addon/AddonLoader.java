package dev.teraprath.partygames.core.addon;

import dev.teraprath.partygames.api.addon.Addon;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AddonLoader {

    private final JavaPlugin plugin;
    private final Path addonsDir;

    public AddonLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        this.addonsDir = plugin.getDataFolder().toPath().resolve("addons");
    }

    public List<Addon> loadAddons() {
        List<Addon> loaded = new ArrayList<>();

        if (!Files.exists(addonsDir)) {
            try {
                Files.createDirectories(addonsDir);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create addons directory!");
                return loaded;
            }
        }

        try (DirectoryStream<Path> jarFiles = Files.newDirectoryStream(addonsDir, "*.jar")) {
            for (Path jarPath : jarFiles) {
                plugin.getLogger().info("Loading Addon: " + jarPath.getFileName());

                try (JarFile jarFile = new JarFile(jarPath.toFile())) {
                    JarEntry configEntry = jarFile.getJarEntry("addon.yml");
                    if (configEntry == null) {
                        plugin.getLogger().warning("No addon.yml found in " + jarPath.getFileName());
                        continue;
                    }

                    // Parse addon.yml
                    InputStream in = jarFile.getInputStream(configEntry);
                    Properties props = new Properties();
                    props.load(in);
                    String mainClass = props.getProperty("main");
                    if (mainClass == null) {
                        plugin.getLogger().warning("Missing 'main' in addon.yml for " + jarPath.getFileName());
                        continue;
                    }

                    // Load class
                    URLClassLoader classLoader = new URLClassLoader(
                            new URL[]{jarPath.toUri().toURL()},
                            this.getClass().getClassLoader()
                    );

                    Class<?> clazz = classLoader.loadClass(mainClass);
                    if (!clazz.isAnnotationPresent(Addon.class)) {
                        plugin.getLogger().warning("Main class is not annotated with @Addon: " + mainClass);
                        continue;
                    }

                    if (!Addon.class.isAssignableFrom(clazz)) {
                        plugin.getLogger().warning("Main class does not extend Addon: " + mainClass);
                        continue;
                    }

                    Constructor<?> constructor = clazz.getConstructor();
                    Addon addon = (Addon) constructor.newInstance();
                    // addon.setAddonDescription(props);
                    // addon.setClassLoader(classLoader);

                    plugin.getLogger().info("Successfully loaded addon: " + props.getProperty("name"));
                    loaded.add(addon);

                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to load addon: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to scan addons directory: " + e.getMessage());
        }

        return loaded;
    }
}