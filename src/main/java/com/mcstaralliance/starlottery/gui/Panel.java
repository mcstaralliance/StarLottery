package com.mcstaralliance.starlottery.gui;

import com.mcstaralliance.starlottery.manager.ConfigManager;
import com.mcstaralliance.starlottery.manager.InventoryManager;
import com.mcstaralliance.starlottery.manager.LotteryManager;
import com.mcstaralliance.starlottery.manager.PrizeInfoManager;
import com.mcstaralliance.starlottery.util.StringConst;
import com.sun.tools.javac.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class Panel implements InventoryHolder {
    private Inventory inv;
    public Panel() {}

    public static ItemStack createItemStack(Material material, String name, String... lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(name);
        }
        List<String> loreList = new ArrayList<>();
        Collections.addAll(loreList, lore);
        if (itemMeta != null) {
            itemMeta.setLore(loreList);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static void showPanel(Player player, Inventory inventory) {
        player.closeInventory();
        player.openInventory(inventory);
    }
    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void setInventory(Inventory inv) {
        this.inv = inv;
    }
}
