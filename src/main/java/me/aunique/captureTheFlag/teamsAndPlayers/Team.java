package me.aunique.captureTheFlag.teamsAndPlayers;

import me.aunique.captureTheFlag.ctf_modules.FlagEntity;
import me.aunique.captureTheFlag.ctf_modules.Game;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private final String name;
    private final TextColor color;
    private final ArrayList<CTFPlayer> players;
    private final Location spawnLocation;
    private FlagEntity flag;
    private int capturedFlags;
    private int totalKills;


    public Team(String name, TextColor color, Location loc){
        this.name = name;
        this.color = color;
        this.players = new ArrayList<>();
        this.capturedFlags = 0;
        this.totalKills = 0;
        this.spawnLocation = loc.clone();
        this.flag = null;
    }
    public void setFlag(FlagEntity flag){
        this.flag = flag;
    }

    public FlagEntity getFlag() {
        return flag;
    }

    public String getName(){
        return name;
    }

    public int getCapturedFlags() {
        return capturedFlags;
    }
    public Location getSpawn() { return this.spawnLocation; }
    public int getTotalKills() {
        return totalKills;
    }
    public void incrementFlag(){
        this.capturedFlags += 1;
        if (this.capturedFlags >= 6){
            Game.getInstance().gameEnd();
        }
    }
    public void incrementKills(){
        this.totalKills += 1;
    }

    public TextColor getColor() {return color; }
    public void addPlayer(CTFPlayer p){
        players.add(p);
    }
    public List<CTFPlayer> getPlayers(){
        return players;
    }


}
