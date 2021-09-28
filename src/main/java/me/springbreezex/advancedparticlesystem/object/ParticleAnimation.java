package me.springbreezex.advancedparticlesystem.object;

import me.springbreezex.advancedparticlesystem.AdvancedParticleSystem;
import me.springbreezex.advancedparticlesystem.utils.FormulaCalculator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;

public class ParticleAnimation {

    private final HashMap<Integer, SParticleAnimation> animations;
    private boolean valid;

    public ParticleAnimation(FileConfiguration file) {
        animations = new HashMap<>();
        valid = true;
        try {
            for (String key : Objects.requireNonNull(file.getConfigurationSection("")).getValues(false).keySet()) {
                ConfigurationSection section = file.getConfigurationSection(key);
                int i = Integer.parseInt(key);
                assert section != null;
                Particle particleType = Particle.valueOf(section.getString("ParticleType"));
                String particleColorR = section.getString("ParticleColor.R");
                String particleColorG = section.getString("ParticleColor.G");
                String particleColorB = section.getString("ParticleColor.B");
                float particleSpeed = (float) section.getDouble("ParticleSpeed");
                float particleSize = (float) section.getDouble("ParticleSize");
                String formulaX = section.getString("formula.x");
                String formulaY = section.getString("formula.y");
                String formulaZ = section.getString("formula.z");
                int InitialT = section.getInt("InitialT");
                int TIncrement = section.getInt("TIncrement");
                int TEnds = section.getInt("TEnds");
                int delay = section.getInt("Delay");
                int period = section.getInt("Period");
                boolean followTarget = section.getBoolean("FollowTarget");
                boolean baseOnYaw = section.getBoolean("BaseOnYaw");
                boolean terminateIfTargetDies = section.getBoolean("TerminateIfTargetDies");
                boolean terminateIfPlayerQuit = section.getBoolean("TerminateIfPlayerQuit");
                boolean showToPlayerOnly = section.getBoolean("ShowToPlayerOnly");
                SParticleAnimation spa = new SParticleAnimation(particleType,particleColorR,particleColorG,particleColorB,
                        particleSpeed,particleSize,formulaX,formulaY,formulaZ,InitialT,TIncrement,TEnds,delay,period,
                        followTarget,baseOnYaw,terminateIfTargetDies,terminateIfPlayerQuit,showToPlayerOnly);
                animations.put(i, spa);
            }
            Bukkit.getLogger().info(ChatColor.GREEN + file.getName()+" has been created.");
        } catch (Exception ex) {
            animations.clear();
            valid = false;
            Bukkit.getLogger().info(ChatColor.RED + file.getName()+" cannot be created.");
        }
    }

    public boolean isValid() {
        return valid;
    }

    public HashMap<Integer, Integer> run(LivingEntity entity) {
        HashMap<Integer, Integer> runnable = new HashMap<>();
        for (int i: animations.keySet()) {
            runnable.put(i, animations.get(i).run(entity));
        }
        return runnable;
    }

    private class SParticleAnimation {
        private final Particle particleType;
        private final String particleColor_R;
        private final String particleColor_G;
        private final String particleColor_B;
        private final float particleSpeed;
        private final float particleSize;
        private final String formula_X;
        private final String formula_Y;
        private final String formula_Z;
        private final int initial_T;
        private final int tIncrement;
        private final int tEnds;
        private final int delay;
        private final int period;
        private final boolean followTarget;
        private final boolean baseOnYaw; //NOT DONE
        private final boolean terminateIfTargetDies;
        private final boolean terminateIfPlayerQuit;
        private final boolean showToPlayerOnly;

        public SParticleAnimation(Particle particleType, String particleColor_R, String particleColor_G, String particleColor_B,
                                  float particleSpeed, float particleSize, String formula_X, String formula_Y, String formula_Z,
                                  int initial_T, int tIncrement, int tEnds, int delay, int period, boolean followPlayer, boolean baseOnYaw,
                                  boolean terminateIfPlayerDies, boolean terminateIfPlayerQuit, boolean showToPlayerOnly) {
            this.particleType = particleType;
            this.particleColor_R = particleColor_R;
            this.particleColor_G = particleColor_G;
            this.particleColor_B = particleColor_B;
            this.particleSpeed = particleSpeed;
            this.particleSize = particleSize;
            this.formula_X = formula_X;
            this.formula_Y = formula_Y;
            this.formula_Z = formula_Z;
            this.initial_T = initial_T;
            this.tIncrement = tIncrement;
            this.tEnds = tEnds;
            this.delay = delay;
            this.period = period;
            this.followTarget = followPlayer;
            this.baseOnYaw = baseOnYaw;
            this.terminateIfTargetDies = terminateIfPlayerDies;
            this.terminateIfPlayerQuit = terminateIfPlayerQuit;
            this.showToPlayerOnly = showToPlayerOnly;
        }

        public int run(LivingEntity entity) {
            if (showToPlayerOnly && !(entity instanceof Player)) return -1;
            boolean rgbUsed = particleType.equals(Particle.REDSTONE);
            BukkitRunnable runnable = new BukkitRunnable() {
                int t = initial_T;
                final boolean packetParticle = showToPlayerOnly;
                Location loc = entity.getLocation();
                float yaw = (float) (entity.getLocation().getYaw()/180*Math.PI);

                @Override
                public void run() {
                    if (entity instanceof Player && terminateIfPlayerQuit && !((Player) entity).isOnline()) {
                        this.cancel(); return;
                    }
                    if (terminateIfTargetDies && entity.isDead()) {
                        this.cancel(); return;
                    }
                    if (followTarget) {
                        loc = entity.getLocation();
                        yaw = (float) (entity.getLocation().getYaw()/180*Math.PI);
                    }
                    for (int i = 0; i<tIncrement; i++) {
                        if (t>tEnds) {
                            this.cancel(); return;
                        }
                        float X = FormulaCalculator.getFloatByT(formula_X,t);
                        float Y = FormulaCalculator.getFloatByT(formula_Y,t);
                        float Z = FormulaCalculator.getFloatByT(formula_Z,t);
                        if (baseOnYaw) {
                            float newX = (float) (X * Math.cos(yaw) - Z * Math.sin(yaw));
                            float newZ = (float) (X * Math.sin(yaw) + Z * Math.cos(yaw));
                            X = newX; Z=newZ;
                        }
                        if (packetParticle) {
                            assert entity instanceof Player;
                            Player p = (Player) entity;
                            if (rgbUsed) {
                                int R = FormulaCalculator.getIntegerByTBetween0and255(particleColor_R,t);
                                int G = FormulaCalculator.getIntegerByTBetween0and255(particleColor_G,t);
                                int B = FormulaCalculator.getIntegerByTBetween0and255(particleColor_B,t);
                                p.spawnParticle(particleType, loc.clone().add(X,Y,Z), 1, 0, 0, 0, particleSpeed, (Object)new Particle.DustOptions(org.bukkit.Color.fromRGB(R, G, B), particleSize));
                            }
                            else p.spawnParticle(particleType, loc.clone().add(X,Y,Z), 1, 0, 0, 0, particleSpeed);
                        } else {
                            if (rgbUsed) {
                                int R = FormulaCalculator.getIntegerByTBetween0and255(particleColor_R,t);
                                int G = FormulaCalculator.getIntegerByTBetween0and255(particleColor_G,t);
                                int B = FormulaCalculator.getIntegerByTBetween0and255(particleColor_B,t);
                                entity.getWorld().spawnParticle(particleType, loc.clone().add(X,Y,Z), 1, 0, 0, 0, particleSpeed, (Object)new Particle.DustOptions(org.bukkit.Color.fromRGB(R, G, B), particleSize));
                            }
                            else entity.getWorld().spawnParticle(particleType, loc.clone().add(X,Y,Z), 1, 0, 0, 0, particleSpeed);
                        }
                    }
                    t+=tIncrement;
                }
            };
            return runnable.runTaskTimerAsynchronously(AdvancedParticleSystem.plugin, delay, period).getTaskId();
        }

    }
}
