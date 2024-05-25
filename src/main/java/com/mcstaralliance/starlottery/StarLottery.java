package com.mcstaralliance.starlottery;

import com.mcstaralliance.starlottery.command.StarLotteryCommand;
import com.mcstaralliance.starlottery.listener.*;
import com.mcstaralliance.starlottery.manager.ConfigManager;
import com.mcstaralliance.starlottery.task.AnimationTask;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

public class StarLottery extends JavaPlugin {
    private static StarLottery instance;

    public static StarLottery getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        initConfig();
        initWorld();
        initEvent();
        initCommand();
    }

    private void initConfig() {
        ConfigManager.rootPath = getDataFolder().getPath();
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }

    private void initCommand() {
        Bukkit.getPluginCommand("sl").setExecutor(new StarLotteryCommand());
    }

    public void initWorld() {
        WorldCreator creator = new WorldCreator("BetterLotteryChest");
        creator.generateStructures(false);
        creator.environment(Environment.NORMAL);
        creator.type(WorldType.FLAT);
        World world = creator.createWorld();
        if (world != null) {
            world.setSpawnLocation(0, 5, 0);
            world.save();
        }
        Bukkit.createWorld(creator);
    }

    private void initEvent() {
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new EntityPickupItemListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPortalListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        AnimationTask animationTask = new AnimationTask();
        animationTask.runTaskTimer(this, 0, 1);
    }

}