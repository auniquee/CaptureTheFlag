package me.aunique.captureTheFlag.listeners;

import me.aunique.captureTheFlag.CaptureTheFlag;
import me.aunique.captureTheFlag.ctf_modules.Game;
import me.aunique.captureTheFlag.teamsAndPlayers.CTFPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

public class DeathHandler implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent playerDeathEvent){

        Game game = Game.getInstance();
        playerDeathEvent.setCancelled(true);
        playerDeathEvent.getPlayer().setGameMode(GameMode.SPECTATOR);

        final int countdownTime = 5;
        CTFPlayer killedPlayer = game.getCTFPlayer(playerDeathEvent.getPlayer());
        new BukkitRunnable() {
            int timeLeft = countdownTime;
            @Override
            public void run() {
                if (timeLeft > 0) {
                    playerDeathEvent.getPlayer().showTitle(Title.title(
                            Component.text("DU DOG!", NamedTextColor.RED),
                            Component.text("Återupplivas om: ", NamedTextColor.GRAY)
                                    .append(Component.text(timeLeft, NamedTextColor.RED)),
                            Title.Times.times(
                                    Duration.ZERO, Duration.ofSeconds(2), Duration.ZERO
                            )
                    ));
                    timeLeft--;
                } else {
                    if(killedPlayer.getHoldingFlagTeam() != null){
                        killedPlayer.getHoldingFlagTeam().getFlag().restoreFlag();
                    }
                    killedPlayer.playerDie();

                    playerDeathEvent.getPlayer().spigot().respawn();
                    playerDeathEvent.getPlayer().teleport(killedPlayer.getPlayerTeam().getSpawn());
                    playerDeathEvent.getPlayer().setGameMode(GameMode.ADVENTURE);
                    cancel();
                }
            }
        }.runTaskTimer(CaptureTheFlag.getInstance(), 0L, 20L);

    }
}
