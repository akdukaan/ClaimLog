package com.birdflop.claimlog;

import org.bukkit.plugin.java.JavaPlugin;

public final class ClaimLog extends JavaPlugin {

    public static ClaimLog plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Config.reload();
        Lang.reload();
        History.reload();

        getServer().getPluginManager().registerEvents(new ListenerClaimDelete(), this);
        getServer().getPluginManager().registerEvents(new ListenerClaimCreate(), this);

        getCommand("claimlog").setExecutor(new CommandClaimLog());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
