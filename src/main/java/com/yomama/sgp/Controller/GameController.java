package com.yomama.sgp.Controller;

import com.yomama.sgp.Enums.GameState;
import com.yomama.sgp.Enums.XPGivingCause;
import com.yomama.sgp.Helpers.Chat;
import com.yomama.sgp.Interfaces.MinigameObject;
import com.yomama.sgp.SGP;
import com.yomama.sgp.Tasks.GameStartCountdownTask;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.ezapi.chat.ChatMessage;
import org.ezapi.util.PlayerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {
    private GameState gameState = GameState.LOBBY;
    final private Location lobbyLocation = new Location(Bukkit.getWorld("Lobby"),  14,83,-7);
    public Location getLobbyLocation(){return lobbyLocation;}
    private GameStartCountdownTask gameStartCountdownTask;

    private List<Player> playersList = new ArrayList<>();
    private List<Player> deadPlayersList = new ArrayList<>();

    private List<Player> playersKarmaLiost = new ArrayList<>();
    public void PlayerSayGG(Player player){
        if(playersKarmaLiost.contains(player))return;

        GiveXPToPlayer(player,3, XPGivingCause.Karma);
        playersKarmaLiost.add(player);
    }

    private MinigameObject currentMinigame;
    public MinigameObject getCurrentMinigame(){
        return currentMinigame;
    }

    public GameController(){

    }

    public void setGameState(GameState gameState){
        if(this.gameState == GameState.ACTIVE && gameState == GameState.STARTING)return;
        this.gameState = gameState;
        ClearInventories();
        switch (gameState) {
            case ACTIVE -> {
                if (this.gameStartCountdownTask != null) this.gameStartCountdownTask.cancel();

                PickMinigame();
            }
            //give player the items or teleport
            case STARTING -> {
                Bukkit.broadcastMessage("Starting!");
                this.gameStartCountdownTask = new GameStartCountdownTask(this);
                this.gameStartCountdownTask.runTaskTimer(SGP.getInstance(), 0, 20);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Chat.Colored("&a---&eGame is starting!&a---"));
                    TextComponent textComponent = new TextComponent(Chat.Colored("&a&lClick Here"));
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/sg Join"));
                    textComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( Chat.Colored("&a&l&oClick Here &e&oto join the game.") ).create() ) );

                    ComponentBuilder componentBuilder = new ComponentBuilder();
                    componentBuilder.append(textComponent);
                    componentBuilder.append(Chat.Colored(" &eto join the game."));

                    player.spigot().sendMessage(componentBuilder.create());
                }
            }
            case LOBBY -> {
                Bukkit.broadcastMessage("Stopped!");
                if (this.gameStartCountdownTask != null) this.gameStartCountdownTask.cancel();
                Teleport(lobbyLocation);
            }
            case WON -> {
                GiveXPToPlayer(playersList.get(0),7,XPGivingCause.Win);
                ShowScore();

                Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(),()->{
                    ResetDeadPlayers();
                    ResetPlayers();
                    AddAllPlayers();
                    playersKarmaLiost.clear();
                    Teleport(lobbyLocation);
                },5*20);

            }
        }
    }

    private void ShowScore() {
        sendColoredMessage("&a&lWinner Winner Chicken Dinner!!!",true);
        sendColoredMessage("&a" + getPlayerByRank(1).getName() + " &ebrought the victory!!!",true);
        sendColoredMessage(" ",true);
        sendColoredMessage("&8--------------------",true);
        sendColoredMessage("&e[&a&lLeaderboard&e]",true);
        sendColoredMessage("&7#1 &b" + getPlayerByRank(1).getName(),true);

        sendColoredMessage("&7#2 &b" + getPlayerByRank(2).getName(),true);
        sendColoredMessage("&7#3 &b" + getPlayerByRank(3).getName(),true);

        sendColoredMessage(" ",true);

        sendColoredMessage("&e[&a&lYour Info&e]",true);
        for (Player player : getAllPlayers()) {
            sendColoredMessage("&eRank: &7#&a"+ getRankOfPlayer(player),player);
        }


        sendColoredMessage("&8--------------------",true);

    }
    private int getRankOfPlayer(Player player){
        Bukkit.getLogger().info(player.getName() + "(" + getAllPlayers().indexOf(player)+ " | #" +(getAllPlayers().indexOf(player) + 1));
       return getAllPlayers().indexOf(player) + 1;
    }
    private Player getPlayerByRank(int rank){
        return getAllPlayers().get(rank-1);
    }

    public void GiveXPToPlayer(Player player,int amount,XPGivingCause cause) {
        player.giveExp(amount);
        player.sendMessage(Chat.Colored("&8--------------------"));
        player.sendMessage(Chat.Colored("&a&l"+cause));
        player.sendMessage(Chat.Colored("&8[&a+&8] &d&l" + amount + " &5experience points."));
        player.sendMessage(Chat.Colored("&8--------------------"));
    }


    public GameState getGameState() {
        return gameState;
    }

    public boolean isActive(MinigameObject minigame){
        return currentMinigame != null && currentMinigame.equals(minigame)&&getGameState().equals(GameState.ACTIVE);
    }
    public boolean isActive(String minigame){
        return currentMinigame != null && currentMinigame.getName().equalsIgnoreCase(minigame) && getGameState().equals(GameState.ACTIVE);
    }
    public void sendColoredTitle(String text, String subtitle, int fadeIn, int stay, int fadeOut){
        for (Player player : playersList) {
            player.sendTitle(Chat.Colored(text),Chat.Colored(subtitle),20,70,20);
        }
    }
    public List<Player> getAllPlayers(){
        List<Player> pls = new ArrayList<>();
        pls.addAll(playersList);
        List<Player> deads = new ArrayList<>();
        deads.addAll(deadPlayersList);
        Collections.reverse(deads);
        pls.addAll(deads);

        return pls;
    }
    public void sendColoredMessage(String text,boolean includeDead){
        for (Player player : playersList) {
            sendColoredMessage(text, player);
        }
        if(includeDead){
            for (Player player : deadPlayersList) {
                sendColoredMessage(text, player);
            }
        }
    }
    public void sendColoredMessage(String text,Player player){
        player.sendMessage(Chat.Colored(text));
    }

    public void sendColoredActionBar(String text){
        for (Player player : playersList) {
           PlayerUtils.actionbar(new ChatMessage(Chat.Colored(text),false),player);
        }
    }
    public void Teleport(Location location){
        for (Player player : playersList) {
           player.teleport(location);
        }
    }
    private void PickMinigame() {
        sendColoredTitle("&6&lChoosing a minigame...","&6&l&kmsdfdssgdfgfdgdfgdsfgfsg",20,120,20);
        Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(), () -> {
            currentMinigame = MinigamesManager.getRandomMinigame();
            sendColoredTitle("&6&l"+currentMinigame.getName(),"&6"+currentMinigame.getDescription(),0,80,20);

            Teleport(currentMinigame.getStartingLocation());
            currentMinigame.onMinigameStart(this);
        }, 120L);
    }
    private void AddAllPlayers(){
        playersList.addAll(SGP.getInstance().getServer().getOnlinePlayers());
    }
    private void ResetPlayers(){
        playersList.clear();
    }
    public void AddDeadPlayer(Player p){
        if(playersList.size() > 1){
            playersList.remove(p);
            deadPlayersList.add(p);

            if(playersList.size() == 1){
                setGameState(GameState.WON);
            }
        }
    }
    public void AddPlayer(Player p){
        if(playersList.size() < 8){
            playersList.add(p);
        }
    }
    public boolean inPlayerList(Player p){
        return playersList.contains(p);
    }
    public boolean inDeadList(Player p){
        return deadPlayersList.contains(p);
    }

    public boolean inMinigame(Player p){
        return inPlayerList(p) || inDeadList(p) && getGameState().equals(GameState.ACTIVE);
    }
    private void ResetDeadPlayers(){
        deadPlayersList.clear();
    }
    public Player[] getPlayers(){
        Player[] playersArray = new Player[playersList.size()];
        playersList.toArray(playersArray);
        return playersArray;
    }
    public void cleanUp(){
        if(getCurrentMinigame()!=null){
            getCurrentMinigame().onMinigameStop(this);
        }
    }
    public void PlaySound(Sound sound){
        for (Player player : playersList) {
            player.playSound(player.getLocation(),sound,1,1);
        }
    }
    public void PlaySound(Sound sound,float volume,float pitch){
        for (Player player : playersList) {
            player.playSound(player.getLocation(),sound,volume,pitch);
        }
    }
    public void ClearInventories(){
        for (Player player : playersList) {
            player.getInventory().clear();
        }
    }
}
