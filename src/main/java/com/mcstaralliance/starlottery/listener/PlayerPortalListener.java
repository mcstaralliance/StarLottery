package com.mcstaralliance.starlottery.listener;

import com.mcstaralliance.starlottery.StarLottery;
import com.mcstaralliance.starlottery.util.DebugUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PlayerPortalListener implements Listener {

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (event.getTo().getWorld().getName().equals("BetterLotteryChest") && !DebugUtil.getDebugMode()) {
            event.getPlayer().sendMessage("[§aStarLottery§f] §c该世界禁止进入，对~ OP也不行╮(￣▽￣\")╭");
            event.setCancelled(true);
        }
    }
}
