package com.mcstaralliance.starlottery.listener;

import com.mcstaralliance.starlottery.util.DebugUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo().getWorld().getName().equals("BetterLotteryChest") && !DebugUtil.getDebugMode()) {
            event.getPlayer().sendMessage("[§aBetterLottery-Reloaded§f] §c该世界禁止进入，对~ OP也不行╮(￣▽￣\")╭");
            event.setCancelled(true);
        }
    }
}
