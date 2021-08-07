package com.yomama.sgp.Listeners;

import com.yomama.sgp.Controller.GameController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropListener implements Listener {
    private GameController controller;
    public  PlayerDropListener(GameController controller){
        this.controller = controller;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(controller.inMinigame(event.getPlayer()))
            event.setCancelled(true);
    }
}
