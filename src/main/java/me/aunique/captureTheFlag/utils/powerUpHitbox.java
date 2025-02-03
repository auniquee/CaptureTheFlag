package me.aunique.captureTheFlag.utils;

import me.aunique.captureTheFlag.ctf_modules.PowerUp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.potion.PotionType;

public class powerUpHitbox {
    // i read that you could use "AreaEffectCloud" to make performant hitboxes, that'll trigger upon collision
    private AreaEffectCloud hitbox;
    private PowerUp powerUp;

    public powerUpHitbox(PowerUp powerUp, Location loc, String id) {
        this.powerUp = powerUp;
        setHitbox(loc, id);
    }

    public void setHitbox(Location hitboxLocation, String id) {
        this.hitbox = hitboxLocation.getWorld().spawn(hitboxLocation, AreaEffectCloud.class);
        hitbox.setRadius(1.5f);
        hitbox.setDuration(Integer.MAX_VALUE);
        hitbox.setWaitTime(0); // Check every tick
        hitbox.setBasePotionType(PotionType.SWIFTNESS);
        //hitbox.clearCustomEffects();
        hitbox.setParticle(Particle.BLOCK, Material.AIR.createBlockData());
        hitbox.addScoreboardTag(powerUp.toString());

    }

    public String getId(){
        return hitbox.getScoreboardTags().iterator().next();
    }

    public AreaEffectCloud getHitbox() {
        if(hitbox.isDead()){
            return null;
        }
        return hitbox;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public void deleteHitbox() {
        this.hitbox.remove();
    }
}
