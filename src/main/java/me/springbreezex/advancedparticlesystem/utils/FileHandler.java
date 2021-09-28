package me.springbreezex.advancedparticlesystem.utils;

import me.springbreezex.advancedparticlesystem.AdvancedParticleSystem;
import me.springbreezex.advancedparticlesystem.object.ParticleAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileHandler {



    public static void loadConfig() {
        File folder = new File(AdvancedParticleSystem.plugin.getDataFolder() + "");
        folder.mkdirs();
        File config = new File(AdvancedParticleSystem.plugin.getDataFolder()+"/config.yml");
        if (!config.exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "No config found! Creating default config.");
            AdvancedParticleSystem.plugin.saveResource("config.yml", false);
            config = new File(AdvancedParticleSystem.plugin.getDataFolder()+"/config.yml");
        }
        FileConfiguration fileRead = YamlConfiguration.loadConfiguration(config);
        Configuration.CommandByPlayer = fileRead.getBoolean("settings.command-by-player");
        Configuration.CommandPermissionCheck = fileRead.getBoolean("settings.command-permission-check");

        Configuration.NoPerm = ChatColor.translateAlternateColorCodes('&', fileRead.getString("messages.NoPerm"));
        Configuration.PlayerNotValid = ChatColor.translateAlternateColorCodes('&', fileRead.getString("messages.PlayerNotValid"));
        Configuration.TargetNotValid = ChatColor.translateAlternateColorCodes('&', fileRead.getString("messages.TargetNotValid"));
        Configuration.PluginReloaded = ChatColor.translateAlternateColorCodes('&', fileRead.getString("messages.PluginReloaded"));
    }

    public static void loadParticleAnimation() {
        ParticleAnimationManager.clearAll();
        File folder = new File(AdvancedParticleSystem.plugin.getDataFolder() + "/particle");
        folder.mkdirs();
        if (folder.listFiles().length == 0) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "No particle found! Creating default template.");
            AdvancedParticleSystem.plugin.saveResource("particle/example.yml", false);
        }
        for (File file : folder.listFiles()) {
            try {
                if (file.getName().endsWith("yml")) {
                    FileConfiguration fileRead = YamlConfiguration.loadConfiguration(file);
                    ParticleAnimation pa = new ParticleAnimation(fileRead);
                    if (pa.isValid()) {
                        ParticleAnimationManager.saveParticleAnimation(file.getName().replace(".yml",""),pa);
                    }
                }
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + file.getName()+" cannot be loaded! Something went wrong!");
            }
        }
    }
}
