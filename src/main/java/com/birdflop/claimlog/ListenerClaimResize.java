package com.birdflop.claimlog;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.events.ClaimResizeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class ListenerClaimResize implements Listener {

    @EventHandler
    public void onClaimResized(ClaimResizeEvent event) {
        Claim oldClaim = event.getFrom();
        Claim newClaim = event.getTo();
        String claimId = oldClaim.getID().toString();

        // Add to claim history
        Map<String, String> claimData = new HashMap<>();
        Date date = new Date();
        claimData.put("action", "resize");
        claimData.put("time", String.valueOf(date.getTime()));
        claimData.put("old-x1", String.valueOf(oldClaim.getLesserBoundaryCorner().getBlockX()));
        claimData.put("old-x2", String.valueOf(oldClaim.getGreaterBoundaryCorner().getBlockX()));
        claimData.put("old-z1", String.valueOf(oldClaim.getLesserBoundaryCorner().getBlockZ()));
        claimData.put("old-z2", String.valueOf(oldClaim.getGreaterBoundaryCorner().getBlockZ()));
        claimData.put("new-x1", String.valueOf(newClaim.getLesserBoundaryCorner().getBlockX()));
        claimData.put("new-x2", String.valueOf(newClaim.getGreaterBoundaryCorner().getBlockX()));
        claimData.put("new-z1", String.valueOf(newClaim.getLesserBoundaryCorner().getBlockZ()));
        claimData.put("new-z2", String.valueOf(newClaim.getGreaterBoundaryCorner().getBlockZ()));

        List<Map<?, ?>> rawList = History.history.getMapList("claim-history." + claimId);
        rawList.add(claimData);
        History.history.set("claim-history." + claimId, rawList);

        History.save();
    }
}
