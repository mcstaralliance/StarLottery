package com.mcstaralliance.starlottery.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    public static String rootPath;

    public static FileConfiguration load(String string) {
        File file = new File(rootPath + "/" + string + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void remove(String string) {
        new File(rootPath + "/" + string + ".yml").delete();
    }

    public static void save(FileConfiguration config, String string) {
        try {
            config.save(new File(rootPath + "/" + string + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
