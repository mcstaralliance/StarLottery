package com.mcstaralliance.starlottery.manager;

import java.util.HashMap;

public class LotteryManager {
    private static final HashMap<String, Integer> SETTING_ODDS_OPS = new HashMap<>();
    private static final HashMap<String, InventoryManager> SETTING_OPS = new HashMap<>();
    private static final HashMap<String, String> CREATING_OPS = new HashMap<>();
    private static final HashMap<String, Integer> SELECT_POOL_OPS = new HashMap<>();
    private static final HashMap<String, PlayerLotteryManager> LOTTERY_PLAYERS = new HashMap<>();
    public static HashMap<String, String> getCreatingOps() {
        return CREATING_OPS;
    }
    public static HashMap<String, Integer> getSelectPoolOps() {
        return SELECT_POOL_OPS;
    }
    public static HashMap<String, PlayerLotteryManager> getLotteryPlayers() {
        return LOTTERY_PLAYERS;
    }
    public static HashMap<String, InventoryManager> getSettingOps() {
        return SETTING_OPS;
    }

    public static HashMap<String, Integer> getSettingOddsOps() {
        return SETTING_ODDS_OPS;
    }
}
