package me.aunique.captureTheFlag.events;

import me.aunique.captureTheFlag.ctf_modules.FlagEntity;
import me.aunique.captureTheFlag.teamsAndPlayers.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CaptureFlagEvent extends Event {


    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Team flagTeam;

    private final FlagEntity flag;
    public CaptureFlagEvent(Player player, FlagEntity flag){
        this.flagTeam = flag.getTeam();
        this.player = player;
        this.flag = flag;
    }
    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return flagTeam;
    }

    public FlagEntity getFlag() { return flag; }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
