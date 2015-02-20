package net.porillo;

import java.util.ArrayList;
import java.util.List;

import net.porillo.workers.FadeWorker;
import net.porillo.workers.HealthWorker;
import net.porillo.workers.SyncWorker;
import net.porillo.workers.Worker;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Utility {

    /**
     * Gets a repeated string
     * 
     * @param s Symbol
     * @param num of symbols
     * @return String of symbols with length num
     */
    public static String getSym(String s, int num) {
        StringBuilder sb = new StringBuilder(num);
        for (int i = 0; i < num; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Sets lore to a players armor
     * 
     * @param p Player to set lore for armor
     * @param str Lore string
     */
    public static void setLore(Player p, String str) {
        List<String> lores = new ArrayList<String>();
        lores.add(str);
        ItemMeta meta;
        for (ItemStack is : p.getInventory().getArmorContents()) {
            if (is != null && (meta = is.getItemMeta()) instanceof LeatherArmorMeta) {
                if (meta.hasLore()) {
                    List<String> localAdd = new ArrayList<String>();
                    for (String l : meta.getLore()) {
                        if (!l.startsWith("RG|")) {
                            localAdd.add(l);
                        }
                    }
                    lores.addAll(localAdd);
                }
                meta.setLore(lores);
                is.setItemMeta(meta);
            }
        }
    }

    /**
     * 
     * @param meta
     * @return true if item has correct lore
     */
    public static boolean isWorthy(ItemMeta meta) {
        if (meta.hasLore()) {
            for(String s : meta.getLore()) {
                if(s.startsWith("RG|")) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Gets the worker based on lore
     * @param p Player
     * @param lores list of lore
     * @return a new worker instance based on the lore
     */
    public static Worker getWorker(Player p, List<String> lores) {
        for (String lore : lores) {
            if (lore.equals("RG|Fade")) {
                return new FadeWorker(p.getUniqueId());
            } else if (lore.equals("RG|Sync")) {
                return new SyncWorker(p.getUniqueId());
            } else if (lore.equals("RG|Health")) {
                return new HealthWorker(p.getUniqueId());
            }
        }
        return null;
    }

    public static void send(Player player, String str) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
    } 
}
