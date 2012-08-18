package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class SugarcanePopulator extends BlockPopulator {

	private int canePatchChance, numSteps;
	private Material cane;
	
	private BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

	public SugarcanePopulator() {
		this.canePatchChance = 100;
		cane = Material.SUGAR_CANE_BLOCK;
		this.numSteps = 5;
	}

	@Override
	public void populate(World world, Random random, Chunk source) {

		// Check if we should plant a flower patch here
		if (random.nextInt(100) < canePatchChance) {
			for(int i=0; i<16; i++) {
				Block b;
				if(random.nextBoolean())
					b = getHighestBlock(source, random.nextInt(16), i);
				else
					b = getHighestBlock(source, i, random.nextInt(16));
				if(b.getType() == Material.GRASS || b.getType() == Material.SAND) {
					createCane(b, random);
				}
			}
		}
	}
	
	public void createCane(Block b, Random rand) {
		// Is water nearby?
		boolean create = false;
		for(BlockFace face : faces) {
			if(b.getRelative(face).getType().name().toLowerCase().contains("water")) {
				create = true;
			}
		}
		// Only create if water is nearby
		if(!create)
			return;
		
		for(int i=1; i<rand.nextInt(numSteps); i++)
			b.getRelative(0, i, 0).setType(cane);
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

}
