package me.mafrans.ntiplugin.commands;

import me.mafrans.ntiplugin.Base;
import me.mafrans.ntiplugin.util.NUtil;
import me.mafrans.ntiplugin.util.Schedule;
import me.mafrans.ntiplugin.util.Student;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Command_link extends Base implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length != 1) {
            if(config.contains("messages.help") && config.getString("messages.help").isEmpty()) {
                sender.sendMessage(NUtil.colorize(config.getString("messages.help")));
                sender.sendMessage(NUtil.colorize(config.getString("messages.class_list")));
            }
            else {
                return false;
            }
        }

        Student student = new Student((Player)sender);

        if(config.getStringList("classes").contains(args[0].toUpperCase())) {
            try {
                student.setSchedule(new Schedule(args[0].toUpperCase(), new File(plugin.getDataFolder(), "schedules/" + args[0].toUpperCase() + ".json")));
                plugin.database.addStudent(student);

                plugin.roleHandler.setRole(
                        (Player)sender,
                        student.getSchedule().getName(),
                        NUtil.colorize(ChatColor.DARK_GRAY + "[" + ChatColor.RESET + student.getSchedule().getJson().getString("display") + ChatColor.DARK_GRAY + "] " + ChatColor.RESET),
                        "",
                        ChatColor.RESET
                );
                sender.sendMessage("Du är nu medlem i " + NUtil.colorize(student.getSchedule().getJson().getString("display")));
                student.getPlayer().setGameMode(GameMode.SURVIVAL);
            }
            catch (IOException e) {
                e.printStackTrace();

                if(config.contains("messages.error") && config.getString("messages.error").isEmpty()) {
                    sender.sendMessage(NUtil.colorize(config.getString("messages.error")));
                }
                else {
                    sender.sendMessage(e.toString());
                }
            }
        }
        else if(args[0].equalsIgnoreCase("help")) {
            if(config.contains("messages.help") && config.getString("messages.help").isEmpty()) {
                sender.sendMessage(NUtil.colorize(config.getString("messages.help")));
            }
            else {
                return false;
            }
        }
        else {
            if(config.contains("messages.help") && config.getString("messages.class_list").isEmpty()) {
                sender.sendMessage(NUtil.colorize(config.getString("messages.class_list")));
            }
            else {
                return false;
            }
        }

        return true;
    }
}
