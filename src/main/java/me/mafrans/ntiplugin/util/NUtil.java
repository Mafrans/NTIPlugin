package me.mafrans.ntiplugin.util;

import me.mafrans.ntiplugin.Base;
import org.bukkit.ChatColor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NUtil extends Base {
    public static String[] readFile(File file) throws IOException {
        if(plugin.debug) {
            logger.warning("Reading from file...");
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> list = new ArrayList<>();

        String line;
        while((line = reader.readLine()) != null) {
            list.add(line);
        }

        return list.toArray(new String[0]);
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
