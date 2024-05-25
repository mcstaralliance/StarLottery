package com.mcstaralliance.starlottery.listener;

import com.mcstaralliance.starlottery.manager.LotteryManager;
import com.mcstaralliance.starlottery.manager.PrizeInfoManager;
import com.mcstaralliance.starlottery.util.PoolCreationUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (LotteryManager.getCreatingOps().containsKey(player.getName())) {
            // 通过聊天栏获取新建奖池名，奖池名优先，概率其次
            event.setCancelled(true);
            String poolName = event.getMessage();
            PoolCreationUtil.createPool(player, poolName);
            LotteryManager.getCreatingOps().remove(player.getName());
            return;
        }
        if (LotteryManager.getSelectPoolOps().containsKey(player.getName())) {
            event.setCancelled(true);
            int odds = Integer.parseInt(event.getMessage());
            PrizeInfoManager.setOdds(player, odds);
        }
    }
}
