package me.springbreezex.advancedparticlesystem;

import me.springbreezex.advancedparticlesystem.command.Commands;
import me.springbreezex.advancedparticlesystem.utils.FileHandler;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class AdvancedParticleSystem extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        getLogger().info("");
        getLogger().info(ChatColor.AQUA+"     Advanced"+ChatColor.WHITE+"Particle"+ChatColor.GRAY+"System");
        getLogger().info("         "+ChatColor.WHITE+"v "+ getVersion() +" - ALPHA");
        getLogger().info("         "+ChatColor.GRAY+"Developer: SpringBreezeX");
        getLogger().info("");

        FileHandler.loadConfig();
        FileHandler.loadParticleAnimation();

        this.getCommand("aps").setExecutor(new Commands());
        getLogger().info(ChatColor.GREEN+"[AdvancedParticleSystem] Plugin has been loaded successfully");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(ChatColor.RED+"[AdvancedParticleSystem] Plugin has been unloaded");
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }
}
