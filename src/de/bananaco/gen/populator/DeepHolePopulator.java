package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;

public class DeepHolePopulator extends BlockPopulator {

	int chance = 25;
	private static boolean populating = false;

	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		/*int conx = Continent.getContinentFromChunk(chunk.getX());
		int conz = Continent.getContinentFromChunk(chunk.getZ());

		Continent continent = ContinentManager.getInstance(world.getName()).getContinent(world, conx, conz);

		if(continent.getType() != ContinentType.DESERT)
			return;*/

		if(populating)
			return;

		if(rand.nextInt(100) > chance)
			return;

		populating = true;
		createHole(world, getHighestBlock(chunk, rand.nextInt(16), rand.nextInt(16)), rand);
		populating=  false;

	}

	public void createHole(World world, Block block, Random rand) {
		Block center = block;
		Material type = (center.getType()==Material.WATER||center.getType()==Material.STATIONARY_WATER)?Material.WATER:Material.AIR;
		Vector v = new Vector(0, 0, 0);

		int size = 6+rand.nextInt(6);
		Block rel;
		// Create the initial hole
		for(int x=-size; x<=size; x++)
			for(int z=-size; z<=size; z++)
				for(int y=-size; y<=size; y++) {
					Vector vx = new Vector(x, y, z);
					if(vx.distance(v) < size) {
						rel = center.getRelative(x, y, z);
						if(rel.getType() != Material.AIR)
							rel.setType(type);
					}
				}
		int depth = block.getY()-15;
		size = size-3;
		// Then create a burrow to the bowels of the earth
		for(int x=-size; x<=size; x++)
			for(int z=-size; z<=size; z++)
				for(int y=-depth; y<=0 && y+block.getY()>=15; y++) {
					Vector vx = new Vector(x, 0, z);
					if(vx.distance(v) < size) {
						rel = center.getRelative(x, y, z);
						if(rel.getType() != Material.AIR)	
							rel.setType(type);
					}
				}
		// Then we create diamond ore deposits
		for(int i=0; i<8; i++)
			for(int x=-size; x<=size; x++)
				for(int z=-size; z<=size; z++) {
					Vector vx = new Vector(x, 0, z);
					if(vx.distance(v) < size && rand.nextInt(100) < 13) {
						rel = center.getRelative(x, -depth, z);
						// Get the block above the highest block
						rel = getNextAir(rel); 
						rel.setType(Material.DIAMOND_ORE);
					}
				}
		// And fill with lava
		for(int x=-size; x<=size; x++)
			for(int z=-size; z<=size; z++)
				for(int y=-depth; y<=-depth+5 && y+block.getY()>=15; y++) {
					Vector vx = new Vector(x, 0, z);
					if(vx.distance(v) < size) {
						rel = center.getRelative(x, y, z);
						if(rel.getType() == Material.AIR)	
							rel.setType(Material.LAVA);
					}
				}
		// Cobblestone layer
		for(int x=-size; x<=size; x++)
			for(int z=-size; z<=size; z++) {
				int y = -depth+6;
				Vector vx = new Vector(x, 0, z);
				if(vx.distance(v) < size) {
					rel = center.getRelative(x, y, z);	
					rel.setType(Material.COBBLESTONE);
				}
			}
		// Water layer
		for(int x=-size; x<=size; x++)
			for(int z=-size; z<=size; z++) {
				int y = -depth+7;
				Vector vx = new Vector(x, 0, z);
				if(vx.distance(v) < size) {
					rel = center.getRelative(x, y, z);	
					rel.setType(Material.WATER);
				}
			}
		// And finally the sandstone lining pass
		for(int x=-size; x<=size; x++)
			for(int z=-size; z<=size; z++)
				for(int y=-depth; y<=0 && y+block.getY()>=15; y++) {
					Vector vx = new Vector(x, 0, z);
					if(vx.distance(v) < size+2) {
						rel = center.getRelative(x, y, z);
						if(rel.getType() == Material.STONE)	
							rel.setType(Material.DIRT);
					}
				}
	}

	public Block getNextAir(Block from) {
		Block to = null;
		for(int i=1; i+from.getY()<127 && i<6; i++) {
			to = from.getRelative(0, i, 0);
			if(to.getType() == Material.AIR || to.getType() == Material.WATER || to.getType() == Material.STATIONARY_WATER)
				return to;
		}
		return to;
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
