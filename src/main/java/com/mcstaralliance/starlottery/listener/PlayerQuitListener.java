package com.mcstaralliance.starlottery.listener;

import com.mcstaralliance.starlottery.manager.LotteryManager;
import com.mcstaralliance.starlottery.manager.PlayerLotteryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (LotteryManager.getLotteryPlayers().containsKey(player.getName())) {
            PlayerLotteryManager playerLotteryManager = LotteryManager.getLotteryPlayers().get(player.getName());
            if (playerLotteryManager.isRun()) {
                playerLotteryManager.setAnimationEnded(true);
                playerLotteryManager.ended();
            }
        }
    }
}
