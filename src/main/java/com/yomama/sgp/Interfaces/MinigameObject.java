package com.yomama.sgp.Interfaces;

import com.yomama.sgp.Controller.GameController;
import com.yomama.sgp.Helpers.Chat;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public interface MinigameObject {
    public String getName();
    public String getDescription();
    public Material getDisplayMaterial();
    public Location getStartingLocation();
    public Location getDyingLocation();
    public boolean playerBecomeSpectatorOnDeath();


    public void onMinigameStart(GameController manager);
    public void onMinigameStop(GameController manager);

    public default ItemStack getDisplayItem(){
        ItemStack item = new ItemStack(getDisplayMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Chat.Colored("&b"+getName()));
        List<String> lore = new ArrayList<>();
        lore.add(Chat.Colored("&3"+getDescription()));
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public boolean isPlayerDieParmemantly();

    public default String getDeathMessage(){
        return "{PLAYER} has died.";
    }

    public default void onPlayerDie(Player player, GameController manager){
        manager.AddDeadPlayer(player);
    }
    public default void onPlayerRespawn(Player player, GameController manager){
        if(playerBecomeSpectatorOnDeath()){
            player.teleport(getStartingLocation());
            player.setGameMode(GameMode.SPECTATOR);
        }
        else{
            player.teleport(getDyingLocation());
        }
    }

}

