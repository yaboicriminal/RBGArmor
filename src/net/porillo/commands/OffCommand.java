package net.porillo.commands;

import static net.porillo.Utility.send;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.porillo.RBGArmor;
import net.porillo.workers.Worker;

public class OffCommand extends BaseCommand {

    public OffCommand(RBGArmor plugin) {
        super(plugin);
        super.setName("off");
        super.addUsage("Disables your armor coloring");
        super.setPermission("rgbarmor.off");
    }

    @Override
    public void runCommand(CommandSender s, List<String> args) {
        if (!this.checkPermission(s)) {
            this.noPermission(s);
            return;
        }
        if(s instanceof Player) {
            Player p = (Player)s;
            final UUID uuid = p.getUniqueId();
            Map<UUID, Worker> workerz = plugin.getWorkers();
            if (workerz.containsKey(uuid)) {
                Worker w = workerz.get(uuid);
                Bukkit.getScheduler().cancelTask(w.getUniqueId());
                workerz.remove(uuid);
                send(p, "&eSuccess. Deactivated your armor.");
            } else {
                send(p, "&cError: Your armor is not updating, &ecant turn off.");
            }
        }
    }

}
