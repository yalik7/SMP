package com.yomama.sgp.Commands.TabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MainAdminCommandTabCompleter implements TabCompleter {
    private List<String> getArguments(){
        List<String> arguments = new ArrayList<>();
        arguments.add("Start");
        arguments.add("Stop");
        arguments.add("QuickRestart");
        arguments.add("Restart");
        arguments.add("QuickStart");



        return arguments;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if(args.length == 1){
          getArguments().forEach(a->{
              if(a.toLowerCase().startsWith(args[0].toLowerCase()))
                  result.add(a);
          });
          return result;
        }
        return null;
    }
}
