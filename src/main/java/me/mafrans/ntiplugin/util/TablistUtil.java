package me.mafrans.ntiplugin.util;

import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_14_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;

public class TablistUtil {
    public static void setForPlayer(Player p, String header, String footer){

        CraftPlayer craftplayer = (CraftPlayer)p;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent top = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent bottom = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try
        {
            System.out.println(Arrays.toString(packet.getClass().getDeclaredFields()));
            Field headerField = packet.getClass().getDeclaredFields()[0];
            headerField.setAccessible(true);
            headerField.set(packet, top);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredFields()[1];
            footerField.setAccessible(true);
            footerField.set(packet, bottom);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception ev) {
            ev.printStackTrace();
        }

        connection.sendPacket(packet);
    }
}