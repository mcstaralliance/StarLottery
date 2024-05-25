package com.mcstaralliance.starlottery.util;

import com.mcstaralliance.starlottery.manager.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.Action;

public abstract class StarLotteryUtil {
    public static boolean isRightClick(Action action) {
        return action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
    }

    public static boolean isAvailablePool(String poolName) {
        // 可用奖池指: 1. 奖池存在 2. 奖池已开启
        FileConfiguration config = ConfigManager.load("BetterLottery");
        return config.contains(poolName) && config.getBoolean(poolName + ".enable") ;
    }
}
