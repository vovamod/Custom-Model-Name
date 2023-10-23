package org.vega.personal.cmn.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import static org.vega.personal.cmn.CMN.INSTANCE;

public class Commands implements CommandExecutor {
    public Commands() {
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("creload")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("cmn.reload") || sender.isOp()) {
                try {
                    INSTANCE.reloadConfig();
                    INSTANCE.loadConfig();
                    sender.sendMessage("CMN reloaded");
                } catch (Exception ignored) {
                }
            } else {
                sender.sendMessage("No permission!");
            }
            return true;
        }
        return false;
    }
}
