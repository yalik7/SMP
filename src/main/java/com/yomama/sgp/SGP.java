package com.yomama.sgp;

import com.yomama.sgp.Commands.InvisibleItemFrame;
import com.yomama.sgp.Commands.MainAdminCommand;
import com.yomama.sgp.Commands.MainCommand;
import com.yomama.sgp.Commands.TabCompleters.MainAdminCommandTabCompleter;
import com.yomama.sgp.Commands.TabCompleters.MainCommandTabCompleter;
import com.yomama.sgp.Controller.GameController;
import com.yomama.sgp.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class SGP extends JavaPlugin {

    private static SGP instance;
    public static SGP getInstance() {
        return instance;
    }

    private GameController gameController;
    public GameController getGameController(){return gameController;}


    @Override
    public void onEnable() {
        instance = this;

        gameController = new GameController();

        registerEvents();
        registerCommands();
        registerTabCompleters();
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDieListener(gameController),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDropListener(gameController),this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(gameController),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(gameController),this);
        Bukkit.getServer().getPluginManager().registerEvents(new SignsListener(gameController),this);

    }
    private void registerCommands() {
        this.getCommand("invisibleitemframe").setExecutor(new InvisibleItemFrame());
        this.getCommand("smallgamesadmin").setExecutor(new MainAdminCommand(gameController));
        this.getCommand("smallgames").setExecutor(new MainCommand(gameController));
    }
    private void registerTabCompleters() {
        this.getCommand("smallgamesadmin").setTabCompleter(new MainAdminCommandTabCompleter());
        this.getCommand("smallgames").setTabCompleter(new MainCommandTabCompleter());
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        gameController.cleanUp();
    }
}
