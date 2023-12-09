package it.mandress101.sac;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import it.mandress101.sac.listener.TabListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class SAC_AD extends JavaPlugin {
    private static SAC_AD pl;
    public static ProtocolManager manager;
    public static File txt;
    public boolean isLogs;

    @Override
    public void onEnable() {
        pl = this;
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().severe("PROTOCOL LIB IS NULL DISABLING PLUGIN");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if(Bukkit.getPluginManager().getPlugin("SAC") == null) {
            getLogger().severe("SAC IS NULL DISABLING PLUGIN");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        manager = ProtocolLibrary.getProtocolManager();
        File configFile = new File(getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            saveDefaultConfig();
        }
        getConfig().options().copyDefaults(true);
        txt = new File(getDataFolder(), "logs.txt");
        isLogs = getConfig().getBoolean("features.anti-tab-complete.logs.enabled");
        if(isLogs) {
            if(!txt.exists()) {
                try {
                    txt.createNewFile();
                } catch (IOException ignored) {}
            }
        }
        new TabListener(this, isLogs);
    }

    public static SAC_AD getPlugin() {
        return pl;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
