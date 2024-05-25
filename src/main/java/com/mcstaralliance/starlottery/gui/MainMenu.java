package com.mcstaralliance.starlottery.gui;

import com.mcstaralliance.starlottery.manager.LotteryManager;
import com.mcstaralliance.starlottery.util.StringConst;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MainMenu extends Panel implements InventoryHolder {
    public MainMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(this, 9, StringConst.MENU_TITLE);
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, createItemStack(Material.BLACK_STAINED_GLASS_PANE, "§8永远被封印的结界"));
        }
        inventory.setItem(3, createItemStack(Material.ANVIL, "§a创建抽奖池", "§c创建操作： §e左键单击"));
        inventory.setItem(5, createItemStack(Material.OAK_SIGN, "§a抽奖池设置", "§c设置操作： §e左键单击"));
        setInventory(inventory);
        showPanel(player, inventory);
    }

    public static void executeMainMenuClick(Player player, int rawSlot) {
        if (rawSlot == 3) {
            player.sendMessage("[§aBetterLotteryReload§f] §e第一步>>>请在聊天框中输入奖池名称§c(颜色符&,名称可以重复)");
            LotteryManager.getCreatingOps().put(player.getName(), null);
            player.closeInventory();
        } else if (rawSlot == 5) {
            LotteryManager.getSelectPoolOps().put(player.getName(), 0);
            new PoolManageMenu(player);
        }
    }
}
