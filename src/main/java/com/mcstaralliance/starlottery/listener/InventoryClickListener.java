package com.mcstaralliance.starlottery.listener;

import com.mcstaralliance.starlottery.gui.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player && event.getRawSlot() >= 0)) {
            return;
        }
        // 如果是 StarLottery 相关菜单才向下执行
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof Panel)) {
            return;
        }
        int rawSlot = event.getRawSlot();
        event.setCancelled(true);
        if (holder instanceof MainMenu) {
            MainMenu.executeMainMenuClick(player, rawSlot);
            return;
        }
        if (holder instanceof PrizeManageMenu) {
            if (rawSlot < 54 || event.isShiftClick() || event.getClick() == ClickType.DOUBLE_CLICK) {
                event.setCancelled(true);
            }
            PrizeManageMenu.executePrizeMangeClick(player, rawSlot, event.getClick());
            return;
        }
        if (holder instanceof PoolManageMenu) {
            PoolManageMenu.executePoolManageMenuClick(player, rawSlot, event.getClick());
            return;
        }
        if (holder instanceof LotteryMenu) {
            boolean clickLaunchButton = event.getRawSlot() == 22;
            if (clickLaunchButton) {
                LotteryMenu.launchLottery(player);
            }
        }
    }
}


