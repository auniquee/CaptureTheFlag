package me.aunique.captureTheFlag.events;

import me.aunique.captureTheFlag.ctf_modules.Game;
import me.aunique.captureTheFlag.teamsAndPlayers.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.List;

public class CaptureFlagEventTrigger implements Listener {
    private final List<Entity> flagEntities;
    public CaptureFlagEventTrigger(List<Entity> flagEntities){
        this.flagEntities = flagEntities;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent e){
        if(flagEntities.contains(e.getRightClicked())){
            Bukkit.getServer()
                    .getPluginManager()
                    .callEvent(new CaptureFlagEvent(
                            e.getPlayer(),
                            Game.getInstance().getFlagByEntity(e.getRightClicked())
                    )
            );
        }
    }
}
