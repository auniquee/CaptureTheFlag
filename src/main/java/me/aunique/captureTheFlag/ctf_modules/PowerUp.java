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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
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
    private final Boolean randomizePowerUp;
    private final int respawnInterval;

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
            powerUpTitle.setCustomNameVisible(true);
            powerUpTitle.setVisible(false);
            powerUpTitle.setInvulnerable(true);
            powerUpTitle.setGravity(false);
        }
        if(powerUpSubtitle == null){
            location.add(0, -0.3, 0);
            powerUpSubtitle = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            powerUpSubtitle.setCustomNameVisible(true);
            powerUpSubtitle.setVisible(false);
            powerUpSubtitle.setInvulnerable(true);
            powerUpSubtitle.setGravity(false);
            location.add(0, 0.3, 0);
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
                powerUpTitle.customName(
                        Component.text()
                                .append(Component.text("a"))
                                .build()
                );
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
        for (Player teamPlayer : takePlayer.getTeam().getPlayers().stream().map(CTFPlayer::getPlayer).toList()){


            switch (powerUpType) {
                case TEAM_SPEED -> {
                    teamPlayer.addPotionEffect(
                            new PotionEffect(PotionEffectType.SPEED, 30, 0, false, true)
                    );
                }
                case TEAM_SWORD_UPGRADE -> {
                    if(teamPlayer.getInventory().contains(Material.WOODEN_SWORD)){
                        teamPlayer.getInventory().remove(Material.WOODEN_SWORD);
                        teamPlayer.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                teamPlayer.getInventory().remove(Material.IRON_SWORD);
                                teamPlayer.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));

                            }
                        }.runTaskLaterAsynchronously(CaptureTheFlag.getInstance(), 20*30); // 30 sek
                    }
                }
                case TEAM_FLAG_PROTECTION -> {

                }
                case TEAM_ARMOR_UPGRADE -> {
                    if(teamPlayer.getInventory().contains(Material.LEATHER_CHESTPLATE)){
                        teamPlayer.getInventory().remove(Material.LEATHER_CHESTPLATE);
                        teamPlayer.getInventory().addItem(new ItemStack(Material.IRON_CHESTPLATE));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                teamPlayer.getInventory().remove(Material.IRON_CHESTPLATE);
                                teamPlayer.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE));

                            }
                        }.runTaskLaterAsynchronously(CaptureTheFlag.getInstance(), 20*30); // 30 sek
                    }
                }
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
