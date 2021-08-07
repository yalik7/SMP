package com.yomama.sgp.Controller;

import com.yomama.sgp.Controller.Minigames.RainbowMan;
import com.yomama.sgp.Interfaces.MinigameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinigamesManager {
    public static MinigameObject[] getMinigames(){
        List<MinigameObject> minigamesList = new ArrayList<>();
        minigamesList.add( new RainbowMan());

        MinigameObject[] minigamesArray = new MinigameObject[minigamesList.size()];
        minigamesList.toArray(minigamesArray);
        return minigamesArray;
    }
    public static MinigameObject getRandomMinigame(){
        return getMinigames()[new Random().nextInt(getMinigames().length)];
    }
}
