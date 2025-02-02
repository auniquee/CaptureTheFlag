package me.aunique.captureTheFlag.listeners;

import me.aunique.captureTheFlag.events.CaptureFlagEvent;
import me.aunique.captureTheFlag.teamsAndPlayers.CTFPlayer;
import me.aunique.captureTheFlag.teamsAndPlayers.Team;
import me.aunique.captureTheFlag.utils.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import me.aunique.captureTheFlag.ctf_modules.Game;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;


public class CaptureFlagListener implements Listener {



    @EventHandler
    public void onFlagCapture(CaptureFlagEvent captureFlagEvent){

        ConfigManager config = ConfigManager.getInstance();
        Team stolenTeam = captureFlagEvent.getTeam();
        CTFPlayer capturePlayer = Game.getInstance().getCTFPlayer(captureFlagEvent.getPlayer());
        Component message;


        // custom message, capturer ska inte få 2 rader, om egen flagga tagen ska texten vara röd etc

        // Om spelare har klickat på sin egen flagga
        if(capturePlayer.getPlayerTeam().equals(stolenTeam)){
            if (capturePlayer.getHoldingFlagTeam() != null) { // Om spelaren håller på en flagga så ska den bli captured
                Bukkit.getServer().broadcast(
                        Component.text()
                                .append(Component.text(capturePlayer.getPlayer().getName() + " ", capturePlayer.getPlayerTeam().getColor()))
                                .append(Component.text("har fångatagit ", NamedTextColor.GRAY))
                                .append(Component.text(capturePlayer.getHoldingFlagTeam().getName().toUpperCase() + "S ", capturePlayer.getHoldingFlagTeam().getColor()))
                                .append(Component.text("flagga!", NamedTextColor.GRAY))
                                .build()
                );
                capturePlayer.captureFlag(); // change player stats (set holdingteam = false), remove effects, and restore helmet
            }else if(capturePlayer.getPlayer().getGameMode() != GameMode.SPECTATOR){
                captureFlagEvent.getPlayer().sendMessage(Component.text("Du kan inte ta din egen flagga!", NamedTextColor.RED));
            }


        }
        // Om spelaren klickar på en flagga men har redan tagit en annan flagga
        else if (capturePlayer.getHoldingFlagTeam() != null) {
            captureFlagEvent.getPlayer().sendMessage(Component.text("Du har redan tagit en flagga!", NamedTextColor.RED));
        }
        // Om spelaren klickar på en flagga för att ta den
        else if (capturePlayer.getPlayer().getGameMode() != GameMode.SPECTATOR && !stolenTeam.getFlag().getCaptured() && !stolenTeam.getFlag().getCurrentlyProtected()){ // not spectator so that you cant take the flag when died
            stolenTeam.getFlag().takeFlag();
            ArrayList<CTFPlayer> players = Game.getInstance().getAllPlayers();
            for(CTFPlayer player : players){

                //Message för spelaren vars flagga blir tagen
                if(player.getPlayerTeam() == stolenTeam){
                    message = Component.text()
                            .append(Component.text("Din flagga har blivit stulen av ", NamedTextColor.RED))
                            .append(Component.text(capturePlayer.getPlayer().getName() + "!", capturePlayer.getPlayerTeam().getColor()))
                            .append(Component.text("!", NamedTextColor.RED))
                            .build();
                    player.getPlayer().playSound(capturePlayer.getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, .3F, .8F);
                    player.getPlayer().sendMessage(message);
                }

                // Message till spelaren som har tagit en flagga
                else if(player == capturePlayer) {
                    player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, -1, 1, true, true));
                    player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 1, true, false));
                    message = Component.text()
                            .append(Component.text("Du har tagit ", NamedTextColor.GRAY))
                            .append(Component.text(stolenTeam.getName().toUpperCase() + "S", stolenTeam.getColor()))
                            .append(Component.text(" flagga!", NamedTextColor.GRAY))
                            .build();
                    player.getPlayer().sendMessage(message);

                    player.setHoldingFlagTeam(stolenTeam);
                    //player.addMynt(1);

                    ItemStack banner = config.getTeamBanner(stolenTeam, Game.getInstance().getMap());
                    capturePlayer.getPlayer().getInventory().setHelmet(banner);
                }

                // message till resten av spelet när en flagga tas
                else {
                    message = Component.text()
                            .append(Component.text(capturePlayer.getPlayer().getName(), capturePlayer.getPlayerTeam().getColor()))
                            .append(Component.text(" Har tagit ", NamedTextColor.GRAY))
                            .append(Component.text(stolenTeam.getName().toUpperCase() + "S ", stolenTeam.getColor()))
                            .append(Component.text("flagga!", NamedTextColor.GRAY))
                            .build();
                    player.getPlayer().sendMessage(message);
                }
            }

        // om lagets flagga redan är tagen
        }else if(stolenTeam.getFlag().getCurrentlyProtected()){
            capturePlayer.getPlayer().sendMessage(Component.text("Denna flagga är skyddad just nu!", NamedTextColor.RED));
        }
        else if(capturePlayer.getPlayer().getGameMode() != GameMode.SPECTATOR && stolenTeam.getFlag().getCaptured()){
            capturePlayer.getPlayer().sendMessage(Component.text("Denna flagga är redan tagen!", NamedTextColor.RED));
        }
    }
}
