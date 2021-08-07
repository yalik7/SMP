package com.yomama.sgp.Listeners;

import com.yomama.sgp.Controller.GameController;
import com.yomama.sgp.Enums.GameState;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class SignsListener implements Listener {
    private GameController gameController;
    public SignsListener(GameController gameController){
        this.gameController = gameController;
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(getLobbyTeleports().containsKey(event.getClickedBlock().getLocation())){
                TeleportPlayer(event.getPlayer(),getLobbyTeleports().get(event.getClickedBlock().getLocation()));
            }


            //Start game sign
            if(event.getClickedBlock().getLocation().equals(lobbyLocation(14, 83, -17))){
               if(gameController.getGameState().equals(GameState.LOBBY)){
                   gameController.setGameState(GameState.STARTING);
                   event.getPlayer().performCommand("sg Join");
               }else{
                   TextComponent message = new TextComponent("Click me");
                   message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sg join"));
               }

            }
        }
    }

    private Location lobbyLocation(double x, double y, double z, float yaw,float pitch){
        return new Location(Bukkit.getWorld("Lobby"),x,y,z,yaw,pitch);
    }
    private Location lobbyLocation(double x, double y, double z){
        return new Location(Bukkit.getWorld("Lobby"),x,y,z);
    }

    private void TeleportPlayer(Player player,Location location){
        player.teleport(location);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
    }

    public HashMap<Location,Location> getLobbyTeleports(){
        HashMap<Location,Location> teleports = new HashMap<>();
        teleports.put(lobbyLocation(2,83,-7,0,0),lobbyLocation(14,83,-7,90,0.5f));
        teleports.put(lobbyLocation(4,83,-7,0,0),lobbyLocation(2,83,-7,90.5f,6.5f));
        return teleports;
    }
}
