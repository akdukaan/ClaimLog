package com.birdflop.claimlog;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class CommandClaimLog implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false; // show usage
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("claimlog.command.reload")) {
                Lang.sendMessage(sender, Lang.COMMAND_NO_PERMISSION);
                return true;
            }

            Config.reload();
            Lang.reload();
            History.reload();

            Lang.sendMessage(sender, "<green>" + ClaimLog.plugin.getName() + " v" + ClaimLog.plugin.getDescription().getVersion() + " reloaded");
            return true;
        }

        if (args[0].equalsIgnoreCase("here")) {
            if (!(sender instanceof Player)) {
                Lang.sendMessage(sender, Lang.COMMAND_PLAYERS_ONLY);
                return true;
            }
            Player player = (Player) sender;
            if (!sender.hasPermission("claimlog.command.here")) {
                Lang.sendMessage(sender, Lang.COMMAND_NO_PERMISSION);
                return true;
            }
            String claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null).getID().toString();
            if (claim.equalsIgnoreCase("")) {
                Lang.sendMessage(player, Lang.CLAIM_DOESNT_EXIST);
            }
            String claimHistory = Util.getClaimHistoryString(claim);
            Lang.sendMessage(sender, claimHistory);
            return true;
        }

        if (args[0].equalsIgnoreCase("player")) {
            if (!sender.hasPermission("claimlog.command.player")) {
                Lang.sendMessage(sender, Lang.COMMAND_NO_PERMISSION);
                return true;
            }
            if (args.length <= 1) {
                return false; // show usage
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (target.getName() == null) {
                String message = Lang.PLAYER_DOESNT_EXIST.replace("{player}", args[1]);
                Lang.sendMessage(sender, message);
                return true;
            }
            String message = Lang.CLAIM_LIST_PLAYER;
            message = message.replace("{player}", target.getName());

            Vector<Claim> claims = GriefPrevention.instance.dataStore.getPlayerData(target.getUniqueId()).getClaims();
            StringBuilder claimsList = new StringBuilder();
            for (Claim claim : claims) {
                String claimHistory = Util.getClaimHistoryString(String.valueOf(claim.getID()));
                String color = Util.stringToHexColor(claim.getLesserBoundaryCorner().getWorld().getName());
                claimsList.append("<").append(color).append(">")
                        .append("<hover:show_text:\"").append(claimHistory).append("\">")
                        .append("<click:suggest_command:/claimlog claim ").append(claim.getID()).append(">")
                        .append(claim.getID()).append("<reset> ");
            }
            List<String> deletedClaims = History.history.getStringList("deleted-claims." + target.getUniqueId());
            for (String claim : deletedClaims) {
                String claimHistory = Util.getClaimHistoryString(claim);
                List<Map<?,?>> claimLog = History.history.getMapList("claim-history." + claim);
                String world = (String) claimLog.getLast().get("world");
                String color = Util.stringToHexColor(world);
                claimsList.append("<").append(color).append("><st>")
                        .append("<hover:show_text:\"").append(claimHistory).append("\">")
                        .append("<click:suggest_command:/claimlog claim ").append(claim).append(">")
                        .append(claim).append("<reset> ");
            }
            message = message.replace("{claims}",claimsList.toString());
            Lang.sendMessage(sender, message);

            return true;
        }

        if (args[0].equalsIgnoreCase("claim")) {
            if (!sender.hasPermission("claimlog.command.claim")) {
                Lang.sendMessage(sender, Lang.COMMAND_NO_PERMISSION);
                return true;
            }
            if (args.length <= 1) {
                return false; // show usage
            }

            String claimHistory = Util.getClaimHistoryString(args[1]);
            Lang.sendMessage(sender, Lang.CLAIM_HISTORY_HEADER.replace("{claim}", args[1]));
            Lang.sendMessage(sender, claimHistory);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("claimlog.command.here")) {
                list.add("here");
            }
            if (sender.hasPermission("claimlog.command.claim")) {
                list.add("claim");
            }
            if (sender.hasPermission("claimlog.command.player")) {
                list.add("player");
            }
            if (sender.hasPermission("claimlog.command.reload")) {
                list.add("reload");
            }
            return StringUtil.copyPartialMatches(args[0], list, new ArrayList<>());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("player")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                list.add(player.getName());
            }
        }

        return list;
    }
}
