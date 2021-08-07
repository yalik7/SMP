package com.yomama.sgp.Tasks;

import com.yomama.sgp.Enums.GameState;
import com.yomama.sgp.Controller.GameController;
import com.yomama.sgp.Helpers.Chat;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.ezapi.chat.ChatMessage;
import org.ezapi.util.PlayerUtils;

import java.util.Arrays;

public class GameStartCountdownTask extends BukkitRunnable {
    private final GameController gameManager;

    public GameStartCountdownTask(GameController gameManager) {
        this.gameManager = gameManager;
    }
    private int timeLeft = 20;
    @Override
    public void run() {
        timeLeft--;
        if(timeLeft <=0){
            cancel();
            gameManager.setGameState(GameState.ACTIVE);
            return;
        }
        Arrays.stream(gameManager.getPlayers()).forEach(p -> {

            ChatMessage message = new ChatMessage(Chat.Colored("&aStarting in: &e" + timeLeft), false);
            if(timeLeft < 4)
            {
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                PlayerUtils.actionbar(new ChatMessage(Chat.Colored("" + timeLeft), false));
                PlayerUtils.title(message, p);
            }else{
                PlayerUtils.actionbar(message,p);
            }
        });

    }
}
