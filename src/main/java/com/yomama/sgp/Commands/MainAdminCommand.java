package com.yomama.sgp.Commands;

import com.yomama.sgp.Controller.GameController;
import com.yomama.sgp.Enums.GameState;
import com.yomama.sgp.Helpers.Chat;
import com.yomama.sgp.SGP;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainAdminCommand implements CommandExecutor {

    private GameController gameManager;
    public MainAdminCommand(GameController gameManager){
        this.gameManager = gameManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            ShowHelp(sender);
            return true;
        }
        switch (args[0].toLowerCase()){
            case "start":
                gameManager.setGameState(GameState.STARTING);
                break;
            case "quickstart":
                gameManager.setGameState(GameState.ACTIVE);
                break;
            case "stop":
                gameManager.setGameState(GameState.LOBBY);
                break;
            case "restart":
                gameManager.setGameState(GameState.LOBBY);
                Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(),()->{
                    gameManager.setGameState(GameState.STARTING);
                },10L);
                break;
            case "quickrestart":
                gameManager.setGameState(GameState.LOBBY);
                Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(),()->{
                    gameManager.setGameState(GameState.ACTIVE);
                },10L);
                break;
            default:
                ShowHelp(sender);
        }
        return true;
    }


    private void ShowHelp(CommandSender sender){
        sender.sendMessage(Chat.Colored("\n&6Small Games Admin Commands\n&8------------------\n" +
                "&7/sga &bStart &8- &7Starting the game"+"\n" +
                "&7/sga &bStop &8- &7Stopping the game" +
                "&7/sga &bRestart &8- &7Restarting the game"
        ));
    }
}
