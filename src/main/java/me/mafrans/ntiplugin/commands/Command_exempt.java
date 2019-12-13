package me.mafrans.ntiplugin.commands;

import me.mafrans.ntiplugin.Base;
import me.mafrans.ntiplugin.util.NUtil;
import me.mafrans.ntiplugin.util.Schedule;
import me.mafrans.ntiplugin.util.Student;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Command_exempt extends Base implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
            sender.sendMessage("Currently exempted players:");
            for(UUID uuid : Schedule.getExemptedPlayers().keySet()) {
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
                Integer time = Schedule.getExemptedPlayers().get(uuid);

                sender.sendMessage("- " + offlinePlayer.getName() + ": " + time + " minutes");
            }
            return true;
        }

        if(args.length != 2) {
            return false;
        }

        OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[0]);

        if(Schedule.getTimeMinutes(args[1]) > 0) {
            Schedule.exempt(player.getUniqueId(), args[1]);
            sender.sendMessage("Exempted " + player.getName() + " until " + args[1]);
        }
        else {
            sender.sendMessage("Invalid time format, please use HH:mm");
        }

        return true;
    }
}
