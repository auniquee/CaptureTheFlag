package me.aunique.captureTheFlag.teamsAndPlayers;

import org.bukkit.entity.Player;

public class CTFPlayer{
    final private Player player;
    private int mynt;
    private int kills;
    private int deaths;
    private Team team;
    private Team flag;
    private int captures;

    public CTFPlayer(Player player, Team team){
        this.player = player;
        this.mynt  = 0;
        this.deaths= 0;
        this.kills= 0;
        this.team = team;
        this.flag = null;
        this.captures = 0;
    }
    public Team getFlag(){ return this.flag; }
    public void setFlag(Team team){ this.flag = team; }
    public Player getPlayer(){
        return this.player;
    }
    public void captureFlag(){
        if (flag != null){
            this.captures++;
            this.team.incrementFlag();
        }
    }
    public Team getTeam(){
        return this.team;
    }
    public void setTeam(Team team) { this.team = team; }
    public int getMynt() { return this.mynt; }
    public void addMynt(int amount){
        this.mynt += amount;
    }
    public void setMynt(int amount){
        this.mynt = amount;
    }
    public void incKills(){
        this.kills++;
    }
    public void incDeaths(){
        this.deaths++;
    }

}
