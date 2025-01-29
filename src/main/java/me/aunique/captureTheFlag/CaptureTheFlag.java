package me.aunique.captureTheFlag;

import me.aunique.captureTheFlag.commands.StartCommand;
import me.aunique.captureTheFlag.listeners.CaptureFlagListener;
import me.aunique.captureTheFlag.listeners.DeathHandler;
import me.aunique.captureTheFlag.listeners.GameRestrictions;
import me.aunique.captureTheFlag.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CaptureTheFlag extends JavaPlugin {
    private static CaptureTheFlag plugin;
    @Override
    public void onEnable() {
        plugin = this;
        File configFile = new File(getDataFolder(), "config.yml");

        if(!configFile.exists()){
            saveDefaultConfig();
            reloadConfig();
        }else {                             // VIKTIGT!! TA BORT INNAN RELEASE!!!
            saveResource("config.yml", true);
            System.out.println("Configuration updated.");
        }
        ConfigManager.initialize(getConfig());

        getCommand("ctf").setExecutor(new StartCommand());
        getServer().getPluginManager().registerEvents(new CaptureFlagListener(), this);
        getServer().getPluginManager().registerEvents(new GameRestrictions(), this);
        getServer().getPluginManager().registerEvents(new DeathHandler(), this);
        //getServer().getPluginManager().registerEvents(new CaptureFlagEventTrigger(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static CaptureTheFlag getInstance(){
        return plugin;
    }
}
