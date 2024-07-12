package com.birdflop.claimlog;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ListenerClaimDelete implements Listener {

    @EventHandler
    public void onClaimDelete(ClaimDeletedEvent event) {
        Claim claim = event.getClaim();
        UUID owner = claim.getOwnerID();
        String claimId = claim.getID().toString();

        // Add to deleted claims
        if (owner != null) {
            List<String> state = History.history.getStringList("deleted-claims." + owner);
            state.add(claimId);
            History.history.set("deleted-claims." + owner, state);
        }

        // Add to claim history
        List<Map<?, ?>> rawList = History.history.getMapList("claim-history." + claimId);
        Map<String, String> claimData = new HashMap<>();
        Date date = new Date();
        claimData.put("action", "delete");
        claimData.put("time", String.valueOf(date.getTime()));
        claimData.put("x1", String.valueOf(claim.getLesserBoundaryCorner().getBlockX()));
        claimData.put("x2", String.valueOf(claim.getGreaterBoundaryCorner().getBlockX()));
        claimData.put("z1", String.valueOf(claim.getLesserBoundaryCorner().getBlockZ()));
        claimData.put("z2", String.valueOf(claim.getGreaterBoundaryCorner().getBlockZ()));
        claimData.put("world", claim.getGreaterBoundaryCorner().getWorld().getName());
        rawList.add(claimData);
        History.history.set("claim-history." + claimId, rawList);

        History.save();
    }

}
