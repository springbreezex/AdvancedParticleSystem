package me.springbreezex.advancedparticlesystem.utils;

import me.springbreezex.advancedparticlesystem.object.ParticleAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;


public class TaskManager {

    public static void spawnParticlesForPlayer(Player p, String name) {
        ParticleAnimation animation = ParticleAnimationManager.getParticleAnimation(name);
        if (animation==null) {
            p.sendMessage(ChatColor.RED+ "Particle Animation with such id does not exist");
        } else {
            HashMap<Integer,Integer> tasksMap = animation.run(p);
        }
    }

    public static void spawnParticlesForLivingEntity(LivingEntity entity, String name) {
        ParticleAnimation animation = ParticleAnimationManager.getParticleAnimation(name);
        if (animation==null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+ "Particle Animation with such id does not exist");
        } else {
            animation.run(entity);
        }
    }
}
