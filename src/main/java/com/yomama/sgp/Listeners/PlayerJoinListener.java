package com.yomama.sgp.Listeners;

import com.yomama.sgp.Controller.GameController;
import com.yomama.sgp.Helpers.Chat;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoinListener implements Listener {
    private final GameController controller;

    public PlayerJoinListener( GameController controller){
        this.controller = controller;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location location = new Location(
                event.getPlayer().getServer().getWorld("Lobby"),
                14,83,-7
        );
        event.getPlayer().teleport(location);
        player.sendTitle(Chat.Colored("&6Welcome!"),Chat.Colored("&e"+player.getName()), 20,3*20, 20);
        player.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
        player.setFlying(false);
        player.getInventory().clear();
    }
}
