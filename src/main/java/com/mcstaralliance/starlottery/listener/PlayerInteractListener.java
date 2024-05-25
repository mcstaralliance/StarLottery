package com.mcstaralliance.starlottery.listener;

import com.mcstaralliance.starlottery.gui.LotteryMenu;
import com.mcstaralliance.starlottery.util.StarLotteryUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        Action action = event.getAction();
        if (StarLotteryUtil.isRightClick(action) && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            LotteryMenu.handleTicketRightClick(event.getPlayer());
        }
    }
}
