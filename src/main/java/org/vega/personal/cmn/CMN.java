package org.vega.personal.cmn;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.vega.personal.cmn.Logic.Logic;
import org.vega.personal.cmn.Utils.Commands;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public final class CMN extends JavaPlugin implements Listener {
    private Commands commands;
    public static String version;
    public static List<String> cItemName = new ArrayList<>();
    public static List<Integer> cItemId = new ArrayList<>();
    public static CMN INSTANCE;
    public static Logger logger;
    @Override
    public void onEnable() {
        INSTANCE = this;
        logger = INSTANCE.getLogger();
        getServer().getPluginManager().registerEvents(INSTANCE, INSTANCE);
        saveDefaultConfig();
        loadConfig();
        Logic logic = new Logic();
        logic.registerEvents();
        commands = new Commands();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return commands.onCommand(sender, command, label, args);
    }

    public void loadConfig() {
        //Making config var for easy use
        FileConfiguration config = getConfig();
        //Strings load
        try {
            // Strings load
            version = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("version")));
            List<String> itemList = config.getStringList("predefined");
            for (String item : itemList) {
                String[] parts = item.split(":");
                if (parts.length == 2) {
                    cItemName.add(parts[0].trim());
                    cItemId.add(Integer.parseInt(parts[1].trim()));
                }
            }
        } catch (Exception e) {
            // IF SMT BROKEN REPORT HERE
            logger.severe("\n[CMN] Failed to Init config.yml\nReport of stacktrace:\n" + e);
            errorConfig();
        }
    }
    public void errorConfig(){
        FileConfiguration config = getConfig();
        File configFile = new File(getDataFolder(), "config.yml");
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File brokenConfigFile = new File(getDataFolder(), timestamp + "-broken-config.yml");
        configFile.renameTo(brokenConfigFile);
        // Load default config
        saveDefaultConfig();
        reloadConfig();
        loadConfig();
        version = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("version")));
    }
}
