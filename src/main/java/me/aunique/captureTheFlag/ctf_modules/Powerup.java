package me.aunique.captureTheFlag.ctf_modules;

import me.aunique.captureTheFlag.CaptureTheFlag;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Random;
import org.joml.Vector3f;

import java.util.Arrays;

public class Powerup {


    public enum PowerupType {
        TEAM_SPEED, //ger hela laget speed 1
        TEAM_DAMAGE_BOOST, // Ger hela laget strength, kanske ändrar fägen på armoren till mörkare?
        TEAM_FLAG_PROTECTION,
        PLAYER_SPEED //ger spelaren speed 2

    }

    Location location;
    Boolean spawned;
    PowerupType powerupType;

    public Powerup(Location loc, PowerupType powerupType){
        this.location = loc;
        this.spawned = false;
        this.powerupType = powerupType;
    }

    public void spawn(Boolean respawnSelf, int respawnIntervalTicks, Boolean randomizePowerup, PowerupType powerupType){
        ItemDisplay item;
        Random random = new Random();
        if(randomizePowerup){
            this.powerupType = PowerupType.values()[random.nextInt(PowerupType.values().length)]; // get random powerup from PowerupType enum
        }
        if(respawnSelf){
            new BukkitRunnable() {
                @Override
                public void run() {
                    item = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);

                    Transformation transformation = new Transformation(
                            new Vector3f(0.0f, 0.5f, 0.0f), // 0.5 over the ground
                            new Quaternionf().rotateY((float) Math.toRadians(90)), //rotate 90 degrees
                            new Vector3f(1.0f, 1.0f, 1.0f),
                            new Quaternionf()
                    );
                    item.setTransformation(transformation);

                }
            }.runTaskTimer(CaptureTheFlag.getInstance(), 15*20L, respawnIntervalTicks);
        }
/*
        switch (powerupType) {
            case TEAM_SPEED -> {

            }
            case TEAM_DAMAGE_BOOST -> {

            }
            case PLAYER_SPEED -> {

            }
            case TEAM_FLAG_PROTECTION -> {

            }
        }

 */


        item[0].setItemStack(new ItemStack(Material.LEATHER_BOOTS));


    }
}
