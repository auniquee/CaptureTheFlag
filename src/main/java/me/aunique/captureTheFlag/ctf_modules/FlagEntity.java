package me.aunique.captureTheFlag.ctf_modules;

import me.aunique.captureTheFlag.teamsAndPlayers.Team;
import me.aunique.captureTheFlag.managers.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;


public class FlagEntity {
    private final Location flagLocation;
    private Team team;
    private Boolean captured;
    private ArmorStand title;
    private ArmorStand subTitle;
    private BlockDisplay bannerDisplay;
    private Interaction hitbox;
    private final String banner;
    private Boolean currentlyProtected;


    public FlagEntity(Location flagLocation, String banner){
        this.flagLocation = flagLocation.clone();
        this.team = null;
        this.title = null;
        this.banner = banner;
        this.bannerDisplay = null;
        this.subTitle = null;
        this.captured = false;
        this.currentlyProtected = false;
        this.hitbox = null;
    }

    public Boolean getCurrentlyProtected() { return currentlyProtected; }

    public void setCurrentlyProtected(boolean currentlyProtected) { this.currentlyProtected = currentlyProtected; }

    public void setTeam(Team team){
        this.team = team;
    }

    public Team getTeam() { return team; }

    public void spawn(){
        title = (ArmorStand) flagLocation.getWorld().spawnEntity(flagLocation, EntityType.ARMOR_STAND);
        Component customName = Component.text(team.getName().toUpperCase() + "S FLAGGA", team.getColor(), TextDecoration.BOLD);
        title.setCustomNameVisible(true);
        title.setGravity(false);
        title.setVisible(false);
        title.customName(customName);
        //title.setBillboard(Display.Billboard.CENTER);


        flagLocation.add(0, -0.3, 0);
        subTitle = (ArmorStand) flagLocation.getWorld().spawnEntity(flagLocation, EntityType.ARMOR_STAND);
        flagLocation.add(0, 0.3, 0);

        Component sub = Component.text("(högerklicka)", NamedTextColor.GRAY);
        subTitle.setCustomNameVisible(true);
        subTitle.setGravity(false);
        subTitle.setVisible(false);
        subTitle.customName(customName);
        subTitle.customName(sub);

        hitbox = (Interaction) flagLocation.getWorld().spawnEntity(flagLocation, EntityType.INTERACTION);
        hitbox.setInteractionHeight(3);
        hitbox.setInteractionWidth(2);

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
            title.setCustomNameVisible(false);
            subTitle.customName(Component.text("Flaggan är tagen!", NamedTextColor.RED));
        }
    }
    public void restoreFlag(){
        if(this.captured) {
            this.captured = false;
            bannerDisplay.setBlock(ConfigManager.getInstance().getTeamBanner(team, Game.getInstance().getMap()).getType().createBlockData());
            title.setCustomNameVisible(true);
            subTitle.customName(Component.text("(högerklicka)", NamedTextColor.GRAY));
        }
    }
    public Boolean getCaptured(){
        return this.captured;
    }

    public Entity getInteractionEntity(){
        if(title == null){
            System.out.println("Hitbox hasn't been spawned yet!");
            return null;
        }

        return hitbox;

    }
    public List<Entity> getEntities(){
        return List.of(title, subTitle, bannerDisplay, hitbox);
    }

}
