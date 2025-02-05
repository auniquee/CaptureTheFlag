package me.aunique.captureTheFlag.ctf_modules;

import me.aunique.captureTheFlag.CaptureTheFlag;
import me.aunique.captureTheFlag.teamsAndPlayers.CTFPlayer;
import me.aunique.captureTheFlag.utils.powerUpHitbox;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Random;
import org.joml.Vector3f;

import java.util.Objects;

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
    private powerUpHitbox hitbox;

    private ItemDisplay itemDisplay;
    private TextDisplay powerUpTitle, powerUpSubtitle;

    public PowerUp(Location loc, PowerUpType powerUpType, Boolean respawnSelf, Boolean randomizePowerUp, int respawnInterval){
        this.location = loc;
        this.spawned = false;
        this.powerUpType = powerUpType;
        this.randomizePowerUp = randomizePowerUp;
        this.respawnInterval = respawnInterval;
        if(respawnSelf){
            startSpawningPowerUps();
        }else{
            spawnTask = null;
        }
        itemDisplay = null;
        powerUpTitle = null;
        powerUpSubtitle = null;
        hitbox = null;
    }

    public void spawn(){

        if(spawned){
            return;
        }
        spawned = true;
        Random random = new Random();
        if(randomizePowerUp){
            this.powerUpType = PowerUpType.values()[random.nextInt(PowerUpType.values().length)]; // get random powerup from PowerupType enum
        }
        if(itemDisplay == null){
            itemDisplay = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);

            Transformation transformation = new Transformation(
                    new Vector3f(0.0f, 0.8f, 0.0f), // 0.5 over the ground
                    new Quaternionf(), //new Quaternionf().rotateY((float) Math.toRadians(90)), //rotate 90 degrees
                    new Vector3f(1.0f, 1.0f, 1.0f),
                    new Quaternionf()
            );
            itemDisplay.setTransformation(transformation);

        }


        itemDisplay.setGlowColorOverride(Color.AQUA);

        if(powerUpTitle == null){
            location.add(0, 1.8, 0); // higher the text a bit
            powerUpTitle = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
            powerUpTitle.setBillboard(Display.Billboard.CENTER); //make it act like nametag
        }

        if(powerUpSubtitle == null){
            location.add(0, -0.3, 0);
            powerUpSubtitle = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
            location.add(0, -1.5, 0); // restore location
            powerUpSubtitle.setBillboard(Display.Billboard.CENTER); //make it act like nametag
        }

        if(hitbox.getHitbox() == null){
            location.add(0, 1, 0);
            hitbox.setHitbox(location, this.toString());
            location.add(0, -1, 0);
        }

        switch (powerUpType) {
            case TEAM_SPEED -> {
                itemDisplay.setItemStack(new ItemStack(Material.FEATHER));
                powerUpTitle.text(
                        Component.text()
                                .append(Component.text("TEAM SPEED", NamedTextColor.AQUA))
                                .build()
                );
                powerUpSubtitle.text(
                        Component.text()
                                .append(Component.text("30 sekunder", NamedTextColor.GRAY))
                                .build()
                );
            }


            case TEAM_SWORD_UPGRADE -> {
                itemDisplay.setItemStack(new ItemStack(Material.IRON_SWORD));
                powerUpTitle.text(
                        Component.text()
                                .append(Component.text("SWORD UPGRADE", NamedTextColor.AQUA))
                                .build()
                );
                powerUpSubtitle.text(
                        Component.text()
                                .append(Component.text("30 sekunder", NamedTextColor.GRAY))
                                .build()
                );
            }


            case TEAM_FLAG_PROTECTION -> {
                itemDisplay.setItemStack(new ItemStack(Material.BARRIER));
                powerUpTitle.text(
                        Component.text()
                                .append(Component.text("FLAG PROTECTION", NamedTextColor.AQUA))
                                .build()
                );
                powerUpSubtitle.text(
                        Component.text()
                                .append(Component.text("10 sekunder", NamedTextColor.GRAY))
                                .build()
                );
            }


            case TEAM_ARMOR_UPGRADE -> {
                itemDisplay.setItemStack(new ItemStack(Material.IRON_CHESTPLATE));
                powerUpTitle.text(
                        Component.text()
                                .append(Component.text("TEAM ARMOR UPGRADE", NamedTextColor.AQUA))
                                .build()
                );
                powerUpSubtitle.text(
                        Component.text()
                                .append(Component.text("30 sekunder", NamedTextColor.GRAY))
                                .build()
                );
            }
        }
    }

    public void takePowerUp(CTFPlayer takePlayer){
        if(!spawned){
            return;
        }

        itemDisplay.setItemStack(new ItemStack(Material.AIR)); //make in invis
        powerUpTitle.text(null);
        powerUpSubtitle.text(null);
        teamLoop:
        for (CTFPlayer teamPlayer : takePlayer.getPlayerTeam().getPlayers()){


            switch (powerUpType) {
                case TEAM_SPEED ->
                    teamPlayer.getPlayer().addPotionEffect(
                            new PotionEffect(PotionEffectType.SPEED, 30*20, 0, false, true)
                    );


                case TEAM_SWORD_UPGRADE -> {
                    if(teamPlayer.getPlayer().getInventory().contains(Material.STONE_SWORD)){
                        teamPlayer.getPlayer().getInventory().remove(Material.STONE_SWORD);
                        teamPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if(teamPlayer.getPlayer().getItemOnCursor().getType().equals(Material.IRON_SWORD)){
                                    teamPlayer.getPlayer().setItemOnCursor(new ItemStack(Material.AIR));
                                }
                                teamPlayer.getPlayer().getInventory().remove(Material.IRON_SWORD);
                                teamPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.STONE_SWORD));

                            }
                        }.runTaskLaterAsynchronously(CaptureTheFlag.getInstance(), 20*30); // 30 sek
                    }
                }


                case TEAM_FLAG_PROTECTION -> {
                    teamPlayer.getPlayerTeam().getFlag().setCurrentlyProtected(true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            teamPlayer.getPlayerTeam().getFlag().setCurrentlyProtected(false);

                        }
                    }.runTaskLaterAsynchronously(CaptureTheFlag.getInstance(), 20*7); // 7 sek
                    break teamLoop; //aja baja
                }


                case TEAM_ARMOR_UPGRADE -> {
                    if(Objects.requireNonNull(teamPlayer.getPlayer().getInventory().getChestplate()).getType().equals(Material.LEATHER_CHESTPLATE)){
                        ItemStack playerBoots = teamPlayer.getPlayer().getInventory().getBoots();
                        ItemStack playerLeggings = teamPlayer.getPlayer().getInventory().getLeggings();
                        teamPlayer.getPlayer().getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                        teamPlayer.getPlayer().getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                teamPlayer.getPlayer().getInventory().setLeggings(playerLeggings);
                                teamPlayer.getPlayer().getInventory().setBoots(playerBoots);

                            }
                        }.runTaskLaterAsynchronously(CaptureTheFlag.getInstance(), 20*30); // 30 sek
                    }
                }
            }
        }
        this.spawned = false;
    }

    public void setHitbox(powerUpHitbox hitbox) {
        this.hitbox = hitbox;
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
