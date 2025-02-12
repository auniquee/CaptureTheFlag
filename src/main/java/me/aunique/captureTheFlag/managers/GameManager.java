package me.aunique.captureTheFlag.managers;

import me.aunique.captureTheFlag.ctf_modules.Game;
import org.bukkit.plugin.Plugin;

public class GameManager {

    private Game gameInstance;
    private Plugin plugin;
    public GameManager(Plugin plugin){
        this.plugin = plugin;
    }

    public void startGame(String map){
        if(this.gameInstance != null){
            plugin.getLogger().warning("Game already started!");
            return;
        }
        this.gameInstance = new Game(map);
        gameInstance.initiateGame();
        gameInstance.startGame(false);

    }

    public void endGame(){
        if(this.gameInstance == null){
            plugin.getLogger().warning("No game found!");
            return;
        }
        this.gameInstance.gameEnd();
        this.gameInstance = null;
    }

}
