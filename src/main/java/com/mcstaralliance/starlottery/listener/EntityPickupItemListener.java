package com.mcstaralliance.starlottery.listener;

import com.mcstaralliance.starlottery.manager.LotteryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class EntityPickupItemListener implements Listener {
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (LotteryManager.getLotteryPlayers().containsKey(player.getName()) && LotteryManager.getLotteryPlayers().get(player.getName()).isRun()) {
                event.setCancelled(true);
            }
        }
    }
}
