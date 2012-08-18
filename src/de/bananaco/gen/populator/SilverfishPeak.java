package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.CreatureType;
import org.bukkit.generator.BlockPopulator;

public class SilverfishPeak extends BlockPopulator {
	
	private int chance = 6;
	private Material[] mats = {Material.IRON_ORE, Material.STONE, Material.MONSTER_EGGS, Material.MONSTER_EGGS};
	private static boolean populating = false;
	
	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		if(populating)
			return;
		
		if(rand.nextInt(100) > chance)
			return;
		
		Block block = getHighestBlock(chunk, rand.nextInt(16), rand.nextInt(16));
		
		if(block.getType() != Material.STONE)
			return;
		// Probably not necessary but it's best to clarify
		if(block.getRelative(BlockFace.UP).getType() != Material.AIR)
			return;
		// Create the shrubbery
		populating = true;
		createPeak(block, rand);
		populating = false;
	}
	
	public void createPeak(Block block, Random rand) {
		int height = 2+rand.nextInt(5);
		Block base = block.getRelative(BlockFace.UP);
		Block rel;
		// Create the pyramid
		for(int i=0; i<height; i++) {
			int m = height-i;
			// Create the layer!
			for(int j=0; j<m; j++) {
				// left
				rel = base.getRelative(j, i, 0);
				if(rel.getRelative(BlockFace.DOWN).getType() != Material.AIR)
					rel.setType(mats[rand.nextInt(mats.length)]);
				// right
				rel = base.getRelative(-j, i, 0);
				if(rel.getRelative(BlockFace.DOWN).getType() != Material.AIR)
					rel.setType(mats[rand.nextInt(mats.length)]);
				// up
				rel = base.getRelative(0, i, j);
				if(rel.getRelative(BlockFace.DOWN).getType() != Material.AIR)
					rel.setType(mats[rand.nextInt(mats.length)]);
				// down
				rel = base.getRelative(0, i, -j);
				if(rel.getRelative(BlockFace.DOWN).getType() != Material.AIR)
					rel.setType(mats[rand.nextInt(mats.length)]);
			}
		}
		// Create the spawner
		block.setType(Material.MOB_SPAWNER);
		if(block.getState() instanceof CreatureSpawner) {
			CreatureSpawner spawner = (CreatureSpawner) block.getState();
			spawner.setCreatureType(CreatureType.SILVERFISH);
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
