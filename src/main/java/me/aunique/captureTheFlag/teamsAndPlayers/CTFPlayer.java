package me.aunique.captureTheFlag.teamsAndPlayers;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class CTFPlayer{
    final private Player player;
    private int mynt;
    private int kills;
    private int deaths;
    private Team playerTeam;
    private Team holdingFlagTeam;
    private int captures;

    public CTFPlayer(Player player, Team team){
        this.player = player;
        this.mynt  = 0;
        this.deaths= 0;
        this.kills= 0;
        this.playerTeam = team;
        this.holdingFlagTeam = null;
        this.captures = 0;
    }

    public Team getHoldingFlagTeam(){ return this.holdingFlagTeam; }

    public void setHoldingFlagTeam(Team team){ this.holdingFlagTeam = team; }

    public void captureFlag() {
        if (holdingFlagTeam != null) { // Om spelaren håller på en flagga så ska den bli captured
            holdingFlagTeam.getFlag().captureFlag(); // respawn flag
            playerTeam.incrementFlag();
            mynt += 100;
            removeFlag();
        }
    }
    public void removeFlag(){
        player.clearActivePotionEffects();
        holdingFlagTeam = null;
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
        meta.setColor(Color.fromRGB(
                Integer.parseInt(playerTeam.getColor().asHexString().substring(1), 16))
        );
        helmet.setItemMeta(meta);
        player.getInventory().setHelmet(helmet);
    }
    public Player getPlayer(){
        return this.player;
    }

    public Team getPlayerTeam(){
        return this.playerTeam;
    }

    public void setPlayerTeam(Team playerTeam) { this.playerTeam = playerTeam; }

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
