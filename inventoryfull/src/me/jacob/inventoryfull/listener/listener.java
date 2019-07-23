package me.jacob.inventoryfull.listener;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.jacob.inventoryfull.Main;
import me.jacob.inventoryfull.utils.utils;

public class listener implements Listener {

	private Main plugin;
	
	public listener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
    public void sendPacket(Player player, Object packet) {
        try {
                Object handle = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
                playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
       
        catch (Exception e) {
                e.printStackTrace();
        }
    }

    public Class<?> getNMSClass(String name) {
        // org.bukkit.craftbukkit.v1_8_R3...
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
                return Class.forName("net.minecraft.server." + version + "." + name);
        }
       
        catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
        }
    }
    public void sendTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
        try {
            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            Object enumSubTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\""+title+"\"}");
            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(enumTitle, chat, fadeIn, stay, fadeOut);
            
            sendPacket(player, packet);
            
        }
        catch (Exception e) {
        	
        }
    }
        
    @EventHandler
	 public void onBlockBreak(BlockBreakEvent e) {
        Player p = (Player) e.getPlayer();
        int space = 0;
        
        for (int i =0; i<=35;i++)
        {
            ItemStack content = p.getInventory().getItem(i);
        
            if(content == null) {
                space++;
            }
            else if(content.getAmount() != content.getMaxStackSize()) {
            	space++;
            }
        }
        
        
        //p.sendMessage("Space: " + space);
        if(!(space > 0)) {
            // not enough space
        	sendTitle(p, utils.chat("&cInventory Full!"), 10, 20, 10);
        }
    }
	
}	
