package me.aunique.captureTheFlag.ctf_modules;

import net.kyori.adventure.translation.Translatable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Random;
import org.joml.Vector3f;

public class Powerup {

    Location location;
    Boolean spawned;

    public Powerup(Location loc){
        this.location = loc;
        this.spawned = false;
    }

    public void spawn(){

        ItemDisplay item = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);

        Transformation transformation = new Transformation(
                new Vector3f(0.0f, 0.5f, 0.0f), // 0.5 over the ground
                new Quaternionf().rotateY((float) Math.toRadians(90)), //rotate 90 degrees
                new Vector3f(1.0f, 1.0f, 1.0f),
                new Quaternionf()
        );
        item.setTransformation(transformation);

        Random random = new Random();

        item.setItemStack(new ItemStack(Material.LEATHER_BOOTS));


    }
}
