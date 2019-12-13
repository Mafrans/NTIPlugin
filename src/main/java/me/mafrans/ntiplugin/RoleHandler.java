package me.mafrans.ntiplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class RoleHandler extends Base {
    private ScoreboardManager manager;
    private Scoreboard sb;

    public RoleHandler(ScoreboardManager manager) {
        this.manager = manager;
        this.sb = manager.getNewScoreboard();
    }

    public RoleHandler(Scoreboard scoreboard) {
        this.sb = scoreboard;
        this.manager = Bukkit.getScoreboardManager();
    }

    public RoleHandler(ScoreboardManager manager, Scoreboard scoreboard) {
        this.manager = manager;
        this.sb = scoreboard;
    }

    public RoleHandler() {
        this.manager = Bukkit.getScoreboardManager();
        this.sb = manager.getNewScoreboard();
    }


    public void setRole(Player player, String team, String prefix, String suffix, ChatColor color) {
        Team role;

        if(sb.getTeam(team) != null) {
            role = sb.getTeam(team);
        }
        else {
            role = sb.registerNewTeam(team);
        }

        role.setPrefix(prefix);
        role.setSuffix(suffix);
        role.setColor(color);
        role.addEntry(player.getName());

        player.setScoreboard(sb);
    }
}