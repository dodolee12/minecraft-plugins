package net.dohaw.claim;

import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClaimPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        CoreLib.setInstance(this);

        JPUtils.registerCommand("claim",new ClaimCommand());
        JPUtils.registerCommand("unclaim",new UnclaimCommand());
        JPUtils.registerCommand("trust",new TrustCommand());
        JPUtils.registerCommand("flags",new FlagsCommand(this));
        JPUtils.registerEvents(new EventListener());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
