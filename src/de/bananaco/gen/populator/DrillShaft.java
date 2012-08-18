package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;


public class DrillShaft extends BlockPopulator {
	
	int min = 15;
	int max = 100;
	int chance = 5;
	
	private static boolean populating = false;
	
	@Override
    public void populate(World world, Random random, Chunk source) {
		if(populating)
			return;
		
		if(random.nextInt(100) > chance)
			return;
		
		Block block = getHighestBlock(source, random.nextInt(16), random.nextInt(16));
		if(block.getType() != Material.NETHERRACK)
			return;
		
		createShaft(block, random, min+random.nextInt(max-min));
	}
	
	public double getRandom(Random rand) {
		double r = rand.nextDouble()*2-1;
		while(Math.abs(r) < 0.3) {
			r = rand.nextDouble()*2-1;
		}
		return r;
	}
	
	public void createShaft(Block block, Random rand, int iterations) {
		populating = true;
		Vector direction = new Vector(getRandom(rand), -0.1-rand.nextDouble(), getRandom(rand));

		Location loc = block.getLocation();
		// What does this even DO?
		for(int i=0; i<iterations; i++) {
			loc.add(direction);
			if(loc.getY() > 123)
				break;
			createSector(loc.getBlock(), rand, 2, Material.AIR);
		}
		// And create the bottom of the shaft
		loc.add(direction);
		createSector(loc.getBlock(), rand, 3, Material.GLOWSTONE);
		populating = false;
	}
	
	public void createSector(Block block, Random rand, int radius, Material mat) {
		Vector c = new Vector(0, 0, 0);

		for(int x=-radius; x<=radius; x++)
			for(int z=-radius; z<=radius; z++)
				for(int y=-radius; y<=radius; y++) {
					// Calculate 3 dimensional distance
					Vector v = new Vector(x, y, z);
					// If it's within this radius gen the sphere
					if(c.distance(v) < radius && block.getY()+y < 120 && block.getY()+y > 20) {
						Block b = block.getRelative(x, y, z);
						// Check if the block is already MOSSY_COBBLESTONE
						if(b.getType() != Material.LAVA && b.getType() != Material.BEDROCK)
							b.setType(mat);
						else
							return;
					} else if(block.getY() + y > 20) {
						Block b= block.getRelative(x, y, z);
						if(b.getType() == Material.STONE)
							b.setType(Material.NETHERRACK);
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
