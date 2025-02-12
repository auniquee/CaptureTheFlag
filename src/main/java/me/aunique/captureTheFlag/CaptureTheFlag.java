package me.aunique.captureTheFlag;

import me.aunique.captureTheFlag.commands.StartCommand;
import me.aunique.captureTheFlag.listeners.CaptureFlagListener;
import me.aunique.captureTheFlag.listeners.DeathHandler;
import me.aunique.captureTheFlag.listeners.GameRestrictions;
import me.aunique.captureTheFlag.listeners.powerUpListener;
import me.aunique.captureTheFlag.managers.ConfigManager;
import me.aunique.captureTheFlag.managers.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CaptureTheFlag extends JavaPlugin {

    private static CaptureTheFlag plugin;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        plugin = this;
/*
        if(!configFile.exists()){
            saveDefaultConfig();
            reloadConfig();
        }else {                             // VIKTIGT!! TA BORT INNAN RELEASE!!!
            saveResource("config.yml", true);
            System.out.println("Configuration updated.");
        }

 */
        File configFile = new File(getDataFolder(), "config.yml");
        ConfigManager.initialize(getConfig());
        gameManager = new GameManager(this);

        getCommand("ctf").setExecutor(new StartCommand(gameManager));
        getCommand("ctf").setExecutor(new StartCommand(gameManager));
        getServer().getPluginManager().registerEvents(new CaptureFlagListener(), this);
        getServer().getPluginManager().registerEvents(new GameRestrictions(), this);
        getServer().getPluginManager().registerEvents(new DeathHandler(), this);
        getServer().getPluginManager().registerEvents(new powerUpListener(), this);
        //getServer().getPluginManager().registerEvents(new CaptureFlagEventTrigger(), this);
    }

    @Override
    public void onDisable() {
        gameManager.endGame();
        // Plugin shutdown logic
    }
    public static CaptureTheFlag getInstance(){
        return plugin;
    }
}
