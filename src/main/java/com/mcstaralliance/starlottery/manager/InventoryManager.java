package com.mcstaralliance.starlottery.manager;

import org.bukkit.inventory.Inventory;

public class InventoryManager {
    private Inventory inventory;
    private String key;

    public InventoryManager(String key, Inventory inventory) {
        this.key = key;
        this.inventory = inventory;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
