package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class UnderSea extends BlockPopulator {

	int numChecks = 32;
	Material[] types = {Material.COBBLESTONE, Material.STONE, Material.DIRT, Material.SAND};
	
	int probability = 50;
	
	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		if(rand.nextInt(100) > probability)
			return;
		
		for(int i=0; i<rand.nextInt(numChecks); i++) {
			Block block = chunk.getBlock(rand.nextInt(16), 60, rand.nextInt(16));
			if(block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
				Block rel = block;
				while(rel.getType() == block.getType() && rel.getY() > 40) {
					rel = rel.getRelative(BlockFace.DOWN);
				}
				// And set to the type
				if(rel.getType() == Material.WATER) {}
				else if(rel.getType() == Material.STATIONARY_WATER) {}
				else {
					Material type = types[rand.nextInt(types.length)];
					// Do the calc, set the type
					if(rand.nextBoolean())
						rel.getRelative(BlockFace.UP).setType(type);
					rel.setType(type);
				}
			}
		}
	}

	
	
}
