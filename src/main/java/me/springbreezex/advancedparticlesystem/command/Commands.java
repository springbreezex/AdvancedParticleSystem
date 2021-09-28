package me.springbreezex.advancedparticlesystem.command;

import me.springbreezex.advancedparticlesystem.utils.Configuration;
import me.springbreezex.advancedparticlesystem.utils.FileHandler;
import me.springbreezex.advancedparticlesystem.utils.ParticleAnimationManager;
import me.springbreezex.advancedparticlesystem.utils.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Commands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //if (sender instanceof Player) return false;
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("advancedparticlesystem.reload")) {
                    FileHandler.loadParticleAnimation();
                    FileHandler.loadConfig();
                    player.sendMessage(Configuration.PluginReloaded);
                } else {
                    player.sendMessage(Configuration.NoPerm);
                }
                return false;
            }
            FileHandler.loadParticleAnimation();
            FileHandler.loadConfig();
            sender.sendMessage(Configuration.PluginReloaded);
            return false;
        }
        if (sender instanceof Player) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("spawn")) {
                    Player player = (Player) sender;
                    if (Configuration.CommandByPlayer) {
                        if (Configuration.CommandPermissionCheck) {
                            if (player.hasPermission("advancedparticlesystem.spawn")) TaskManager.spawnParticlesForPlayer((Player) sender, args[1]);
                            else player.sendMessage(Configuration.NoPerm);
                        } else {
                            TaskManager.spawnParticlesForPlayer((Player) sender, args[1]);
                        }
                    }

                } else {
                    help((Player) sender);
                }
            } else {
                help((Player) sender);
            }
        } else {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("spawn")) {

                    Player p = Bukkit.getPlayer(args[1]);
                    if (p==null||!p.isOnline()) {
                        sender.sendMessage(Configuration.PlayerNotValid);
                    }
                    TaskManager.spawnParticlesForPlayer(p, args[2]);
                } else if (args[0].equalsIgnoreCase("spawnbyuuid")) {
                    Entity entity = Bukkit.getEntity(UUID.fromString(args[1]));
                    if (entity instanceof LivingEntity) {
                        TaskManager.spawnParticlesForLivingEntity((LivingEntity) entity, args[2]);
                    } else {
                        sender.sendMessage(Configuration.TargetNotValid);
                    }

                } else {
                    consoleHelp(sender);
                }
            } else {
                consoleHelp(sender);
            }
        }



        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            boolean canSpawn = true;
            boolean canReload = false;
            Player player = (Player) sender;
            if (Configuration.CommandByPlayer) {
                if (Configuration.CommandPermissionCheck) {
                    if (!player.hasPermission("advancedparticlesystem.spawn")) canSpawn=false;
                }
            } else {
                canSpawn=false;
            }
            if (player.hasPermission("advancedparticlesystem.reload")) canReload=true;

            if (args.length==1) {
                List<String> sub = new ArrayList<>();
                if (canSpawn) sub.add("spawn");
                if (canReload) sub.add("reload");
                return sub;
            } else if (args.length==2) {
                if (args[0].equalsIgnoreCase("spawn")) {
                    if (canSpawn) {
                        return new ArrayList<>(ParticleAnimationManager.getParticleAnimations().keySet());
                    }
                }
            }
        } else {
            if (args.length==1) {
                List<String> sub = new ArrayList<>();
                sub.add("spawn"); sub.add("spawnbyuuid"); sub.add("reload");
                return sub;
            } else if (args.length==3) {
                if (args[0].equalsIgnoreCase("spawn")||args[0].equalsIgnoreCase("spawnbyuuid")) {
                    return new ArrayList<>(ParticleAnimationManager.getParticleAnimations().keySet());
                }
            }
        }

        return null;
    }

    public static void help(Player player) {
        if (Configuration.CommandByPlayer) {
            if (Configuration.CommandPermissionCheck) {
                if (player.hasPermission("advancedparticlesystem.spawn")) player.sendMessage("/aps spawn <name>");
            } else {
                player.sendMessage("/aps spawn <name>");
            }
        }
        if (player.hasPermission("advancedparticlesystem.reload")) player.sendMessage("/aps reload <name>");
    }

    public static void consoleHelp(CommandSender sender) {
        sender.sendMessage("/aps reload");
        sender.sendMessage("/aps spawn <player> <name>");
        sender.sendMessage("/aps spawnbyuuid <uuid> <name>");
    }
}
