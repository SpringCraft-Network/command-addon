package it.mandress101.sac.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import it.mandress101.sac.SAC_AD;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabListener {
    private final Map<String, Boolean> allowed;
    private final boolean isLogs2;

    public TabListener(Plugin plugin, boolean isLogs) {
        this.isLogs2 = isLogs;
        this.allowed = new HashMap<>();
        List<String> commands = SAC_AD.getPlugin().getConfig().getStringList("features.anti-tab-complete.list");
        for(String command : commands) {
            allowed.put(command, true);
        }

        SAC_AD.manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.MONITOR, PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {

                String inculati = event.getPacket().getStrings().read(0).toLowerCase();

                if(inculati.startsWith("/")) {
                    String commandDIO = inculati.split(" ")[0].substring(1);

                    if(!allowed.containsKey(commandDIO)) {
                        event.setCancelled(true);
                    }
                    if(isLogs2) {
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                        String playerName = event.getPlayer().getName();
                        String format = playerName + " has flagged TabListener in date " + time + " command: " + commandDIO;
                        saveLog(format);
                    }
                }
            }
        });
    }

    public void saveLog(String format) {
        try {
            File dataFolder = SAC_AD.getPlugin().getDataFolder();
            if(!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            FileWriter fw = new FileWriter(SAC_AD.txt, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(format);
            pw.flush();
            pw.close();
        } catch (IOException ignore) {}
    }
}
