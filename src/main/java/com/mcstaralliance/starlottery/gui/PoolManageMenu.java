package com.mcstaralliance.starlottery.gui;

import com.mcstaralliance.starlottery.manager.ConfigManager;
import com.mcstaralliance.starlottery.manager.LotteryManager;
import com.mcstaralliance.starlottery.util.StringConst;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class PoolManageMenu extends Panel implements InventoryHolder {
    public PoolManageMenu(Player player) {
        FileConfiguration config = ConfigManager.load("BetterLottery");
        Set<String> keys = config.getKeys(false);
        Inventory inventory = Bukkit.createInventory(player, 54, StringConst.SELECT_POOL_TITLE);
        // 先放暂时封印结界
        for (int i = 0; i < 36; i++) {
            inventory.setItem(i, Panel.createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§7暂时被封印的结界"));
        }
        // 再放永久封印结界
        for (int i = 36; i < 54; i++) {
            inventory.setItem(i, Panel.createItemStack(Material.BLACK_STAINED_GLASS_PANE, "§8永远被封印的结界"));
        }
        String[] strings = new String[keys.size()];
        keys.toArray(strings);
        int startValue = LotteryManager.getSelectPoolOps().get(player.getName()) * 36;
        for (int i = startValue; i < startValue + 36 && i < strings.length; i++) {
            String title = "§a奖池名称： §e" + config.getString(strings[i] + ".title").replaceAll("&", "§");
            boolean enable = config.getBoolean(strings[i] + ".enable");
            ItemStack itemStack = Panel.createItemStack(Material.WHITE_WOOL, title, "§a奖券名称： §e" + strings[i].replaceAll("&", "§"), "§a当前状态： §e" + (enable ? "开启" : "关闭"), "§c更改设置： §e左键单击", "§c拓印奖券： §e手持需要拓印的物品 Shift+左键单击", "§c删除奖池： §eShift+右键单击");
            Damageable meta = (Damageable) itemStack.getItemMeta();
            if (meta != null) {
                meta.setDamage(enable ? 5 : 14);
            }
            itemStack.setItemMeta(meta);
            inventory.setItem(i, itemStack);
        }
        boolean hasNext = (strings.length - startValue) + 36 > 0;
        inventory.setItem(48, Panel.createItemStack(Material.LEATHER_CHESTPLATE, (startValue > 0 ? "§a" : "§7") + "上一页", "§c翻页操作： §e左键单击"));
        inventory.setItem(49, Panel.createItemStack(Material.ENDER_PEARL, "§a返回上层", "§c返回操作： §e左键单击"));
        inventory.setItem(50, Panel.createItemStack(Material.LEATHER_LEGGINGS, (hasNext ? "§a" : "§7") + "下一页", "§c翻页操作： §e左键单击"));
        Panel.showPanel(player, inventory);
    }

    public static String getPoolName(Player player, int slot) {
        FileConfiguration config = ConfigManager.load("BetterLottery");
        Set<String> keys = config.getKeys(false);
        int startValue = LotteryManager.getSelectPoolOps().get(player.getName()) * 36;
        String[] strings = new String[keys.size()];
        keys.toArray(strings);

        return strings[startValue + slot];
    }

    public static void executePoolManageMenuClick(Player player, int rawSlot, ClickType action) {
        FileConfiguration config = ConfigManager.load("BetterLottery");
        int startValue = LotteryManager.getSelectPoolOps().get(player.getName()) * 36;
        Set<String> keys = config.getKeys(false);
        String[] strings = new String[keys.size()];
        keys.toArray(strings);
        if (!action.isLeftClick()) {
            return;
        }

        if (!action.isShiftClick()) {
            if (rawSlot < 36 && startValue + rawSlot < strings.length) {
                new PrizeManageMenu(player, getPoolName(player, rawSlot));
            } else if (rawSlot == 48) {
                if (startValue > 0) {
                    LotteryManager.getSelectPoolOps().put(player.getName(), LotteryManager.getSelectPoolOps().get(player.getName()) - 1);
                    new PoolManageMenu(player);
                }
            } else if (rawSlot == 49) {
                new MainMenu(player);
            } else if (rawSlot == 50 && (strings.length - startValue) + 36 > 0) {
                LotteryManager.getSelectPoolOps().put(player.getName(), LotteryManager.getSelectPoolOps().get(player.getName()) + 1);
                new PoolManageMenu(player);
            }
        } else if (startValue + rawSlot < strings.length) {
            String key = strings[startValue + rawSlot];
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType().equals(Material.AIR)) {
                player.sendMessage("[§aBetterLotteryReload§f] §c拓印失败>>>手持物品不能为空");
                return;
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(key.replaceAll("&", "§"));
            }
            itemStack.setItemMeta(itemMeta);
            player.sendMessage("[§aBetterLotteryReload§f] §c拓印完成>>>手持右键即可食用");
        }

        if (action.isRightClick() && action.isShiftClick() && startValue + rawSlot < strings.length) {
            config.set(strings[startValue + rawSlot], null);
            ConfigManager.save(config, "BetterLottery");
            new PoolManageMenu(player);
        }
    }
}
