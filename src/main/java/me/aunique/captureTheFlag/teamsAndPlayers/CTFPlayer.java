package me.aunique.captureTheFlag.teamsAndPlayers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

public class CTFPlayer{
    final private Player player;
    private int mynt;
    private int kills;
    private int deaths;
    private Team playerTeam;
    private Team holdingFlagTeam;
    private int captures;
    private int killStreak;

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
        if (this.holdingFlagTeam != null) { // Om spelaren håller på en flagga så ska den bli captured

            this.holdingFlagTeam.getFlag().restoreFlag(); // respawn flag
            player.setHealth(player.getHealth() + 4);
            playerDie();
            this.playerTeam.incrementFlag();
            this.mynt += 100;
            this.captures++;
        }
    }

    public int getCaptures() {
        return captures;
    }

    public int getKills() {
        return kills;
    }

    public void playerDie(){
        if(this.holdingFlagTeam != null){
            player.removePotionEffect(PotionEffectType.GLOWING);
            player.removePotionEffect(PotionEffectType.SLOWNESS); //removing effects this way to avoid removing swiftness
        }else{ //in case of death
            this.killStreak = 0;
            this.deaths++;
        }

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
        this.killStreak++;
        this.kills++;
        if(killStreak % 5 == 0){
            Bukkit.getServer().broadcast(
                    Component.text()
                            .append(Component.text(this.player.getName(), this.playerTeam.getColor()))
                            .append(Component.text(" har en killstreak på ", NamedTextColor.GRAY))
                            .append(Component.text(this.killStreak, NamedTextColor.RED))
                            .build()
            );
        }
    }

}
