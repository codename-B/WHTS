package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

/**
 * This is a sealed "Bedrock Box" populator with a glowstone floor for
 * lighting purposes
 *
 * Need to implement a /hunt and a /return toggle for this.
 */
public class HiddenBox extends BlockPopulator {
	
	int chance = 7;
	
	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		// Should we create a box?
		if(rand.nextInt(1000) > chance)
			return;
		// Create the box around the center of the chunk
		Block center = chunk.getBlock(8, 8, 8);
		createHiddenBox(world, center.getX(), center.getZ());
	}
	
	/**
	 * Creates a hidden box at that x and z
	 * @param world
	 * @param ax
	 * @param az
	 */
	public void createHiddenBox(World world, int ax, int az) {
		// Create a walled in bedrock box :D x && z == 4 makes the walls
		for(int x=-4; x<=4; x++)
			for(int z=-4; z<=4; z++)
				for(int y=0; y<=5; y++) {
					if(Math.abs(x) <= 3 && Math.abs(z) <=3) {
						if(y==0 || y==5) {
							world.getBlockAt(ax+x, y, az+z).setType(Material.BEDROCK);
						} else if(y==1) {
							world.getBlockAt(ax+x, y, az+z).setType(Material.GLOWSTONE);
						} else {
							world.getBlockAt(ax+x, y, az+z).setType(Material.AIR);
						}
					} else {
						world.getBlockAt(ax+x, y, az+z).setType(Material.BEDROCK);
					}
				}
	}
	
	/**
	 * Returns true if there is a hidden box at that x, z
	 * @param world
	 * @param x
	 * @param z
	 * @return boolean hidden box present
	 */
	public boolean isHiddenBox(World world, int x, int z) {
		// Check the layers to see if they correspond to a hidden box
		if(world.getBlockTypeIdAt(x, 0, z) == Material.BEDROCK.getId() &&
				world.getBlockTypeIdAt(x, 1, z) == Material.GLOWSTONE.getId() &&
				world.getBlockTypeIdAt(x, 2, z) == Material.AIR.getId() &&
				world.getBlockTypeIdAt(x, 3, z) == Material.AIR.getId() &&
				world.getBlockTypeIdAt(x, 4, z) == Material.AIR.getId() &&
				world.getBlockTypeIdAt(x, 5, z) == Material.BEDROCK.getId())
			return true;
		return false;
	}
	
}
