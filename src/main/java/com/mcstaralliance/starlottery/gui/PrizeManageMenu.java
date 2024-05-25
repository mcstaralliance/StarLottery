package com.mcstaralliance.starlottery.gui;

import com.mcstaralliance.starlottery.manager.ConfigManager;
import com.mcstaralliance.starlottery.manager.InventoryManager;
import com.mcstaralliance.starlottery.manager.LotteryManager;
import com.mcstaralliance.starlottery.manager.PrizeInfoManager;
import com.mcstaralliance.starlottery.util.StringConst;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrizeManageMenu extends Panel implements InventoryHolder {
    public PrizeManageMenu(Player player, String key) {
        FileConfiguration config = ConfigManager.load("BetterLottery");
        Inventory inventory = Bukkit.createInventory(player, 54, StringConst.SETTING_TITLE);
        for (int i = 0; i < 36; i++) {
            inventory.setItem(i, createItemStack(Material.GRAY_STAINED_GLASS_PANE, "§7暂时被封印的结界"));
        }
        for (int i2 = 36; i2 < 54; i2++) {
            inventory.setItem(i2, createItemStack(Material.BLACK_STAINED_GLASS_PANE, "§8永远被封印的结界"));
        }
        if (config.contains(key + ".chest")) {
            String[] chestInfo = config.getString(key + ".chest").split(" ");
            int x = Integer.parseInt(chestInfo[0]);
            int y = Integer.parseInt(chestInfo[1]);
            int z = Integer.parseInt(chestInfo[2]);
            Inventory buttonInventory = ((Chest) PrizeInfoManager.getChestWorld().getBlockAt(x, y, z).getState()).getInventory();
            List<Integer> odds = config.getIntegerList(key + ".odds");
            List<Integer> notice = config.getIntegerList(key + ".notice");
            int maxOdds = 0;
            for (Integer i3 : odds) {
                if (i3 > 0) {
                    maxOdds += i3;
                }
            }
            for (int i4 = 0; i4 < 27; i4++) {
                ItemStack tempItemStack = buttonInventory.getItem(i4);
                if (tempItemStack != null) {
                    ItemStack tempItemStack2 = tempItemStack.clone();
                    ItemMeta itemMeta = tempItemStack2.getItemMeta();
                    List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
                    if (itemMeta.hasLore()) {
                        lore.add("§f--------------------");
                    }
                    lore.add("§a中奖几率： §e" + (odds.get(i4) == -1 ? "未设置" : odds.get(i4) + "/" + maxOdds));
                    lore.add("§a全服公告： §e" + (notice.get(i4) == 0 ? "关闭" : "开启"));
                    lore.add("§c设置几率： §e左键单击");
                    lore.add("§c" + (notice.get(i4) == 0 ? "开启" : "关闭") + "公告： §eShift+左键单击");
                    lore.add("§c移除奖品： §eShift+右键单击");
                    itemMeta.setLore(lore);
                    tempItemStack2.setItemMeta(itemMeta);
                    inventory.setItem(i4, tempItemStack2);
                }
            }
            Block block = PrizeInfoManager.getChestWorld().getBlockAt(x, y + 1, z);
            if (block.getType().equals(Material.CHEST)) {
                Inventory topInventory = ((Chest) block.getState()).getInventory();
                for (int i5 = 0; i5 < 27; i5++) {
                    ItemStack tempItemStack3 = topInventory.getItem(i5);
                    if (tempItemStack3 != null) {
                        ItemStack tempItemStack4 = tempItemStack3.clone();
                        ItemMeta itemMeta2 = tempItemStack4.getItemMeta();
                        List<String> lore2 = itemMeta2.hasLore() ? itemMeta2.getLore() : new ArrayList<>();
                        if (itemMeta2.hasLore()) {
                            lore2.add("§f--------------------");
                        }
                        lore2.add("§a中奖几率： §e" + (odds.get(i5 + 27) == -1 ? "未设置" : odds.get(i5 + 27) + "/" + maxOdds));
                        lore2.add("§a全服公告： §e" + (notice.get(i5 + 27) == 0 ? "关闭" : "开启"));
                        lore2.add("§c设置几率： §e左键单击");
                        lore2.add("§c" + (notice.get(i5 + 27) == 0 ? "开启" : "关闭") + "公告： §eShift+左键单击");
                        lore2.add("§c移除奖品： §eShift+右键单击");
                        itemMeta2.setLore(lore2);
                        tempItemStack4.setItemMeta(itemMeta2);
                        inventory.setItem(i5 + 27, tempItemStack4);
                    }
                }
            }
        }
        inventory.setItem(48, createItemStack(Material.HOPPER, "§a添加奖品", "§c添加操作： §e拖拽物品到此处左键单击"));
        inventory.setItem(49, createItemStack(Material.ENDER_PEARL, "§a返回上层", "§c返回操作： §e左键单击"));
        boolean enable = config.getBoolean(key + ".enable");
        Material material = enable ? Material.WATER_BUCKET : Material.BUCKET;
        String str = "§a" + (enable ? "关闭" : "开启") + "奖池";
        String[] strArr = new String[1];
        strArr[0] = "§c" + (enable ? "关闭" : "开启") + "操作： §e左键单击";
        inventory.setItem(50, createItemStack(material, str, strArr));
        setInventory(inventory);
        HashMap<String, InventoryManager> hashMap = LotteryManager.getSettingOps();
        String name = player.getName();
        InventoryManager inventoryManager = new InventoryManager(key, inventory);
        hashMap.put(name, inventoryManager);
    }

    public static void executePrizeMangeClick(Player player, int rawSlot, ClickType action) {
        Block block;
        Inventory chestInventory;
        FileConfiguration config = ConfigManager.load("BetterLottery");
        InventoryManager inventoryManager = LotteryManager.getSettingOps().get(player.getName());
        String key = inventoryManager.getKey();
        boolean isOpened = config.getBoolean(key + ".enable");
        if (action.isLeftClick()) {
            if (!action.isShiftClick()) {
                if (rawSlot == 48) {
                    if (!isOpened) {
                        ItemStack itemStack = player.getItemOnCursor();
                        if (itemStack.getType().getId() > 0) {
                            boolean isTopInventory = false;
                            if (config.contains(key + ".chest")) {
                                String[] chestInfo = config.getString(key + ".chest").split(" ");
                                int x = Integer.parseInt(chestInfo[0]);
                                int y = Integer.parseInt(chestInfo[1]);
                                int z = Integer.parseInt(chestInfo[2]);
                                Inventory buttonInventory = ((Chest) PrizeInfoManager.getChestWorld().getBlockAt(x, y, z).getState()).getInventory();
                                boolean isFull = true;
                                int i = 0;
                                while (true) {
                                    if (i >= 27) {
                                        break;
                                    } else if (buttonInventory.getItem(i) == null) {
                                        isFull = false;
                                        break;
                                    } else {
                                        i++;
                                    }
                                }
                                if (isFull) {
                                    Block block2 = PrizeInfoManager.getChestWorld().getBlockAt(x, y + 1, z);
                                    if (block2.getType().equals(Material.AIR)) {
                                        block2.setType(Material.CHEST);
                                    }
                                    chestInventory = ((Chest) block2.getState()).getInventory();
                                    isTopInventory = true;
                                } else {
                                    chestInventory = buttonInventory;
                                }
                            } else {
                                int i2 = 0;
                                while (true) {
                                    block = PrizeInfoManager.getChestWorld().getBlockAt(i2, 4, 0);
                                    if (block.getType().equals(Material.AIR)) {
                                        break;
                                    }
                                    i2 += 2;
                                }
                                block.setType(Material.CHEST);
                                chestInventory = ((Chest) block.getState()).getInventory();
                                config.set(key + ".chest", block.getX() + " " + block.getY() + " " + block.getZ());
                                ArrayList arrayList = new ArrayList();
                                ArrayList arrayList2 = new ArrayList();
                                for (int j = 0; j < 36; j++) {
                                    arrayList.add(-1);
                                    arrayList2.add(0);
                                }
                                config.set(key + ".odds", arrayList);
                                config.set(key + ".notice", arrayList2);
                                ConfigManager.save(config, "BetterLottery");
                            }
                            int i3 = 0;
                            while (true) {
                                if (i3 >= (isTopInventory ? 9 : 27)) {
                                    break;
                                } else if (chestInventory.getItem(i3) == null) {
                                    chestInventory.setItem(i3, itemStack);
                                    break;
                                } else {
                                    i3++;
                                }
                            }
                            List<Integer> odds = config.getIntegerList(key + ".odds");
                            List<Integer> notice = config.getIntegerList(key + ".notice");
                            int maxOdds = 0;
                            for (Integer i4 : odds) {
                                if (i4.intValue() > 0) {
                                    maxOdds += i4.intValue();
                                }
                            }
                            int i5 = 0;
                            while (i5 < 36) {
                                ItemStack tempItemStack = inventoryManager.getInventory().getItem(i5);
                                if (!tempItemStack.getType().equals(Material.WHITE_STAINED_GLASS_PANE) || !tempItemStack.getItemMeta().getDisplayName().equals("§7暂时被封印的结界")) {
                                    if (i5 == 35) {
                                        player.sendMessage("[§aBetterLotteryReload§f] §c添加失败§e>>>奖池已满，无法继续添加");
                                    }
                                    i5++;
                                } else {
                                    ItemStack itemStack2 = itemStack.clone();
                                    ItemMeta itemMeta = itemStack2.getItemMeta();
                                    List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
                                    if (itemMeta.hasLore()) {
                                        lore.add("§f--------------------");
                                    }
                                    lore.add("§a中奖几率： §e" + (odds.get(i5) == -1 ? "未设置" : odds.get(i5) + "/" + maxOdds));
                                    lore.add("§a全服公告： §e" + (notice.get(i5) == 0 ? "关闭" : "开启"));
                                    lore.add("§c设置几率： §e左键单击");
                                    lore.add("§c" + (notice.get(i5) == 0 ? "开启" : "关闭") + "公告： §eShift+左键单击");
                                    lore.add("§c移除奖品： §eShift+右键单击");
                                    itemMeta.setLore(lore);
                                    itemStack2.setItemMeta(itemMeta);
                                    inventoryManager.getInventory().setItem(i5, itemStack2);
                                    return;
                                }
                            }
                            return;
                        }
                        return;
                    }
                    player.sendMessage("[§aBetterLotteryReload§f] §c添加失败§e>>>必须先关闭奖池才能进行操作");
                } else if (rawSlot == 49) {
                    new PoolManageMenu(player);
                } else if (rawSlot == 50) {
                    if (config.getBoolean(key + ".enable")) {
                        config.set(key + ".enable", Boolean.FALSE);
                        ConfigManager.save(config, "BetterLottery");
                        new PrizeManageMenu(player, key);
                        return;
                    }
                    boolean allowOpen = true;
                    int maxOdds2 = 0;
                    if (config.contains(key + ".odds")) {
                        List<Integer> odds2 = config.getIntegerList(key + ".odds");
                        int i6 = 0;
                        while (true) {
                            if (i6 >= 36) {
                                break;
                            }
                            ItemStack tempItemStack2 = inventoryManager.getInventory().getItem(i6);
                            if (!tempItemStack2.getType().equals(Material.WHITE_STAINED_GLASS_PANE) || !tempItemStack2.getItemMeta().getDisplayName().equals("§7暂时被封印的结界")) {
                                if (odds2.get(i6) == -1) {
                                    allowOpen = false;
                                    break;
                                }
                                maxOdds2 += odds2.get(i6);
                            }
                            i6++;
                        }
                    }
                    if (!allowOpen) {
                        player.sendMessage("[§aBetterLotteryReload§f] §c开启失败>>>必须为所有奖品设置几率");
                    } else if (maxOdds2 == 0) {
                        player.sendMessage("[§aBetterLotteryReload§f] §c开启失败>>>必须有一个几率大于0的奖品");
                    } else {
                        config.set(key + ".enable", true);
                        ConfigManager.save(config, "BetterLottery");
                        new PrizeManageMenu(player, key);
                        player.sendMessage("[§aBetterLotteryReload§f] §c开启完毕>>>获取奖券之后手持奖券右键即可进行抽奖");
                    }
                } else if (rawSlot >= 36) {
                } else {
                    if (!isOpened) {
                        ItemStack tempItemStack3 = inventoryManager.getInventory().getItem(rawSlot);
                        if (!tempItemStack3.getType().equals(Material.WHITE_STAINED_GLASS_PANE) || !tempItemStack3.getItemMeta().getDisplayName().equals("§7暂时被封印的结界")) {
                            player.closeInventory();
                            LotteryManager.getSettingOddsOps().put(player.getName(), rawSlot);
                            player.sendMessage("[§aBetterLotteryReload§f] §e设置几率>>>请在聊天框中输入该奖品的几率§c(必须为正整数，可以是0)");
                            return;
                        }
                        return;
                    }
                    player.sendMessage("[§aBetterLotteryReload§f] §c设置失败§e>>>必须先关闭奖池才能进行操作");
                }
            } else if (rawSlot >= 36) {
            } else {
                if (!isOpened) {
                    ItemStack tempItemStack4 = inventoryManager.getInventory().getItem(rawSlot);
                    if (!tempItemStack4.getType().equals(Material.WHITE_STAINED_GLASS_PANE) || !tempItemStack4.getItemMeta().getDisplayName().equals("§7暂时被封印的结界")) {
                        String string = config.getString(key + ".chest");
                        List<Integer> odds3 = config.getIntegerList(key + ".odds");
                        List<Integer> notice2 = config.getIntegerList(key + ".notice");
                        notice2.set(rawSlot, (notice2.get(rawSlot) == 0 ? 1 : 0));
                        config.set(key + ".notice", notice2);
                        ConfigManager.save(config, "BetterLottery");
                        int maxOdds3 = 0;
                        for (Integer i7 : odds3) {
                            if (i7 > 0) {
                                maxOdds3 += i7;
                            }
                        }
                        String[] chestInfo2 = string.split(" ");
                        int x2 = Integer.parseInt(chestInfo2[0]);
                        int y2 = Integer.parseInt(chestInfo2[1]);
                        int z2 = Integer.parseInt(chestInfo2[2]);
                        World chestWorld = PrizeInfoManager.getChestWorld();
                        if (rawSlot >= 27) {
                            y2++;
                        }
                        ItemStack itemStack3 = ((Chest) chestWorld.getBlockAt(x2, y2, z2).getState()).getInventory().getItem(rawSlot % 27).clone();
                        ItemMeta itemMeta2 = itemStack3.getItemMeta();
                        List<String> lore2 = itemMeta2.hasLore() ? itemMeta2.getLore() : new ArrayList<>();
                        if (itemMeta2.hasLore()) {
                            lore2.add("§f--------------------");
                        }
                        lore2.add("§a中奖几率： §e" + (odds3.get(rawSlot) == -1 ? "未设置" : odds3.get(rawSlot) + "/" + maxOdds3));
                        lore2.add("§a全服公告： §e" + (notice2.get(rawSlot) == 0 ? "关闭" : "开启"));
                        lore2.add("§c设置几率： §e左键单击");
                        lore2.add("§c" + (notice2.get(rawSlot) == 0 ? "开启" : "关闭") + "公告： §eShift+左键单击");
                        lore2.add("§c移除奖品： §eShift+右键单击");
                        itemMeta2.setLore(lore2);
                        itemStack3.setItemMeta(itemMeta2);
                        inventoryManager.getInventory().setItem(rawSlot, itemStack3);
                        return;
                    }
                    return;
                }
                player.sendMessage("[§aBetterLotteryReload§f] §c设置失败§e>>>必须先关闭奖池才能进行操作");
            }
        } else if (action.isShiftClick() && action.isRightClick() && rawSlot < 36) {
            if (!isOpened) {
                ItemStack tempItemStack5 = inventoryManager.getInventory().getItem(rawSlot);
                if (!tempItemStack5.getType().equals(Material.WHITE_STAINED_GLASS_PANE) || !tempItemStack5.getItemMeta().getDisplayName().equals("§7暂时被封印的结界")) {
                    String[] chestInfo3 = config.getString(key + ".chest").split(" ");
                    int x3 = Integer.parseInt(chestInfo3[0]);
                    int y3 = Integer.parseInt(chestInfo3[1]);
                    int z3 = Integer.parseInt(chestInfo3[2]);
                    World chestWorld2 = PrizeInfoManager.getChestWorld();
                    if (rawSlot >= 27) {
                        y3++;
                    }
                    ((Chest) chestWorld2.getBlockAt(x3, y3, z3).getState()).getInventory().setItem(rawSlot < 27 ? rawSlot : rawSlot % 27, null);
                    List<Integer> odds4 = config.getIntegerList(key + ".odds");
                    List<Integer> notice3 = config.getIntegerList(key + ".notice");
                    odds4.set(rawSlot, -1);
                    notice3.set(rawSlot, 0);
                    config.set(key + ".odds", odds4);
                    config.set(key + ".notice", notice3);
                    ConfigManager.save(config, "BetterLottery");
                    new PrizeManageMenu(player, key);
                    return;
                }
                return;
            }
            player.sendMessage("[§aBetterLotteryReload§f] §c移除失败§e>>>必须先关闭奖池才能进行操作");
        }
    }
}
