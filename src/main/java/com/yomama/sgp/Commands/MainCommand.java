package com.yomama.sgp.Commands;

import com.yomama.sgp.Controller.GameController;
import com.yomama.sgp.Enums.GameState;
import com.yomama.sgp.Helpers.Chat;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private GameController gameManager;
    public MainCommand(GameController gameManager){
        this.gameManager = gameManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            ShowHelp(sender);
            return true;
        }
        switch (args[0].toLowerCase()){
            case "join":
                if(sender instanceof Player player){
                    if(!gameManager.inMinigame(player)){
                        gameManager.AddPlayer(player);

                        if(gameManager.inMinigame(player)){
                            gameManager.sendColoredMessage("&b"+player.getName()+" &e joined the game. (&a" + gameManager.getPlayers().length + "&e/&a8)",true);
                        }
                    }
                }
                break;
            default:
                ShowHelp(sender);
        }
        return true;
    }

    private void ShowHelp(CommandSender sender){
        sender.sendMessage(Chat.Colored("\n&6Small Games Commands\n&8------------------\n" +
                "&7/sg &bjoin &8- &7Join current minigame if already started" +".\n"
        ));
    }
}
