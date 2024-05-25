package com.mcstaralliance.starlottery.util;

import com.mcstaralliance.starlottery.manager.ConfigManager;
import com.mcstaralliance.starlottery.manager.LotteryManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PoolCreationUtil {

    public static void createPool(Player player, String msg) {
        String message = ChatColor.translateAlternateColorCodes('&', msg);
        String playerName = player.getName();
        String opMessage = LotteryManager.getCreatingOps().get(playerName);
        if (opMessage == null) {
            LotteryManager.getCreatingOps().put(playerName, message);
            player.sendMessage("[§aBetterLotteryReload§f] §e奖池名称设置为：§f" + message);
            player.sendMessage("");
            player.sendMessage("[§aBetterLotteryReload§f] §e第二步>>>请在聊天框中输入抽奖券名称§c(颜色符&,名称不可以重复)");
            return;
        }
        String message3 = "&f" + message;
        player.sendMessage("[§aBetterLotteryReload§f] §e抽奖券名称设置为：§f" + message3);
        player.sendMessage("");
        FileConfiguration config = ConfigManager.load("BetterLottery");
        if (config.contains(message3.replaceAll("§", "&"))) {
            player.sendMessage("[§aBetterLotteryReload§f] §c" + message3 + "§e>>>创建失败，抽奖券名称已经被占用，请重新点击创建按钮进行创建");
        } else {
            player.sendMessage("[§aBetterLotteryReload§f] §c" + opMessage + "§e>>>创建完成，请点击抽奖池设置按钮进行配置");
            player.sendMessage("");
            config.set(message3.replaceAll("§", "&") + ".title", opMessage.replaceAll("§", "&"));
            config.set(message3.replaceAll("§", "&") + ".enable", false);
            ConfigManager.save(config, "BetterLottery");
        }
    }

}
