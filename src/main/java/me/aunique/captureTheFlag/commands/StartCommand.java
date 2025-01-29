package me.aunique.captureTheFlag.commands;

import me.aunique.captureTheFlag.ctf_modules.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.minimessage.tag.standard.StandardTags.color;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length < 1){
            Component message = Component.text("usage: /ctf [map]")
                .color(TextColor.fromCSSHexString("#992020")
            );
            sender.sendMessage(message);
            return true;
        }


        Game game = new Game(args[0]);

        game.initiateGame();
        game.startGame(false);

        return true;

    }
}
