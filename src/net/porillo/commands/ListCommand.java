package net.porillo.commands;

import static net.porillo.Utility.getSym;
import static net.porillo.Utility.send;

import java.util.List;

import org.bukkit.command.CommandSender;

import net.porillo.RainbowGear;

public class ListCommand extends BaseCommand {

    public ListCommand(RainbowGear plugin) {
        super(plugin);
        super.setName("list");
        super.addUsage("List available coloring modes");
    }

    @Override
    public void runCommand(CommandSender s, List<String> args) {
        String dash = getSym("-", 8);
        send(s, "&2" + dash + " &9Available Modes&2 " + dash);
        send(s, "- &bSync: &3All armor pieces are updated with same color");
        send(s, "- &bFade: &3Every armor update, next color is used");
        send(s, "- &bHelath: &3Armor color is based on health");
    }
}
