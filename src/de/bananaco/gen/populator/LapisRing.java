package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;

/**
 * A "geode" like formation with a lava pool in the center 
 */
public class LapisRing extends BlockPopulator {

	int chance = 13;

	@Override
	public void populate(World world, Random rand, Chunk chunk) {

		if(rand.nextInt(100) > chance)
			return;

		Block block = chunk.getBlock(rand.nextInt(16), 32+rand.nextInt(64), rand.nextInt(16));

		if(block.getType() == Material.AIR || block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER)
			return;

		createRing(world, block, 5+rand.nextInt(5));
	}

	public void createRing(World world, Block block, int radius) {
		Vector v = new Vector(0, 0, 0);

		for(int x=-radius; x<=radius; x++)
			for(int z=-radius; z<=radius; z++)
				for(int y=0; y<=3; y++) {
					Vector d = new Vector(x, 0, z);
					double distance = d.distance(v);
					Block relative = block.getRelative(x, y, z);
					if(relative.getType() == Material.OBSIDIAN ||
							relative.getType() == Material.CLAY ||
							relative.getType() == Material.STONE) {
						// Now a distance calc
						if(distance < radius-3) {
							if(y==0)
								relative.setType(Material.LAVA);
							else
								relative.setType(Material.AIR);
						} else if(distance < radius-2) {
							relative.setType(Material.LAPIS_BLOCK);
						} else if(distance < radius-1) {
							relative.setType(Material.LAPIS_ORE);
						} else if(distance < radius) {
							relative.setType(Material.OBSIDIAN);
						}
					}
				}
	}

	/**
	 * Iteratively determines the highest non-air block
	 * @param chunk
	 * @param x
	 * @param z
	 * @return Block highest non-air
	 */
	public Block getHighestBlock(Chunk chunk, int x, int z) {
		Block block = null;
		// Return the highest block
		for(int i=127; i>=0; i--)
			if((block = chunk.getBlock(x, i, z)).getTypeId() != 0)
				return block;
		// And as a matter of completeness, return the lowest point
		return block;
	}
	/**
	 * Iteratively determines the highest solid block
	 * @param chunk
	 * @param x
	 * @param z
	 * @return Block highest solid
	 */
	public Block getHighestSolidBlock(Chunk chunk, int x, int z) {
		Block block = null;
		// Return the highest block
		for(int i=127; i>=0; i--)
			if(!(block = chunk.getBlock(x, i, z)).isLiquid() && block.getTypeId() != 0)
				return block;
		// And as a matter of completeness, return the lowest point
		return block;
	}

}
