package com.birdflop.claimlog;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Util {

    public static String stringToHexColor(String str) {
        int hash = str.hashCode(); // Convert string to integer hash
        hash = hash & 0xffffff; // Keep only the lower 24 bits (for RGB values)

        // Extract individual color components (red, green, blue) from the hash
        int red = (hash >> 16) & 0xff;
        int green = (hash >> 8) & 0xff;
        int blue = hash & 0xff;

        // Convert each component to a two-digit hexadecimal string with leading zeros
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    public static String getClaimHistoryString(@NotNull String claim) {
        List<Map<?,?>> claimLog = History.history.getMapList("claim-history." + claim);
        if (claimLog.isEmpty()) return Lang.NO_CLAIM_HISTORY.replace("{claim}", claim);
        ArrayList<String> lines = new ArrayList<>();
        for (Map<?,?> map : claimLog) {
            String action = (String) map.get("action");
            String line = "<red>Error: Line has no action";
            if (action.equals("create")) {
                line = Lang.CLAIM_HISTORY_CREATED;
            } else if (action.equals("resize")) {
                line = Lang.CLAIM_HISTORY_RESIZED;
            } else if (action.equals("delete")) {
                line = Lang.CLAIM_HISTORY_DELETED;
            }
            for (Object key : map.keySet()) {
                String val = (String) map.get(key);
                if (key.equals("time")) {
                    long time = Long.parseLong(val);
                    Date date = new Date(time);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(Lang.TIME_FORMAT);
                    line = line.replace("{" + key + "}", dateFormat.format(date));
                } else {
                    line = line.replace("{" + key + "}", val);
                }
            }
            lines.add(line);
        }
        return String.join("<newline>", lines);
    }
}
