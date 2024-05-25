package com.mcstaralliance.starlottery.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerLotteryManager extends InventoryManager {
    private boolean animationEnded;
    private int delayIndex;
    private Delay[] delay;
    private ItemStack hand;
    private final List<ItemStack> inventoryItemStacks = new ArrayList<>();
    private boolean isGave;
    private int lastItemStackSelectedID = -1;
    private int maxOdds;
    private final Player player;
    private PrizeInfoManager prize;
    private List<PrizeInfoManager> prizeInfoManager;
    private boolean run;
    private long startTime;

    public PlayerLotteryManager(Player player, String key, Inventory inventory, ItemStack hand) {
        super(key, inventory);
        this.hand = hand;
        this.player = player;
    }

    public ItemStack getHand() {
        return hand;
    }

    public void setHand(ItemStack hand) {
        this.hand = hand;
    }

    public PrizeInfoManager getPrize() {
        return prize;
    }

    public void setPrize(PrizeInfoManager prize) {
        this.prize = prize;
        this.prize.setItemStack(this.prize.getItemStack().clone());
    }

    public List<PrizeInfoManager> getPrizeInfo() {
        return prizeInfoManager;
    }

    public void setPrizeInfo(List<PrizeInfoManager> prizeInfoManager) {
        this.prizeInfoManager = prizeInfoManager;
    }

    public int getMaxOdds() {
        return this.maxOdds;
    }

    public void setMaxOdds(int maxOdds) {
        this.maxOdds = maxOdds;
    }

    public void run() {
        ItemStack[] contents = this.player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null && contents[i].equals(this.hand)) {
                if (contents[i].getAmount() == 1) {
                    this.player.getInventory().setItem(i, null);
                } else {
                    contents[i].setAmount(contents[i].getAmount() - 1);
                    this.player.getInventory().setItem(i, contents[i]);
                }
                break;
            }
        }
        int ranNum = ((int) (Math.random() * ((double) this.prizeInfoManager.size()))) + this.prizeInfoManager.size();
        int posCount;
        for (posCount = 0; posCount < this.prizeInfoManager.size(); posCount++) {
            ItemStack itemStack = this.prizeInfoManager.get((((ranNum + 4) % this.prizeInfoManager.size()) + posCount) % this.prizeInfoManager.size()).getItemStack();
            if (itemStack.equals(this.prize.getItemStack())) {
                break;
            }
        }
        this.delay = new Delay[(ranNum + 5 + posCount)];
        for (int i2 = 0; i2 < 5; i2++) {
            this.delay[i2] = new Delay(500 - (i2 * 100));
        }
        for (int i3 = 0; i3 < ranNum; i3++) {
            this.delay[i3 + 5] = new Delay(100);
        }
        for (int i4 = 0; i4 < posCount; i4++) {
            this.delay[i4 + 5 + ranNum] = new Delay(((this.prizeInfoManager.size() * 20) + 100) - ((this.prizeInfoManager.size() - i4) * 20));
        }
        for (int i5 = this.delay.length - 1; i5 >= 0; i5--) {
            int count = 0;
            for (int j = 0; j <= i5; j++) {
                count += this.delay[j].getTime();
            }
            this.delay[i5].setTime(count);
        }
        this.run = true;
        this.startTime = System.currentTimeMillis();
        getInventory().setItem(22, null);

        int currentPos = 0;
        for (PrizeInfoManager prizeInfoManager : this.prizeInfoManager) {
            getInventory().setItem(currentPos, prizeInfoManager.getItemStack());
            this.inventoryItemStacks.add(getInventory().getItem(currentPos));
            currentPos++;
            if (currentPos == 22 || (currentPos < 45 && getInventory().getItem(currentPos).getType().equals(Material.BLACK_STAINED_GLASS_PANE))) {

            }
        }
    }

    public void update() {
        if (this.run && !this.animationEnded) {
            long time = System.currentTimeMillis() - this.startTime;
            Delay delay = this.delay[this.delayIndex];
            if (time >=  delay.getTime()) {
                this.delayIndex++;
                if (this.delayIndex == this.delay.length) {
                    this.player.playSound(this.player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f);
                    this.animationEnded = true;
                    ended();
                }
            } else if (!delay.isDelayed()) {
                this.player.playSound(this.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                if (this.lastItemStackSelectedID != -1) {
                    this.inventoryItemStacks.get(this.lastItemStackSelectedID).removeEnchantment(Enchantment.DURABILITY);
                }
                ItemStack itemStack = this.inventoryItemStacks.get(this.delayIndex % this.inventoryItemStacks.size());
                getInventory().setItem(22, itemStack.clone());
                itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                this.lastItemStackSelectedID = this.delayIndex % this.inventoryItemStacks.size();
                delay.setDelayed(true);
            }
        }
    }

    public boolean isRun() {
        return this.run;
    }

    public boolean isAnimationEnded() {
        return this.animationEnded;
    }

    public void setAnimationEnded(boolean animationEnded2) {
        this.animationEnded = animationEnded2;
    }

    public void ended() {
        if (!isGave) {
            player.getInventory().addItem(getPrize().getItemStack());
            LotteryManager.getLotteryPlayers().remove(player.getName());
            isGave = true;
            if (getPrize().isNotice()) {
                Collection<? extends Player> arrayOfPlayer = Bukkit.getOnlinePlayers();
                int i = arrayOfPlayer.size();
                for (byte b = 0; b < i; b = (byte) (b + 1)) {
                    ((Player) arrayOfPlayer.toArray()[b]).sendMessage("§b[§c小域§b] §a恭喜玩家§e[" + this.player.getName() + "§e]§a使用§e[" + this.hand.getItemMeta().getDisplayName() + "§e]§a抽取到了§e[" + ((!this.prize.getItemStack().hasItemMeta() || !this.prize.getItemStack().getItemMeta().hasDisplayName()) ? this.prize.getItemStack().getType().name() : this.prize.getItemStack().getItemMeta().getDisplayName()) + "§e X " + this.prize.getItemStack().getAmount() + "]");
                }
            }
        }
    }
}
