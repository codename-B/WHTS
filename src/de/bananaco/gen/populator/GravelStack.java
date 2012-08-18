package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class GravelStack extends BlockPopulator {
	
	int min = 5;
	int max = 20;
	int minh = 3;
	int maxh = 7;
	int chance = 7;
	
	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		
		if(rand.nextInt(1000) > chance)
			return;
		
		// Total # of stacks
		int num = min+rand.nextInt(max-min);
		
		for(int i=0; i<num; i++) {
			// Choose a different x, z every time (sometimes the same but it'll be fine)
			Block b = getHighestBlock(chunk, rand.nextInt(16), rand.nextInt(16));
			if(b.getType() == Material.OBSIDIAN && b.getRelative(BlockFace.UP).getType() == Material.AIR) {
				stack(b, rand);
			}
		}
	}
	
	public void stack(Block block, Random rand) {
		int h = minh+rand.nextInt(maxh-minh);
		Block rel;
		for(int y=0; y<h; y++) {
			rel = block.getRelative(0, y, 0);
			if(rel.getType() == Material.AIR)
				rel.setType(Material.GRAVEL);
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
