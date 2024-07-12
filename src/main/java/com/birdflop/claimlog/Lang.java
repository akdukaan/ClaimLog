package com.birdflop.claimlog;

import com.google.common.base.Throwables;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Lang {
    private static YamlConfiguration config;

    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss z";
    public static String COMMAND_NO_PERMISSION = "<red>You do not have permission for that command.";
    public static String CLAIM_HISTORY_HEADER = "<orange>Showing claim history for {claim}";
    public static String CLAIM_HISTORY_CREATED = "<yellow>Created at {time} spanning from ({x1},{z1}) to ({x2},{z2})";
    public static String CLAIM_HISTORY_RESIZED = "<yellow>Resized at {time}. Old: ({old-x1},{old-z1}) to ({old-x2},{old-z2}). New: ({new-x1},{new-z1}) to ({new-x2},{new-z2})";
    public static String CLAIM_HISTORY_DELETED = "<yellow>Deleted at {time} spanning from ({x1},{z1}) to ({x2},{z2}) in {world}";
    public static String CLAIM_LIST_PLAYER = "<orange>All claims for {player}: {claims}";
    public static String PLAYER_DOESNT_EXIST = "<red>Player {player} was not found.";
    public static String CLAIM_DOESNT_EXIST = "<red>Claim not found.";
    public static String NO_CLAIM_HISTORY = "<orange>Claim {claim} has no history.";
    public static String COMMAND_PLAYERS_ONLY = "<red>That command can only be used by players.";



    private static void init() {
        TIME_FORMAT = getString("time-format", TIME_FORMAT);
        COMMAND_NO_PERMISSION = getString("command-no-permission", COMMAND_NO_PERMISSION);
        CLAIM_HISTORY_HEADER = getString("claim-history-header", CLAIM_HISTORY_HEADER);
        CLAIM_HISTORY_CREATED = getString("claim-history-created", CLAIM_HISTORY_CREATED);
        CLAIM_HISTORY_RESIZED = getString("claim-history-resized", CLAIM_HISTORY_RESIZED);
        CLAIM_HISTORY_DELETED = getString("claim-history-deleted", CLAIM_HISTORY_DELETED);
        CLAIM_LIST_PLAYER = getString("claim-list-player", CLAIM_LIST_PLAYER);
        PLAYER_DOESNT_EXIST = getString("player-doesnt-exist", PLAYER_DOESNT_EXIST);
        CLAIM_DOESNT_EXIST = getString("claim-doesnt-exist", CLAIM_DOESNT_EXIST);
        NO_CLAIM_HISTORY = getString("no-claim-history", NO_CLAIM_HISTORY);
        COMMAND_PLAYERS_ONLY = getString("command-players-only", COMMAND_PLAYERS_ONLY);
    }

    // ############################  DO NOT EDIT BELOW THIS LINE  ############################

    /**
     * Reload the language file
     */
    public static void reload() {
        File configFile = new File(ClaimLog.plugin.getDataFolder(), Config.LANGUAGE_FILE);
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load " + Config.LANGUAGE_FILE + ", please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header("This is the main language file for " + ClaimLog.plugin.getName());
        config.options().copyDefaults(true);

        Lang.init();

        try {
            config.save(configFile);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + configFile, ex);
        }
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    /**
     * Sends a message to a recipient
     *
     * @param recipient Recipient of message
     * @param message   Message to send
     */
    public static void sendMessage(@NotNull CommandSender recipient, String message) {
        MiniMessage mm = MiniMessage.miniMessage();
        Component component = mm.deserialize(message);
        Audience.audience(recipient).sendMessage(component);
    }
}