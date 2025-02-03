package me.aunique.captureTheFlag.listeners;

import me.aunique.captureTheFlag.ctf_modules.Game;
import me.aunique.captureTheFlag.utils.powerUpHitbox;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;

import java.util.ArrayList;
import java.util.Set;

public class powerUpListener implements Listener {
    @EventHandler
    public void onPowerUpPickup(AreaEffectCloudApplyEvent hitboxCollisionEvent){
        if(hitboxCollisionEvent.getAffectedEntities().isEmpty()){
            return;
        }
        System.out.println("hello!!");
        hitboxCollisionEvent.setCancelled(true);
        Game gameInstance = Game.getInstance();

// Get the entity's scoreboard tags
        Set<String> entityTags = hitboxCollisionEvent.getEntity().getScoreboardTags();
        ArrayList<powerUpHitbox> hitboxes = gameInstance.getHitboxes();
        powerUpHitbox hitbox = null;
        for (powerUpHitbox box : hitboxes){
            if (entityTags.contains(box.getId())){
                hitbox = box;
            }
        }

        hitbox.deleteHitbox();
        hitbox.getPowerUp().takePowerUp(gameInstance.getCTFPlayer((Player) hitboxCollisionEvent.getAffectedEntities().getFirst()));
    }
}
