package com.birdflop.claimlog;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class ListenerClaimCreate implements Listener {
    @EventHandler

    public void onClaimCreate(ClaimCreatedEvent event) {
        Claim claim = event.getClaim();
        String claimId = claim.getID().toString();

        // Add to claim history
        Map<String, String> claimData = new HashMap<>();
        Date date = new Date();
        claimData.put("action", "create");
        claimData.put("time", String.valueOf(date.getTime()));
        claimData.put("x1", String.valueOf(claim.getLesserBoundaryCorner().getBlockX()));
        claimData.put("x2", String.valueOf(claim.getGreaterBoundaryCorner().getBlockX()));
        claimData.put("z1", String.valueOf(claim.getLesserBoundaryCorner().getBlockZ()));
        claimData.put("z2", String.valueOf(claim.getGreaterBoundaryCorner().getBlockZ()));

        List<Map<String, String>> rawList = new ArrayList<>();
        rawList.add(claimData);
        History.history.set("claim-history." + claimId, rawList);

        History.save();
    }
}
