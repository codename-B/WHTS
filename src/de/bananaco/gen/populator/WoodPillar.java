package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class WoodPillar extends BlockPopulator {
	
	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		// 30% chance of ever even happening (make it rare)
		if(rand.nextInt(100) > 70)
			return;
		// Get the first block in the operation
		Block first = getHighestSolidBlock(chunk, 0, 0);
		// No liquids pl0x
		if(first.isLiquid())
			return;
		int f = first.getY();
		int nf = 0;
		// iterate through the heightmap and see how many blocks are on the same level
		Block test = null;
		for(int x=0; x<16; x++)
			for(int z=0; z<16; z++) {
				if((test = getHighestSolidBlock(chunk, x, z)).getY() != f) {
					nf++;
				}
				// But no water!
				if(test.isLiquid() || test.getType().name().toLowerCase().contains("water") || test.getY() < 66) {
					return;
				}
			}
		// If there's more than this not level, cancel the operation
		if(nf > 2)
			return;
		
		int center = 8;
		int width = 5;
		
		int wsqr = width*width;
		int wmsqr = (width-1)*(width-1);
		
		int levels = 1+rand.nextInt(2);
		int height = 5;
		
		int f0 = f;
		// Then build the pillar
		for(int x=0; x<16; x++)
			for(int z=0; z<16; z++) {
				// use distance squared for speed
				for(int i=0; i<levels; i++) {
				f = f0+height*i;
				int dsqr = Math.abs(center-x)*Math.abs(center-x)+Math.abs(center-z)*Math.abs(center-z);
				if(dsqr <= wsqr && dsqr >= wmsqr) {
					for(int y=f; y<height+f && y<128; y++) {
						if(y == height+f-1 && i == levels-1) {
						chunk.getBlock(x, y, z).setType(Material.FENCE);	
						} else {
						chunk.getBlock(x, y, z).setType(Material.WOOD);
						}
					}
				} else if(dsqr <= wsqr) {
					// Floor/Hole
					if(i>0 && x==8 && z==11)
						chunk.getBlock(x, f, z).setType(Material.AIR);
					else
						chunk.getBlock(x, f, z).setType(Material.WOOD);
					// Ceil/Hole
					if(x==8 && z==11)
						chunk.getBlock(x, f+height-1, z).setType(Material.AIR);
					else
						chunk.getBlock(x, f+height-1, z).setType(Material.WOOD);
					
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
