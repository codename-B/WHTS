package de.bananaco.gen.populator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class Mineshaft extends BlockPopulator {
	
	int chance = 2;
	private static boolean populating = false;

	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		if(populating)
			return;
		
		if(rand.nextInt(100) > chance)
			return;
		
		populating = true;
		createShaft(world, rand, chunk);
		populating = false;
	}
	
	public void createShaft(World world, Random rand, Chunk chunk) {
		Block start = getHighestBlock(chunk, rand.nextInt(16), rand.nextInt(16));
		// Make sure we start this somewhere reasonable sensible
		if(start.getType() == Material.WATER || start.getType() == Material.STATIONARY_WATER)
			return;
		// Make sure we start this somewhere reasonably high up
		if(start.getY() < 68)
			return;
		
		int segments = 2+rand.nextInt(20);
		BlockFace direction = null; 
		for(int i=0; i<segments; i++) {
			direction = getNextFace(direction);
			int length = 10+rand.nextInt(50);
			start = createSegment(start, direction, length, rand);
		}
	}
	
	/**
	 * Create the segment
	 * @param startBlock
	 * @param direction
	 * @param length
	 * @param rand
	 * @return Block lastBlock
	 */
	public Block createSegment(Block startBlock, BlockFace direction, int length, Random rand) {
		Block block = startBlock;
		List<Block> mineshaft = new ArrayList<Block>();
		for(int i=0; i<length; i++) {
			mineshaft.add(block);
			// We don't want to go into water
			if(block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER)
				return block;
			// Continue the track
			block = block.getRelative(direction);
			// Do we move down?
			if(block.getY() > 40 && rand.nextInt(10)>3) {
				block = block.getRelative(BlockFace.DOWN);
			}
		}
		for(int i=0; i<mineshaft.size(); i++) {
			block = mineshaft.get(i);
			block.setType(Material.WOOD);
			block.getRelative(0, 1, 0).setType(Material.RAILS);
			block.getRelative(0, 2, 0).setType(Material.AIR);
			block.getRelative(0, 3, 0).setType(Material.AIR);
			// Is there a roof?
			if(block.getRelative(0, 4, 0).getType() != Material.AIR) {
				block.getRelative(0, 4, 0).setType(Material.WOOD);
			}
		}
		Block rel;
		// Fill the thing with torches! Let there be light in the darkness of the mineshaft!
		for(int i=0; i<mineshaft.size(); i++) {
			block = mineshaft.get(i);
			rel = block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(getNextFace(direction));
			if(i % 5 == 1 && rel.getType() == Material.STONE && rel.getRelative(BlockFace.DOWN).getType() == Material.STONE) {
				// Initial set
				rel.setType(Material.TORCH);
				// See if we can get the damn lighting to update
				rel.setTypeId(Material.TORCH.getId(), true);
				// And another
				rel.setTypeId(Material.TORCH.getId(), true);
				// And another
				rel.setTypeId(Material.TORCH.getId(), true);
			}
		}
		System.out.println("Created mineshaft! "+block.getX()+","+block.getY()+","+block.getZ());
		return block;
	}
	
	public BlockFace getNextFace(BlockFace previous) {
		if(previous == BlockFace.NORTH)
			return BlockFace.WEST;
		return BlockFace.NORTH;
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
