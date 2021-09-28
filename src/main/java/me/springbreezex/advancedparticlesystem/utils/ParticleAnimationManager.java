package me.springbreezex.advancedparticlesystem.utils;

import me.springbreezex.advancedparticlesystem.object.ParticleAnimation;

import java.util.HashMap;

public class ParticleAnimationManager {

    private static HashMap<String, ParticleAnimation> lists = new HashMap<>();

    public static void clearAll() {
        lists.clear();
    }

    public static void saveParticleAnimation(String str, ParticleAnimation animation) {
        lists.put(str, animation);
        return;
    }

    public static ParticleAnimation getParticleAnimation(String str) {
        if (lists.containsKey(str)) return lists.get(str);
        return null;
    }

    public static HashMap<String, ParticleAnimation> getParticleAnimations() {
        return lists;
    }

    public static boolean doesExist(String str) {
        return lists.containsKey(str);
    }



}
