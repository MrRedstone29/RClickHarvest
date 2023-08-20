package fr.mrredstone29.rclickharvest;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mrredstone29.rclickharvest.events.CropsListener;

public class RClickHarvest extends JavaPlugin {
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new CropsListener(), this);
		System.out.println("RClickHarvest loaded !");
	}
}
