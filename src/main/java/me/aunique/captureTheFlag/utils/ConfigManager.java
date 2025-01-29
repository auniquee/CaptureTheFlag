package me.aunique.captureTheFlag.utils;

import me.aunique.captureTheFlag.teamsAndPlayers.Team;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ConfigManager {
    private static ConfigManager instance;
    private final FileConfiguration config;

    public ConfigManager (FileConfiguration config){
        this.config = config;
    }
    public static void initialize(FileConfiguration config) {
        if (instance == null) {
            instance = new ConfigManager(config);
        }
    }

    public static ConfigManager getInstance(){
        return instance;
    }

    public Map<String, String> getColors(String map){
        Map<String, String> colors = new HashMap<>();
        List<String> teams = config.getConfigurationSection("maps." + map + ".teams").getKeys(false).stream().toList();
        String code;
        for(String team : teams){
            code = config.getString("maps." + map + ".teams." + team +".color-code");
            colors.put(team, code);
        }
        return colors;
    }
    public Map<String, List<Float>> getSpawns(String map){
        Map<String, List<Float>> spawnPoints = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("maps."+map+".teams");
        if(section == null){
            System.err.println("No map with that name");
            return spawnPoints;
        }
        for(String team : section.getKeys(false)){
            //System.out.println("maps." + map + ".teams." + team + ".spawn-point");
            //section = config.getConfigurationSection("maps." + map + ".teams." + team + ".spawn-point");
            //System.out.println(section);
            spawnPoints.put(team, section.getFloatList(team + ".spawn-point"));
        }
        return spawnPoints;
    }
    public Map<String, List<Float>> getFlags(String map){
        Map<String, List<Float>> spawnPoints = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection("maps."+map+".teams");
        if(section == null){
            System.err.println("No map with that name");
            return spawnPoints;
        }
        for(String team : section.getKeys(false)){
            //System.out.println("maps." + map + ".teams." + team + ".spawn-point");
            //section = config.getConfigurationSection("maps." + map + ".teams." + team + ".spawn-point");
            //System.out.println(section);
            spawnPoints.put(team, section.getFloatList(team + ".flag-position"));
        }
        return spawnPoints;
    }
    public List<String> getTeams(String map){

        ArrayList<String> teams = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection("maps."+map+".teams");

        if(section == null){
            System.err.println("No map with that name");
            return teams;
        }

        teams.addAll(section.getKeys(false));
        return teams;
    }

    public ItemStack getTeamBlock(Team team, String map){
        ItemStack item;
        try {
            item = new ItemStack(
                    Objects.requireNonNull(Material.matchMaterial(
                            Objects.requireNonNull(config.getString("maps." + map +".teams."+ team.getName() + ".block-type"))
                    ))
            );
        } catch (NullPointerException e){
            item = new ItemStack(Material.WHITE_WOOL);
        }
        return item;
    }
    public ItemStack getTeamBanner(Team team, String map){
        ItemStack item;
        try {
            item = new ItemStack(
                    Objects.requireNonNull(Material.matchMaterial(
                            Objects.requireNonNull(config.getString("maps." + map +".teams."+ team.getName() + ".banner-type"))
                    ))
            );
        } catch (NullPointerException e){
            item = new ItemStack(Material.WHITE_BANNER); // uh oh not good!
            System.out.println("banner type for team: " + team.getName() + " not found! better check it out");
        }
        return item;
    }

    public ArrayList<ArrayList<Float>> getPowerups(String map){
        ArrayList<ArrayList<Float>> powerups = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection("maps." + map + ".powerup-locations");
        if(section == null){
            System.err.println("No map with that name");
            return powerups;
        }

        //config.getFloatList("maps." + map + ".powerup-locations");

        for(String powerup : section.getKeys(false)){
            //System.out.println("maps." + map + ".teams." + team + ".spawn-point");
            //section = config.getConfigurationSection("maps." + map + ".teams." + team + ".spawn-point");
            //System.out.println(section);
            powerups.add((ArrayList<Float>) section.getFloatList(powerup));




        }
        return powerups;
    }
    public String getWorld(String map){
        return config.getString("maps." + map + ".world");
    }
}
