package com.mcstaralliance.starlottery.command;

import com.mcstaralliance.starlottery.gui.MainMenu;
import com.mcstaralliance.starlottery.manager.PrizeInfoManager;
import com.mcstaralliance.starlottery.util.DebugUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StarLotteryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!label.equalsIgnoreCase("sl") || !(sender instanceof Player player)) {
            return false;
        }
        if (args.length < 1 || !args[0].equalsIgnoreCase("debug")) {
            new MainMenu(player);
            return true;
        }
        DebugUtil.setDebugMode(true);
        player.teleport(PrizeInfoManager.getChestWorld().getSpawnLocation());
        return true;
    }
}
