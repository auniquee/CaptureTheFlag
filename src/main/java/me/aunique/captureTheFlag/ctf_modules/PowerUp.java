package me.aunique.captureTheFlag.ctf_modules;

import me.aunique.captureTheFlag.CaptureTheFlag;
import me.aunique.captureTheFlag.teamsAndPlayers.CTFPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Random;
import org.joml.Vector3f;

public class PowerUp {


    public enum PowerUpType {
        TEAM_SPEED, //ger hela laget speed 1
        TEAM_SWORD_UPGRADE, // ger alla i laget bättre svärd
        TEAM_FLAG_PROTECTION, //går ej att ta deras flagga i typ 10 sek
        TEAM_ARMOR_UPGRADE, // alla får bättre armor en kort stund


    }

    private final Location location;
    private Boolean spawned;
    private PowerUpType powerUpType;
    private BukkitTask spawnTask;
    private Boolean randomizePowerUp;
    private int respawnInterval;

    private ItemDisplay itemDisplay;
    private ArmorStand powerUpTitle, powerUpSubtitle;

    public PowerUp(Location loc, PowerUpType powerUpType, Boolean respawnSelf, Boolean randomizePowerUp, int respawnInterval){
        this.location = loc;
        this.spawned = false;
        this.powerUpType = powerUpType;
        if(respawnSelf){
            startSpawningPowerUps();
        }else{
            spawnTask = null;
        }
        this.randomizePowerUp = randomizePowerUp;
        this.respawnInterval = respawnInterval;
        powerUpTitle = null;
        powerUpSubtitle = null;
        itemDisplay = null;
        powerUpTitle = null;
        powerUpSubtitle = null;
    }

    public void spawn(){
        if(spawned){
            return;
        }
        Random random = new Random();
        if(randomizePowerUp){
            this.powerUpType = PowerUpType.values()[random.nextInt(PowerUpType.values().length)]; // get random powerup from PowerupType enum
        }
        if(itemDisplay == null){
            itemDisplay = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);

            Transformation transformation = new Transformation(
                    new Vector3f(0.0f, 0.5f, 0.0f), // 0.5 over the ground
                    new Quaternionf().rotateY((float) Math.toRadians(90)), //rotate 90 degrees
                    new Vector3f(1.0f, 1.0f, 1.0f),
                    new Quaternionf()
            );
            itemDisplay.setTransformation(transformation);
        }
        if(powerUpTitle == null){
            powerUpTitle = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            powerUpTitle.setVisible(false);
            powerUpTitle.setInvulnerable(true);
            powerUpTitle.setGravity(false);
        }
        if(powerUpSubtitle == null){
            powerUpSubtitle = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            powerUpSubtitle.setVisible(false);
            powerUpSubtitle.setInvulnerable(true);
            powerUpSubtitle.setGravity(false);
        }


        switch (powerUpType) {
            case TEAM_SPEED -> {
                itemDisplay.setItemStack(new ItemStack(Material.FEATHER));
                powerUpTitle.customName(
                        Component.text()
                                .append(Component.text("a"))
                                .build()
                );
            }
            case TEAM_SWORD_UPGRADE -> {
                itemDisplay.setItemStack(new ItemStack(Material.IRON_SWORD));
            }
            case TEAM_FLAG_PROTECTION -> {
                itemDisplay.setItemStack(new ItemStack(Material.BARRIER));
            }
            case TEAM_ARMOR_UPGRADE -> {
                itemDisplay.setItemStack(new ItemStack(Material.IRON_CHESTPLATE));
            }
        }
    }

    public void takePowerUp(CTFPlayer takePlayer){
        if(!spawned){
            return;
        }
        switch (powerUpType) {
            case TEAM_SPEED -> {

            }
            case TEAM_SWORD_UPGRADE -> {

            }
            case TEAM_FLAG_PROTECTION -> {

            }
            case TEAM_ARMOR_UPGRADE -> {

            }
        }
        this.spawned = false;
    }

    public void setRespawnSelf(boolean respawnSelf) {
        if (respawnSelf) {
            startSpawningPowerUps();
        } else {
            stopSpawningPowerUps();
        }
    }

    public void startSpawningPowerUps() {
        if (spawnTask != null && !spawnTask.isCancelled()) {
            return; // Prevent duplicate tasks
        }

        spawnTask = Bukkit.getScheduler().runTaskTimer(CaptureTheFlag.getInstance(), this::spawn, respawnInterval, respawnInterval);
    }

    private void stopSpawningPowerUps() {
        if (spawnTask != null) {
            spawnTask.cancel();
            spawnTask = null;
        }
    }
}
