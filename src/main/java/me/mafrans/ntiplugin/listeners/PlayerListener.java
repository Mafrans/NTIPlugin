package me.mafrans.ntiplugin.listeners;

import me.mafrans.ntiplugin.Base;
import me.mafrans.ntiplugin.util.NUtil;
import me.mafrans.ntiplugin.util.Student;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;

public class PlayerListener  extends Base implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Student student = new Student(e.getPlayer());

        if(student.getSchedule() == null) {
            e.getPlayer().sendMessage(NUtil.colorize(config.getString("messages.join_unlinked")));
            return;
        }

        if(student.getSchedule().hasLesson(new Date())) {
            e.setJoinMessage("");
            e.getPlayer().kickPlayer(NUtil.colorize(config.getString("messages.lesson")));
            return;
        }

        if(config.contains("messages.join") && config.getString("messages.join").isEmpty()) {
            e.getPlayer().sendMessage(NUtil.colorize(config.getString("messages.join")));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Student student = new Student(e.getPlayer());

        if(student.getSchedule() == null) {
            return;
        }

        if(student.getSchedule().hasLesson(new Date())) {
            e.setQuitMessage("");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Student student = new Student(e.getPlayer());

        if(student.getSchedule() == null) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Student student = new Student(e.getPlayer());

        if(student.getSchedule() == null) {
            e.setCancelled(true);
            return;
        }
    }
}
