package de.bananaco.gen;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Gen extends JavaPlugin {
	
	public static JavaPlugin plugin;
	
	ChunkGenerator gen = new de.bananaco.gen.ChunkGenerator();

	@Override
	public void onLoad() {
		plugin = this;
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return gen;
	}

}
