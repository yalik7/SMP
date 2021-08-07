package com.yomama.sgp.Listeners;

import com.yomama.sgp.Controller.GameController;
import com.yomama.sgp.Enums.GameState;
import com.yomama.sgp.SGP;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitTask;

public class ChatListener implements Listener {
    GameController controller;
    public ChatListener(GameController controller){
        this.controller = controller;
    }


    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if(controller.inMinigame(event.getPlayer()) && controller.getGameState().equals(GameState.WON)){
            String[] goodWords = {"gg","good game","good job","nice job"};
            for (String w : goodWords) {
                if(event.getMessage().toLowerCase().contains(w)){
                    Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(),()->{
                        controller.PlayerSayGG(event.getPlayer());
                    },10);
                }
            }

        }
    }
}
