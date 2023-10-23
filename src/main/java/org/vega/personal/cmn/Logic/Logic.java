package org.vega.personal.cmn.Logic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.vega.personal.cmn.CMN;

import static org.vega.personal.cmn.CMN.*;

public class Logic implements Listener {
    public Logic() {
    }
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, CMN.getPlugin(CMN.class));
    }
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        Player player = (Player) event.getView().getPlayer();
        ItemStack resultItem = event.getResult();

        if (resultItem != null && resultItem.getType() != Material.AIR) {
            ItemMeta itemMeta = resultItem.getItemMeta();

            if (itemMeta != null && itemMeta.hasDisplayName()) {
                String displayName = itemMeta.getDisplayName();

                String strippedName = ChatColor.RESET + displayName;

                for (int i = 0; i < cItemName.size(); i++) {
                    String itemName = cItemName.get(i);
                    if (displayName.equalsIgnoreCase(itemName.replaceAll("&.", ""))) {
                        // Apply custom model data to itemMeta
                        itemMeta.setCustomModelData(cItemId.get(i));
                        strippedName = ChatColor.RESET + itemName;
                        break;
                    }
                }
                if (strippedName.matches(".* \\[CM:\\d+]$") && player.hasPermission("cmn.cmd")) {
                    // Extract the CustomModelData value from the display name
                    String customModelDataString = strippedName.substring(strippedName.lastIndexOf("[CM:") + 4, strippedName.lastIndexOf(']'));
                    int customModelData = Integer.parseInt(customModelDataString);

                    // Remove the CustomModelData from the display name
                    strippedName = strippedName.replaceFirst(" \\[CM:\\d+]", "");
                    itemMeta.setCustomModelData(customModelData);
                }
                if (player.hasPermission("cmn.color")) {
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',strippedName));
                } else {
                    itemMeta.setDisplayName(strippedName);
                }
                resultItem.setItemMeta(itemMeta);
                // Save the modified item back to the inventory
                event.setResult(resultItem);
            }
        }
    }
}
