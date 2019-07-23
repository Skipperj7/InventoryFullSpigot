package me.jacob.inventoryfull;

import org.bukkit.plugin.java.JavaPlugin;

import me.jacob.inventoryfull.listener.listener;

public class Main extends JavaPlugin{

	@Override
	public void onEnable() {
		
		new listener(this);
	}
}
