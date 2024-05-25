package com.mcstaralliance.starlottery.listener;

import com.mcstaralliance.starlottery.manager.LotteryManager;
import com.mcstaralliance.starlottery.manager.PlayerLotteryManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity humanEntity = event.getPlayer();
        if (humanEntity instanceof Player player) {
            if (LotteryManager.getLotteryPlayers().containsKey(player.getName())) {
                PlayerLotteryManager manager = LotteryManager.getLotteryPlayers().get(player.getName());
                if (manager.isRun()) {
                    manager.setAnimationEnded(true);
                    manager.ended();
                }
            }
        }
    }
}
