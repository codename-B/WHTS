package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;

public class GiantCavePopulator extends BlockPopulator {

	private int chance = 17;
	private int iterate = 125;
	
	private static boolean populating = false;
	
	private Material[] stalactities = {Material.COBBLESTONE, Material.IRON_ORE, Material.COBBLESTONE, Material.GOLD_ORE, Material.COBBLESTONE, Material.COBBLESTONE};
	
	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		if(populating)
			return;
		
		
		// Randomness factor
		if(rand.nextInt(1000) > chance)
			return;
		// Random height
		int height = 20+rand.nextInt(20);
		// Random block
		Block block = chunk.getBlock(rand.nextInt(16), height, rand.nextInt(16));
		
		populating = true;
		createOblong(block, rand);
		populating = false;
	}
	
	public void createOblong(Block block, Random rand) {
		System.out.println("Generating oblong: "+block.getX()+","+block.getZ());
		Vector v = new Vector(0, 0, 0);
		int radius = 10+rand.nextInt(20);
		// Create the giant oblong
		Block rel;
		for(int x=-radius; x<=radius; x++)
			for(int z=-radius; z<=radius; z++)
				for(int y=-radius/2; y<=radius/2; y++) {
					Vector d = new Vector(x, y*2, z);
					if(v.distance(d) < radius) {
						rel = block.getRelative(x, y, z);
						if(rel.getType() != Material.AIR && rel.getType() != Material.BEDROCK && rel.getType() != Material.WATER && rel.getType() != Material.STATIONARY_WATER) {
							rel.setType(Material.AIR);
						}
					}
				}
		for(int x=-radius; x<=radius; x++)
			for(int z=-radius; z<=radius; z++) {
					int y = rand.nextInt(radius/4);
					Vector d = new Vector(x, y*2, z);
					if(v.distance(d) < radius && rand.nextInt(35) > 9) {
						rel = block.getRelative(x, y, z);
						if(rel.getType() != Material.BEDROCK && rel.getType() != Material.WATER && rel.getType() != Material.STATIONARY_WATER) {
							while((rel = rel.getRelative(0, 1, 0)).getType() == Material.AIR && rel.getY() < 70) {
								rel.setType(stalactities[rand.nextInt(stalactities.length)]);
							}
						}
					}
				}		
		// Iterate to the next one
		if(rand.nextInt(1000) < iterate) {
			rel = block.getRelative(rand.nextInt(radius)-radius/2, 0, rand.nextInt(radius)-radius/2);
			createOblong(rel, rand);
		}
	}
	
	

}
