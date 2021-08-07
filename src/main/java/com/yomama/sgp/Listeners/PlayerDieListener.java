package com.yomama.sgp.Listeners;

import com.yomama.sgp.Controller.GameController;
import com.yomama.sgp.Enums.GameState;
import com.yomama.sgp.Helpers.Chat;
import com.yomama.sgp.SGP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Arrays;
import java.util.List;

public class PlayerDieListener implements Listener {
    private final GameController controller;

    public PlayerDieListener(GameController controller){
        this.controller = controller;
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(controller.getCurrentMinigame()!=null){
            if(controller.inMinigame(event.getEntity())){

                String[] words = controller.getCurrentMinigame().getDeathMessage().split(" ");
                for (int i = 0; i < words.length; i++) {
                    if(words[i].contains("{PLAYER}")) {
                        words[i] = words[i].replace("{PLAYER}","&b" + event.getEntity().getName());
                    }
                    words[i] = "&e"+words[i];
                }

                event.setDeathMessage(ChatColor.translateAlternateColorCodes('&',String.join(" ", Arrays.asList(words))));

                Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(),()->{
                    controller.getCurrentMinigame().onPlayerDie(event.getEntity(),controller);
                },10L);
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (controller.getCurrentMinigame() != null) {
            if (controller.inMinigame(event.getPlayer())) {

                Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(), () -> {
                    controller.getCurrentMinigame().onPlayerRespawn(event.getPlayer(),controller);
                }, 10L);
            }
        }
    }
}
