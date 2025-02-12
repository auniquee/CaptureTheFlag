package me.aunique.captureTheFlag.commands;

import me.aunique.captureTheFlag.ctf_modules.Game;
import me.aunique.captureTheFlag.managers.ConfigManager;
import me.aunique.captureTheFlag.managers.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.commands.Commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {

    private final GameManager gameManager;

    public StartCommand(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length < 2 || (args[0] != "end" && args[0] != "start" )){
            Component message = Component.text("Använding: /ctf [start/end] [map]")
                .color(TextColor.fromCSSHexString("#992020")
            );
            sender.sendMessage(message);
            return true;
        }
        if(args[0] == "start"){
            if (!ConfigManager.getInstance().getMaps().contains(args[1])){
                sender.sendMessage(Component.text("Ogilitig karta!", TextColor.fromCSSHexString("#992020")));
                return false;
            }

            gameManager.startGame(args[0]);
        }else{
            gameManager.endGame();
        }

        return true;

    }
}
