package net.porillo;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class RBGArmor extends JavaPlugin implements Listener {

    private Map<UUID, Worker> workers;
    public static Color[] rb;

    @Override
    public void onEnable() {
        workers = new HashMap<UUID, Worker>();
        rb = new Color[64];
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

            @Override
            public void run() {
                final int colors = 64;
                final double f = (6.48 / (double) colors);
                for (int i = 0; i < colors; ++i) {
                    double r = sin(f * i + 0.0D) * 127.0D + 128.0D;
                    double g = sin(f * i + (2 * PI / 3)) * 127.0D + 128.0D;
                    double b = sin(f * i + (4 * PI / 3)) * 127.0D + 128.0D;
                    rb[i] = Color.fromRGB((int) r, (int) g, (int) b);
                }
            }
        });
    }

    @Override
    public void onDisable() {
        workers.clear();
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        handler.runCommand(s, l, a);
        return true;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        this.removeUUID(e.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        this.removeUUID(e.getEntity().getUniqueId());
    }
    
    
    // no idea if the code below will work as I am in class
    @EventHandler
    public void handleJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(!workers.containsKey(player.getUniqueId())) {
            if(player.isOp()) {
                Worker worker = (Worker) new SyncWorker(player.getUniqueId());
                if(worker != null) {
                    initWorker(p, worker);
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (!workerz.containsKey(p.getUniqueId())) {
            // use a little CPU to prevent network abuse
            // if player has rgb armor, but no perms, and spam sneaks
            // they would trigger a TON of message sends (lines 131, 133)
            if(sneaks.containsKey(p.getUniqueId())) {
                long last = sneaks.get(p.getUniqueId());
                if(System.currentTimeMillis() - last < 5000) {
                    return;
                }
                sneaks.remove(p.getUniqueId());
            }
            for (ItemStack is : p.getInventory().getArmorContents()) {
                ItemMeta meta;
                if (is != null && (meta = is.getItemMeta()) instanceof LeatherArmorMeta) {
                    if (meta.hasLore()) {
                        Worker worker = getWorker(p, meta.getLore());
                        if (worker != null) {
                            this.initWorker(p, worker);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void initWorker(Player p, Worker rw) {
        BukkitTask id = Bukkit.getScheduler().runTaskTimer(this, rw, 5, 5);
        rw.setUniqueId(id.getTaskId());
        workers.put(p.getUniqueId(), rw);
    }

    private void removeUUID(UUID uuid) {
        if (workers.containsKey(uuid)) {
            Worker w = workers.get(uuid);
            Bukkit.getScheduler().cancelTask(w.getUniqueId());
            workers.remove(uuid);
        }
    }

    public Map<UUID, Worker> getWorkers() {
        return this.workerz;
    }

    public Map<UUID, DebugWindow> getDebuggers() {
        return this.debuggers;
    }
}
