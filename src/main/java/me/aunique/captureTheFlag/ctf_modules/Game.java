package me.aunique.captureTheFlag.ctf_modules;

import me.aunique.captureTheFlag.CaptureTheFlag;
import me.aunique.captureTheFlag.events.CaptureFlagEventTrigger;
import me.aunique.captureTheFlag.teamsAndPlayers.CTFPlayer;
import me.aunique.captureTheFlag.teamsAndPlayers.Team;
import me.aunique.captureTheFlag.utils.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final Map<String, Team> teams;
    private static ArrayList<CTFPlayer> players = new ArrayList<>();
    private boolean isRunning = false;
    private final String map;
    private final List<FlagEntity> flags;
    private ArrayList<PowerUp> powerUps;
    private static Game instance;

    public Game(String map){
        this.teams = new HashMap<>();
        players = new ArrayList<>();
        this.isRunning = false;
        this.map = map;
        this.powerUps = new ArrayList<>();
        this.flags = new ArrayList<>();
    }
    public static ConfigManager config = ConfigManager.getInstance();

    public static Game getInstance(){
        return instance;
    }
/*
    public void addPlayer(Player p) {
        players.add(new CTFPlayer(p,new Team("", NamedTextColor.GREEN)));
    }
    */
    public CTFPlayer getCTFPlayer(Player p){
        return players.stream()
                .filter(CTFPlayer -> p.equals(CTFPlayer.getPlayer()))
                .findAny()
                .orElse(null);
    }
    public static ArrayList<CTFPlayer> getAllPlayers(){
        return players;
    }
    public void setPlayerTeam(Player p, String team){
        teams.get(team).addPlayer(getCTFPlayer(p));
    }
    public String getMap() { return this.map; }
    public void addTeam(Team team) {
        teams.put(team.getName(), team);
    }

    public Map<String, Team> getTeams(){
        return this.teams;
    }

    public FlagEntity getFlagByEntity(Entity entity){
        for (FlagEntity flag : flags){
            if(flag.getEntity().equals(entity)){
                return flag;
            }
        }
        return null;
    }

    public void initiateGame(){
        if(map.isEmpty()){ // ändra till map logik
            System.out.println("Ogiltig map");
            return;
        }
        instance = this;
        List<String> teamNames = config.getTeams(map);
        Map<String, String> teamColors = config.getColors(map);
        Map<String, List<Float>> flagLocations = config.getFlags(map);
        World world = Bukkit.getWorld(config.getWorld(map));
        ArrayList<FlagEntity> flagEntities = new ArrayList<>();
        Location spawnLocation;
        for (String teamName : teamNames) {
            Map<String, List<Float>> spawnPoints = config.getSpawns(map);
            spawnLocation = new Location(
                    Bukkit.getWorld(config.getWorld(map)),
                    spawnPoints.get(teamName).get(0),
                    spawnPoints.get(teamName).get(1),
                    spawnPoints.get(teamName).get(2),
                    spawnPoints.get(teamName).get(3),
                    spawnPoints.get(teamName).get(4)
            );

            Location flagLocation = new Location(
                    world,
                    flagLocations.get(teamName).get(0),
                    flagLocations.get(teamName).get(1),
                    flagLocations.get(teamName).get(2),
                    flagLocations.get(teamName).get(3),
                    flagLocations.get(teamName).get(4)
            );
            Team currentTeam = new Team(teamName, TextColor.fromHexString("#" + teamColors.get(teamName)), spawnLocation);
            FlagEntity currentFlag = new FlagEntity(flagLocation, config.getTeamBanner(currentTeam, map).getType().name());
            currentTeam.setFlag(currentFlag);
            currentFlag.setTeam(currentTeam);
            currentFlag.spawn();
            teams.put(teamName, currentTeam);
            flags.add(currentFlag);

        }
        Bukkit.getServer().getPluginManager().registerEvents(new CaptureFlagEventTrigger(
                flags.stream().map(FlagEntity::getEntity).collect(Collectors.toList())),
                CaptureTheFlag.getInstance()
        );

        int i = 0;
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(onlinePlayers);
        for(Player p : onlinePlayers){ //team assignment currently not supporting party
            CTFPlayer newPlayer = new CTFPlayer(p,teams.get(teamNames.get(i%4))); //ONLY SUPPORTS TEAMS OF 4 EASY FIX!!!
            players.add(newPlayer);
            teams.get(teamNames.get(i%4)).addPlayer(players.getLast());
            i++;
        }
        //POWERUPS
        for (ArrayList<Float> coords : config.getPowerUps(map)) {
            Location loc = new Location(world, coords.get(0), coords.get(1), coords.get(2), coords.get(3), 0);
            powerUps.add(new PowerUp(loc.clone(), null, true, true, 20*3)); // 3 sec
            powerUps.getLast().spawn();
        }
        /*



        Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        Objective objective;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(getAllPlayers().toString()).setScore(0);
        for(CTFPlayer player : players){
            try{
                objective = board.registerNewObjective("capture_the_flag", "dummy", Component.text("Capture the Flag", NamedTextColor.GREEN));

            }catch (IllegalArgumentException e){
                objective =  board.getObjective("capture_the_flag").;
            }
            //board.
            //player.getPlayer();
        }

 */

    }

    public void startGame(boolean kaos){
        if(isRunning){
            return;
        }
        if(players.isEmpty()){
            System.out.println("Game hasn't been initiated!");
        }

        //List<String> teamNames = config.getTeams(map);
        Map<String, String> codes = config.getColors(map);
        for(Team team : teams.values() ){

            for(CTFPlayer initializedPlayer : team.getPlayers()){
                Component teamMessage = Component.text()
                        .append(Component.text(initializedPlayer.getPlayerTeam().getName())
                        .color(TextColor.fromHexString("#" + codes.get(initializedPlayer.getPlayerTeam().getName()))))
                        .build();
                Component message = Component.text()
                        .append(Component.text("Du är lag ", NamedTextColor.GRAY))
                        .append(Component.text(initializedPlayer.getPlayerTeam().getName(), initializedPlayer.getPlayerTeam().getColor()))
                        .build();
                initializedPlayer.getPlayer().teleport(team.getSpawn());
                initializedPlayer.getPlayer().sendMessage(message);
                ItemStack[] armor = {
                    new ItemStack(Material.LEATHER_BOOTS),
                    new ItemStack(Material.LEATHER_LEGGINGS),
                    new ItemStack(Material.LEATHER_CHESTPLATE),
                    new ItemStack(Material.LEATHER_HELMET)
                };

                for(ItemStack piece : armor){

                    LeatherArmorMeta meta = (LeatherArmorMeta) piece.getItemMeta();

                    meta.setColor(
                            Color.fromRGB(
                                    Integer.parseInt(team.getColor().asHexString().substring(1), 16))
                    );
                    piece.setItemMeta(meta);
                    piece.addItemFlags(ItemFlag.HIDE_DYE);

                }

                initializedPlayer.getPlayer().getInventory().setArmorContents(armor);
                initializedPlayer.getPlayer().getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
                initializedPlayer.getPlayer().getInventory().setItem(1, config.getTeamBlock(team, map).asQuantity(64));

            }
        }
        isRunning = true;
    }

    public void gameEnd(){

    }
}