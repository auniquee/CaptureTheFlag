package me.aunique.captureTheFlag.ctf_modules;

import me.aunique.captureTheFlag.teamsAndPlayers.Team;
import me.aunique.captureTheFlag.utils.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.Objects;


public class FlagEntity {
    private final Location flagLocation;
    private Team team;
    private Boolean captured;
    private ArmorStand armorStand;
    private ArmorStand subTitle;
    private BlockDisplay bannerDisplay;
    private final String banner;
    private Boolean currentlyProtected;


    public FlagEntity(Location flagLocation, String banner){
        this.flagLocation = flagLocation.clone();
        this.team = null;
        this.armorStand = null;
        this.banner = banner;
        this.bannerDisplay = null;
        this.subTitle = null;
        this.captured = false;
        this.currentlyProtected = false;
    }

    public Boolean getCurrentlyProtected() { return currentlyProtected; }

    public void setCurrentlyProtected(boolean currentlyProtected) { this.currentlyProtected = currentlyProtected; }

    public void setTeam(Team team){
        this.team = team;
    }

    public Team getTeam() { return team; }

    public void spawn(){
        armorStand = (ArmorStand) flagLocation.getWorld().spawnEntity(flagLocation, EntityType.ARMOR_STAND);
        Component customName = Component.text(team.getName().toUpperCase() + "S FLAGGA", team.getColor(), TextDecoration.BOLD);
        armorStand.customName(customName);
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        flagLocation.add(0, -0.3, 0);

        subTitle = (ArmorStand) flagLocation.getWorld().spawnEntity(flagLocation, EntityType.ARMOR_STAND);
        Component sub = Component.text("(högerklicka)", NamedTextColor.GRAY);
        subTitle.customName(sub);
        subTitle.setCustomNameVisible(true);
        subTitle.setVisible(false);
        subTitle.setInvulnerable(true);
        subTitle.setGravity(false);
        flagLocation.add(0, 0.3, 0);


        flagLocation.setPitch(0);
        Transformation transformation = new Transformation(
                new Vector3f(-0.5f, 0, -0.5f),
                new org.joml.Quaternionf(),   // no clue what this is doing
                new Vector3f(1f, 1f, 1f),
                new org.joml.Quaternionf()
        );
        bannerDisplay = (BlockDisplay) flagLocation.getWorld().spawnEntity(flagLocation, EntityType.BLOCK_DISPLAY);
        bannerDisplay.setTransformation(transformation);
        try{
            bannerDisplay.setBlock(Bukkit.createBlockData(Objects.requireNonNull(Material.matchMaterial(banner))));
        }catch(NullPointerException e){
            bannerDisplay.setBlock(Bukkit.createBlockData(Material.WHITE_BANNER));
        }
    }

    public void takeFlag(){
        if(!this.captured){
            this.captured = true;
            bannerDisplay.setBlock(Material.BARRIER.createBlockData());
            armorStand.setCustomNameVisible(false);
            subTitle.customName(Component.text("Flaggan är tagen!", NamedTextColor.RED));
        }
    }
    public void captureFlag(){
        if(this.captured) {
            this.captured = false;
            bannerDisplay.setBlock(ConfigManager.getInstance().getTeamBanner(team, Game.getInstance().getMap()).getType().createBlockData());
            armorStand.setCustomNameVisible(true);
            subTitle.customName(Component.text("(högerklicka)", NamedTextColor.GRAY));
        }
    }
    public Boolean getCaptured(){
        return this.captured;
    }

    public Entity getEntity(){
        if(armorStand == null){
            System.out.println("ArmorStand hasn't been spawned yet!");
            return null;
        }

        return (Entity) armorStand;

    }

}
