package de.bananaco.gen.populator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class ObsidianPillar extends BlockPopulator {
	
	public static List<BlockPopulator> getSingleList() {
		List<BlockPopulator> pops = new ArrayList<BlockPopulator>();
		pops.add(new ObsidianPillar());
		return pops;
	}

	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		
		// a 10% chance that this chunk has an obsidian pillar
		if(rand.nextInt(100) > 15)
			return;
		int center = 8;
		int width = 5;
		int wsqr = width*width;
		int w1sqr = (width-1)*(width-1);
		int height = 10+rand.nextInt(10);
		boolean cont = false;
		// Check that there's at least one non-water block
		for(int x=0; x<16; x++)
			for(int z=0; z<16; z++) {
				Block highest = getHighestBlock(chunk, x, z);
				if(!highest.getType().name().toLowerCase().contains("water"))
					cont = true;
			}
		// Do we continue?
		if(!cont)
			return;
		int h=-1;
		// Then build the pillar
		for(int x=0; x<16; x++)
			for(int z=0; z<16; z++) {
				// use distance squared for speed
				int dsqr = Math.abs(center-x)*Math.abs(center-x)+Math.abs(center-z)*Math.abs(center-z);
				if(dsqr <= wsqr) {
					Block start = getHighestSolidBlock(chunk, x, z);
					int starty = start.getY();
					if(h == -1)
						h = starty+height;	
					for(int y=starty; y<h && y<128; y++) {
						// Obsidian pillars are now filled with logs!
						// This makes them a fantastic source of basic resources (once you actually get inside them)
						if(y == starty || y == h-1 || dsqr >= w1sqr)
							chunk.getBlock(x, y, z).setType(Material.OBSIDIAN);
						else
							chunk.getBlock(x, y, z).setType(Material.LOG);
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
