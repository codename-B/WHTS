package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class ToppedFence extends BlockPopulator {

	int chance = 45;
	
	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		
		if(rand.nextInt(1000) > chance)
			return;
		
		Block b = getHighestBlock(chunk, rand.nextInt(16), rand.nextInt(16));
		
		if(b.getType() == Material.OBSIDIAN && b.getRelative(BlockFace.UP).getType() == Material.AIR) {
			create(b, rand.nextInt(10));
		}
	}
	
	/**
	 * Creates the "pillar" thing
	 * @param block
	 * @param height
	 */
	public void create(Block block, int height) {
		// The base
		block.setType(Material.NETHER_BRICK);
		// The pillar
		for(int i=1; i<height; i++) {
			block.getRelative(0, i, 0).setType(Material.NETHER_FENCE);
		}
		
		Block rel;
		// The top
		rel = block.getRelative(0, height, 0);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.NETHERRACK);
		
		// Stairs around? No, netherrack!
		rel = block.getRelative(1, height, 0);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.NETHERRACK);
		rel = block.getRelative(-1, height, 0);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.NETHERRACK);
		rel = block.getRelative(0, height, 1);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.NETHERRACK);
		rel = block.getRelative(0, height, -1);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.NETHERRACK);
		
		// And fire ontop of this netherrack
		rel = block.getRelative(1, height+1, 0);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.FIRE);
		rel = block.getRelative(-1, height+1, 0);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.FIRE);
		rel = block.getRelative(0, height+1, 1);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.FIRE);
		rel = block.getRelative(0, height+1, -1);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.FIRE);
		
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
