package com.mcstaralliance.starlottery.task;

import com.mcstaralliance.starlottery.manager.LotteryManager;
import com.mcstaralliance.starlottery.manager.PlayerLotteryManager;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimationTask extends BukkitRunnable {
    @Override
    public void run() {
        for (PlayerLotteryManager manager : LotteryManager.getLotteryPlayers().values()) {
            manager.update();
        }
    }
}
