package com.birdflop.claimlog;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class History {
    public static YamlConfiguration history;
    public static File historyFile;

    public static void save() {
        try {
            history.save(historyFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reload() {
        historyFile = new File(ClaimLog.plugin.getDataFolder(), "history.yml");

        if (!historyFile.exists()) {
            try {
                historyFile.createNewFile();
            } catch (IOException e) {
                ClaimLog.plugin.getLogger().severe("Could not create history.yml file!");
                return;
            }
        }

        history = YamlConfiguration.loadConfiguration(historyFile);
    }
}
