package com.mcstaralliance.starlottery.gui;

import com.mcstaralliance.starlottery.StarLottery;
import com.mcstaralliance.starlottery.manager.ConfigManager;
import com.mcstaralliance.starlottery.manager.LotteryManager;
import com.mcstaralliance.starlottery.manager.PlayerLotteryManager;
import com.mcstaralliance.starlottery.manager.PrizeInfoManager;
import com.mcstaralliance.starlottery.util.StarLotteryUtil;
import com.mcstaralliance.starlottery.util.StringConst;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class LotteryMenu extends Panel implements InventoryHolder {
    private final StarLottery plugin = StarLottery.getInstance();
    private static final int[] BORDER_SLOTS = {12, 13, 14, 21, 22, 23, 30, 31, 32};

    public LotteryMenu(Player player, String poolName) {
        FileConfiguration config = ConfigManager.load("BetterLottery");
        String poolTitle = config.getString(poolName + ".title");
        Inventory inventory = Bukkit.createInventory(this, 45, StringConst.TITLE_PREFIX + poolTitle.replaceAll("&", "§"));
        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, Panel.createItemStack(Material.WHITE_STAINED_GLASS_PANE, "§7暂时被封印的结界"));
        }
        int[] arrayOfInt = BORDER_SLOTS;
        int j = arrayOfInt.length;
        for (byte b = 0; b < j; b = (byte) (b + 1)) {
            inventory.setItem(arrayOfInt[b], Panel.createItemStack(Material.BLACK_STAINED_GLASS_PANE, "§8永远被封印的结界"));
        }
        String[] chestInfo = config.getString(poolName + ".chest").split(" ");
        int x = Integer.parseInt(chestInfo[0]);
        int y = Integer.parseInt(chestInfo[1]);
        int z = Integer.parseInt(chestInfo[2]);
        Inventory buttonInventory = ((Chest) PrizeInfoManager.getChestWorld().getBlockAt(x, y, z).getState()).getInventory();
        int currentPos = 0;
        for (int i2 = 0; i2 < 27; i2++) {
            ItemStack tempItemStack = buttonInventory.getItem(i2);
            if (tempItemStack != null) {
                inventory.setItem(currentPos, tempItemStack);
                do {
                    currentPos++;
                } while (inventory.getItem(currentPos).getType().equals(Material.WHITE_STAINED_GLASS_PANE));
            }
        }
        Block block = PrizeInfoManager.getChestWorld().getBlockAt(x, y + 1, z);
        if (block.getType().equals(Material.CHEST)) {
            Inventory topInventory = ((Chest) block.getState()).getInventory();
            for (int i3 = 0; i3 < 27; i3++) {
                ItemStack tempItemStack2 = topInventory.getItem(i3);
                if (tempItemStack2 != null) {
                    inventory.setItem(currentPos, tempItemStack2);
                    do {
                        currentPos++;
                    } while (currentPos < 45 && inventory.getItem(currentPos).getType().equals(Material.BLACK_STAINED_GLASS_PANE));
                }
            }
        }
        inventory.setItem(22, Panel.createItemStack(Material.LEVER, "§a启动"));
        Panel.showPanel(player, inventory);
        HashMap<String, PlayerLotteryManager> hashMap = LotteryManager.getLotteryPlayers();
        String name = player.getName();
        PlayerLotteryManager playerLotteryManager = new PlayerLotteryManager(player, poolName, inventory, player.getInventory().getItemInMainHand());
        hashMap.put(name, playerLotteryManager);
    }
    public static void handleTicketRightClick(Player player) {
        String itemName = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
        if (itemName.contains("&")) {
            return;
        }
        String key = itemName.replaceAll("§", "&");
        // get pure pool name.
        if (StarLotteryUtil.isAvailablePool(key)) {
            // 展示奖池待启动界面
            new LotteryMenu(player, key);
        } else {
            player.sendMessage("[§aBetterLotteryReload§f] §c无法使用[" + key.replaceAll("&", "§") + "§c]§e>>>该奖池未开启");
        }
    }
    // 处理点击「开始抽奖」
    public static void launchLottery(Player player) {
        PlayerLotteryManager manager = LotteryManager.getLotteryPlayers().get(player.getName());
        if (manager != null && !manager.isRun()) {
            FileConfiguration config = ConfigManager.load("BetterLottery");
            if (!config.getBoolean(manager.getKey() + ".enable")) {
                player.sendMessage("[§aBetterLotteryReload§f] §c无法启动§e>>>该奖池已关闭");
                player.closeInventory();
            } else if (!manager.getHand().equals(player.getInventory().getItemInMainHand())) {
                player.closeInventory();
                LotteryManager.getLotteryPlayers().remove(player.getName());
            } else {
                boolean isFull = true;
                ItemStack[] itemStacks = player.getInventory().getContents();
                for (ItemStack itemStack : itemStacks) {
                    if (itemStack == null) {
                        isFull = false;
                        break;
                    }
                }
                if (isFull) {
                    player.sendMessage("[§aBetterLotteryReload§f] §c无法启动§e>>>背包至少保留一个空位");
                    player.closeInventory();
                    return;
                }
                List<Integer> odds = config.getIntegerList(manager.getKey() + ".odds");
                List<Integer> notice = config.getIntegerList(manager.getKey() + ".notice");
                int maxOdds = 0;
                for (Integer i : odds) {
                    if (i > 0) {
                        maxOdds += i;
                    } else {
                        break;
                    }
                }
                manager.setMaxOdds(maxOdds);
                String[] chestInfo = config.getString(manager.getKey() + ".chest").split(" ");
                int x = Integer.parseInt(chestInfo[0]);
                int y = Integer.parseInt(chestInfo[1]);
                int z = Integer.parseInt(chestInfo[2]);
                Inventory buttonInventory = ((Chest) PrizeInfoManager.getChestWorld().getBlockAt(x, y, z).getState()).getInventory();
                ArrayList<PrizeInfoManager> arrayList = new ArrayList<>();
                for (int i = 0; i < 27; i++) {
                    ItemStack tempItemStack = buttonInventory.getItem(i);
                    if (tempItemStack != null) {
                        PrizeInfoManager prizeInfoManager = new PrizeInfoManager(tempItemStack.clone(), odds.get(i), notice.get(i) == 1);
                        arrayList.add(prizeInfoManager);
                    }
                }
                Block block = PrizeInfoManager.getChestWorld().getBlockAt(x, y + 1, z);
                if (block.getType().equals(Material.CHEST)) {
                    Inventory topInventory = ((Chest) block.getState()).getInventory();
                    for (int i3 = 0; i3 < 27; i3++) {
                        ItemStack tempItemStack2 = topInventory.getItem(i3);
                        if (tempItemStack2 != null) {
                            PrizeInfoManager prizeInfoManager2 = new PrizeInfoManager(tempItemStack2.clone(), odds.get(i3 + 27), notice.get(i3 + 27) == 1);
                            arrayList.add(prizeInfoManager2);
                        }
                    }
                }
                Collections.shuffle(arrayList);
                manager.setPrizeInfo(arrayList);
                int num = ((int) (Math.random() * ((double) manager.getMaxOdds()))) + 1;
                Iterator<PrizeInfoManager> it = manager.getPrizeInfo().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    PrizeInfoManager prizeInfoManager3 = it.next();
                    num -= prizeInfoManager3.getOdds();
                    if (num <= 0) {
                        manager.setPrize(prizeInfoManager3);
                        break;
                    }
                }
                manager.run();
            }
        }
    }
}


