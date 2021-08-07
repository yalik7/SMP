package com.yomama.sgp.Controller.Minigames;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.yomama.sgp.Controller.GameController;
import com.yomama.sgp.Interfaces.MinigameObject;
import com.yomama.sgp.Helpers.Chat;
import com.yomama.sgp.SGP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ezapi.module.npc.EzNPC;
import org.ezapi.module.npc.NPCType;
import org.ezapi.util.BuildingUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Platform {
    public Location point1;
    public Location point2;

    public Platform(Location point1, Location point2) {
        this.point1 = point1;
        this.point2 = point2;
    }
    public Platform(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.point1 = new Location(Bukkit.getWorld("RainbowMan"), x1, y1, z1);
        this.point2 = new Location(Bukkit.getWorld("RainbowMan"), x2, y2, z2);
    }

    public static List<Platform> getPlatforms() {
        List<Platform> platforms = new ArrayList<>();

        platforms.add(new Platform(12, 79, -3, -2, 79, 11));
        platforms.add(new Platform(12, 74, -3, -2, 74, 11));
        platforms.add(new Platform(12, 69, -3, -2, 69, 11));
        platforms.add(new Platform(12, 64, -3, -2, 64, 11));

        platforms.add(new Platform(-4, 74, -3, -18, 74, 11));
        platforms.add(new Platform(-4, 69, -3, -18, 69, 11));
        platforms.add(new Platform(-4, 64, -3, -18, 64, 11));

        platforms.add(new Platform(-4, 74, 13, -18, 74, 27));
        platforms.add(new Platform(-4, 69, 13, -18, 69, 27));
        platforms.add(new Platform(-4, 64, 13, -18, 64, 27));

        platforms.add(new Platform(12, 74, 13, -2, 74, 27));
        platforms.add(new Platform(12, 69, 13, -2, 69, 27));
        platforms.add(new Platform(12, 64, 13, -2, 64, 27));

        platforms.add(new Platform(12, 69, 29, -2, 69, 43));
        platforms.add(new Platform(12, 64, 29, -2, 64, 43));

        platforms.add(new Platform(-17, 64, 29, -4, 64, 43));
        platforms.add(new Platform(-17, 69, 29, -4, 69, 43));


        return platforms;
    }

    public static Platform getRandomPlatform() {
        return getPlatforms().get(new Random().nextInt(getPlatforms().size()));
    }

    public BlockVector3 getPoint1() {
        return BlockVector3.at(point1.getBlockX(), point1.getBlockY(), point1.getBlockZ());
    }

    public BlockVector3 getPoint2() {
        return BlockVector3.at(point2.getBlockX(), point2.getBlockY(), point2.getBlockZ());
    }
}
@SuppressWarnings("ALL")
public class RainbowMan implements MinigameObject {

    @Override
    public String getName() {
        return "RainbowMan";
    }

    @Override
    public String getDescription() {
        return "Dance to best and colorfull party";
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.GLOWSTONE;
    }

    @Override
    public Location getStartingLocation() {
        return new Location(Bukkit.getWorld("RainbowMan"),19 ,65, -29);
    }

    @Override
    public Location getDyingLocation() {
        return new Location(getStartingLocation().getWorld(), 38,71,-28,90.7f,12.0f);
    }

    @Override
    public boolean playerBecomeSpectatorOnDeath() {
        return false;
    }

    @Override
    public void onMinigameStart(GameController manager) {
        round=1;
        destroyTime = 6;
        gameController = manager;


        Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(), this::PickColor, 10*20L);

    }

    @Override
    public void onMinigameStop(GameController manager) {
        round=0;
    }

    @Override
    public boolean isPlayerDieParmemantly() {
        return true;
    }

    @Override
    public String getDeathMessage(){
        return "{PLAYER} crashed all their bones while dancing on the colorful floor.";
    }


    private GameController gameController;
    public int round = 0;
    public long destroyTime = 6;

    private Platform getMainFloorPlatform(){
        return new Platform(26 ,63, -21,12 ,63, -35);
    }
    private void PickColor(){
        if(!gameController.isActive(this))return;


       var randomColor = getRandomMaterial();
        gameController.PlaySound(Sound.BLOCK_NOTE_BLOCK_BASS,1,1.5f);
        gameController.PlaySound(Sound.BLOCK_NOTE_BLOCK_BASS,1,1.5f);
        gameController.PlaySound(Sound.BLOCK_NOTE_BLOCK_BASS,1,1.5f);


        gameController.sendColoredActionBar("&l&6Chosen block is: " + getChatColorOfBlock(getColorOfMaterial(randomColor)).toUpperCase());
        FillInventoryWith(randomColor);


        DestroyNonColoredBlocks(getColorOfMaterial(randomColor),randomColor,5*20,4*20,3*20);
    }
    private void FillInventoryWith(Material blockType){
        gameController.ClearInventories();

        for (Player player : gameController.getPlayers()) {
            ItemStack item = new ItemStack(blockType);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            meta.setLore(Arrays.asList(new String[]{
                    Chat.Colored("&7Run to this blockk!!!!"),Chat.Colored("&7&lNOWWWWWWWWWWW!!!!")
            }));

            item.setItemMeta(meta);
            player.getInventory().setItem(3,item);
            player.getInventory().setItem(4,item);
            player.getInventory().setItem(5,item);

        }
    }
    private Material getRandomMaterial(){
        List<Material> materials = new ArrayList<>();
        for (int x = Math.min(getMainFloorPlatform().point1.getBlockX(),getMainFloorPlatform().point2.getBlockX()); x < Math.max(getMainFloorPlatform().point1.getBlockX(),getMainFloorPlatform().point2.getBlockX()); x++) {
            for (int z = Math.min(getMainFloorPlatform().point1.getBlockZ(),getMainFloorPlatform().point2.getBlockZ()); z < Math.max(getMainFloorPlatform().point1.getBlockZ(),getMainFloorPlatform().point2.getBlockZ()); z++) {
                var block = getMainFloorPlatform().point1.getWorld().getBlockAt(x,getMainFloorPlatform().point1.getBlockY(),z);
                if(!materials.contains(block.getType()))
                    materials.add(block.getType());
            }
        }
        return materials.get(new Random().nextInt(materials.size()));
    }
    private String getColorOfBlock(Block block){
        String[] colors = {"white","orange","magenta","light_blue","yellow","lime","pink","gray","light_gray","cyan","purple","blue","brown","green","red","black","white"};
        String blockName = block.getType().name().toLowerCase().replace("concrete"," ").replace("terracotta","").replace(" ","_");

        for (String color : colors) {
            if (blockName.contains(color)) {
                return color;
            }
        }
        return "";
    }
    private String getColorOfMaterial(Material material){
        String[] colors = {"white","orange","magenta","light_blue","yellow","lime","pink","gray","light_gray","cyan","purple","blue","brown","green","red","black","white"};
        String blockName = material.name().toLowerCase().replace("concrete"," ").replace("terracotta","").replace(" ","_");

        for (String color : colors) {
            if (blockName.contains(color)) {
                return color;
            }
        }
        return "";
    }
    private String getChatColorOfBlock(String color) {
        return switch (color) {
            case "orange", "brown" -> ChatColor.translateAlternateColorCodes('&', "&6") + color;
            case "magenta", "purple" ->ChatColor.translateAlternateColorCodes('&', "&5") + color;
            case "light_blue", "cyan" -> ChatColor.translateAlternateColorCodes('&', "&b") + color;
            case "yellow" -> ChatColor.translateAlternateColorCodes('&', "&e") + color;
            case "lime" -> ChatColor.translateAlternateColorCodes('&', "&a") + color;
            case "gray" -> ChatColor.translateAlternateColorCodes('&', "&8") + color;
            case "light_gray" -> ChatColor.translateAlternateColorCodes('&', "&7") + color;
            case "blue" -> ChatColor.translateAlternateColorCodes('&', "&9") + color;
            case "green" -> ChatColor.translateAlternateColorCodes('&', "&2") + color;
            case "red" -> ChatColor.translateAlternateColorCodes('&', "&4") + color;
            case "black" -> ChatColor.translateAlternateColorCodes('&', "&0") + color;
            case "white" -> ChatColor.translateAlternateColorCodes('&', "&f") + color;
            case "pink" -> ChatColor.translateAlternateColorCodes('&', "&d") + color;
            default -> ChatColor.translateAlternateColorCodes('&', "&f") + color;
        };
    }



    private void DestroyNonColoredBlocks(String color,Material currentBlockType,long destroyDelay,long resetDelay,long loopDelay){
        Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(), () -> {
            gameController.ClearInventories();
            List<Block> coloredBlocks = new ArrayList<>();
            List<Block> nonColoredBlocks = new ArrayList<>();
            HashMap<Location, Material> savedBlocks = new HashMap<>();

            for (Block block : blocksFromTwoPoints(getMainFloorPlatform().point1, getMainFloorPlatform().point2)) {
                savedBlocks.put(block.getLocation(), block.getType());
                if (getColorOfBlock(block).equals(color)) {
                    coloredBlocks.add(block);
                } else {
                    nonColoredBlocks.add(block);
                }
            }

            BuildingUtils.fill(getMainFloorPlatform().point1, getMainFloorPlatform().point2, Material.AIR);
            gameController.PlaySound(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE);
            coloredBlocks.forEach(block -> block.setType(currentBlockType));


            Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(), () -> {
                BuildRandomPlatform();
//                savedBlocks.forEach((k, v) -> {
//                    k.getBlock().setType(v);
//                });

                Bukkit.getScheduler().scheduleSyncDelayedTask(SGP.getInstance(), () -> {
                    round += 1;
                    if(destroyDelay > 1.5){
                        destroyTime -= 0.5;
                    }
                    PickColor();
                }, loopDelay);
            }, resetDelay);
        },destroyTime*20);

    }
    private void BuildRandomPlatform(){
        Platform platform = Platform.getRandomPlatform();
        CuboidRegion region = new CuboidRegion(
                BlockVector3.at(
                        platform.point1.getBlockX(),platform.point1.getBlockY(),platform.point1.getBlockZ()
                ),
                BlockVector3.at(
                    platform.point2.getBlockX(),platform.point2.getBlockY(),platform.point2.getBlockZ()
                )
        );
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);


        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(Bukkit.getWorld("RainbowMan")),-1)) {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    editSession, region, clipboard, region.getMinimumPoint()
            );
            // configure here
            Operations.complete(forwardExtentCopy);
        }
        catch (WorldEditException e){
            e.printStackTrace();
        }

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(Bukkit.getWorld("RainbowMan")),-1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(getMainFloorPlatform().getPoint2())
                    // configure here
                    .build();
            Operations.complete(operation);
        }
        catch (WorldEditException e){
            e.printStackTrace();
        }
    }
    public List<Block> blocksFromTwoPoints(Location loc1, Location loc2){
        List<Block> blocks = new ArrayList<>();

        for(int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x <= Math.max(loc1.getBlockX(), loc2.getBlockX()); x++)
        {
            for(int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z <= Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++)
            {
                for(int y = Math.min(loc1.getBlockY(), loc2.getBlockY()); y <= Math.max(loc1.getBlockY(), loc2.getBlockY()); y++)
                {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }
    public Location getRandomLocation(Location loc1, Location loc2) {
        Preconditions.checkArgument(loc1.getWorld() == loc2.getWorld());
        double minX = Math.min(loc1.getX(), loc2.getX());
        double minY = Math.min(loc1.getY(), loc2.getY());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());

        double maxX = Math.max(loc1.getX(), loc2.getX());
        double maxY = Math.max(loc1.getY(), loc2.getY());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());

        return new Location(loc1.getWorld(), randomDouble(minX, maxX), randomDouble(minY, maxY), randomDouble(minZ, maxZ));
    }
    public double randomDouble(double min, double max) {
        return min + ThreadLocalRandom.current().nextDouble(Math.abs(max - min + 1));
    }


}
