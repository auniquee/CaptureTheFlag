package me.aunique.captureTheFlag.ctf_modules;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import me.aunique.captureTheFlag.teamsAndPlayers.CTFPlayer;
import me.aunique.captureTheFlag.teamsAndPlayers.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

public class CTFScoreboardManager {
    private Map<CTFPlayer, Scoreboard> scoreboards;
    static CTFScoreboardManager instance;

    public CTFScoreboardManager(){
        this.scoreboards = new HashMap<>();
        instance = this;
    }

    public void initiateScoreboard(){
        //iterate over all players
        for(CTFPlayer player : Game.getInstance().getAllPlayers()){
            if(scoreboards.isEmpty()){
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                scoreboards.put(player, scoreboard);
                player.getPlayer().setScoreboard(scoreboard);


            }
               //player.getPlayer().setScoreboard(); //
            //Scoreboard scoreboard = Bukkit.getScoreboardManager().get

        }
    }
public void updateScoreboard() {
        int score = 100;
        for(CTFPlayer player : Game.getInstance().getAllPlayers()) {
            Scoreboard scoreboard = scoreboards.get(player);
            Objective playerObjective = scoreboard. registerNewObjective(player.getPlayer().getName(), Criteria.DUMMY,
                    Component.text()
                            .append(Component.text("Capture the flag", NamedTextColor.DARK_GREEN))
                            .build()
            );
            playerObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

            playerObjective.numberFormat(NumberFormat.blank());

            Score space = playerObjective.getScore("space");
            space.customName(Component.empty());
            space.setScore(score--);


            Score teamLabel = playerObjective.getScore("teamLabel");
            teamLabel.customName(Component.text()
                    .append(Component.text("▪ Ditt lag: ", NamedTextColor.WHITE))
                    .append(Component.text(player.getPlayerTeam().getName(), player.getPlayerTeam().getColor()))
                    .build()
            );
            teamLabel.setScore(score--);
            space = playerObjective.getScore("space1");
            space.customName(Component.empty());
            space.setScore(score--);
            Score teamsTitle = playerObjective.getScore("teamsTitle");
            teamsTitle.customName(Component.text()
                    .append(Component.text("Ställning", NamedTextColor.GREEN))
                    .build()
            );
            teamsTitle.setScore(score--);


            for(Team team : Game.getInstance().getTeams().values()){
                Score tempTeam = playerObjective.getScore("team"+ team.getName());
                tempTeam.customName(Component.text()
                        .append(Component.text(team.getName().toUpperCase(), team.getColor()))
                        .append(Component.text("  " + team.getCapturedFlags(), NamedTextColor.WHITE))
                        .build());
                tempTeam.setScore(score--);
            }

            space = playerObjective.getScore("space2");
            space.customName(Component.empty());
            space.setScore(score--);

            Score playerTitle = playerObjective.getScore("playerTitle");
            playerTitle.customName(Component.text()
                    .append(Component.text("Din statistik", NamedTextColor.GREEN))
                    .build());
            playerTitle.setScore(score--);

            Score captures = playerObjective.getScore("captures");
            captures.customName(Component.text()
                    .append(Component.text("▪ Captures: ", NamedTextColor.WHITE))
                    .append(Component.text(player.getCaptures(), NamedTextColor.GREEN))
                    .build()
            );
            captures.setScore(score--);

            Score playerName = playerObjective.getScore("kills");
            playerName.customName(Component.text()
                    .append(Component.text("▪ Mord: ", NamedTextColor.WHITE))
                    .append(Component.text(player.getKills(), NamedTextColor.GREEN))
                    .build()
            );
            playerName.setScore(score--);
        }
        //hello

}
    public static CTFScoreboardManager getInstance() {
        return instance;
    }


}
