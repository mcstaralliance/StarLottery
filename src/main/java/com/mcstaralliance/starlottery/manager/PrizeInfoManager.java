package com.mcstaralliance.starlottery.manager;

import com.mcstaralliance.starlottery.gui.Panel;
import com.mcstaralliance.starlottery.gui.PrizeManageMenu;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PrizeInfoManager {
    private ItemStack itemStack;
    private boolean notice;
    private int odds;

    public PrizeInfoManager(ItemStack itemStack, int odds, boolean notice) {
        this.itemStack = itemStack;
        this.odds = odds;
        this.notice = notice;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getOdds() {
        return odds;
    }

    public void setOdds(int odds) {
        this.odds = odds;
    }

    public boolean isNotice() {
        return notice;
    }

    public void setNotice(boolean notice) {
        this.notice = notice;
    }

    public String toString() {
        return "PrizeInfo [itemStack=" + this.itemStack + ", odds=" + this.odds + ", notice=" + this.notice + "]";
    }
    public static World getChestWorld() {
        return Bukkit.getWorld("BetterLotteryChest");
    }

    public static void setOdds(Player player, int odds) {
        String playerName = player.getName();
        int rawSlot = LotteryManager.getSettingOddsOps().get(playerName);
        FileConfiguration config = ConfigManager.load("BetterLottery");
        String key = LotteryManager.getSettingOps().get(player.getName()).getKey();
        if (odds < 0) {
            player.sendMessage("[§aBetterLotteryReload§f] §c设置失败，必须输入正整数，可以是0");
        }
        List<Integer> oddsList = config.getIntegerList(key + ".odds");
        oddsList.set(rawSlot, odds);
        config.set(key + ".odds", oddsList);
        ConfigManager.save(config, "BetterLottery");
        player.sendMessage("[§aBetterLotteryReload§f] §e奖品几率设置为：§f" + odds);
        player.sendMessage("");
        LotteryManager.getSettingOddsOps().remove(playerName);
        new PrizeManageMenu(player, key);
    }
}