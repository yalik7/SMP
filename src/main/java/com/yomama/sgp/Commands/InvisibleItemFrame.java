package com.yomama.sgp.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvisibleItemFrame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
          if(args.length == 0){
            player.performCommand("minecraft:give @s item_frame{EntityTag:{Invisible:1}}");
          }else if(args.length == 1){
            player.performCommand("minecraft:give @s item_frame{EntityTag:{Invisible:1,Item:{id:"+ args[0] +",Count:1b}}}");
          }
        }
        return true;
    }
}
