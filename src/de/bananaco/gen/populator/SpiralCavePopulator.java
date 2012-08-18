package de.bananaco.gen.populator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


public class SpiralCavePopulator extends BlockPopulator {
	
	private int chance = 10;
	
	private static boolean populating = false;
	
	private Material[] remnants = {Material.COAL, Material.IRON_ORE, Material.GOLD_ORE, Material.IRON_INGOT, Material.GOLD_INGOT};
	
	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		if(populating)
			return;

		int x = rand.nextInt(16);
		int z = rand.nextInt(16);
		Block highest = getHighestBlock(chunk, x, z);
		
		if(highest.getType() == Material.WATER || highest.getType() == Material.STATIONARY_WATER)
			return;
		
		// Randomness factor
		if(rand.nextInt(1000) > chance)
			return;
		// Fixed height
		int height = 25;
		// Random block
		Block block = chunk.getBlock(x, height, z);
		
		populating = true;
		createSpiral(block, rand, true);
		populating = false;
	}
	
	public void createSpiral(Block block, Random rand, boolean recurse) {
		Block rel = block;
		List<Integer[]> pairs = getXYList();
		List<Block> remnants = new ArrayList<Block>();
		System.out.println("Spiral created: center - "+block.getX()+","+block.getZ());
		for(int i=0; i<pairs.size()-1; i++) {
			Integer[] pair = pairs.get(i);
			
			if(Math.abs(pair[0]) > 128 || Math.abs(pair[1]) > 128)
				return;
			
			rel = block.getRelative(pair[0], 0, pair[1]);
			
			if(rel.getY() > 90 || rel.getType() == Material.WATER || rel.getType() == Material.STATIONARY_WATER || rel.getType() == Material.BEDROCK)
				return;
			
			else if(rand.nextInt(5000) < chance && recurse)
				createSpiral(rel, rand, false);
			
			else {
				createSphere(rel, 3);
				if(rand.nextInt(5000) < 3)
					remnants.add(rel);
			}
		}
		System.out.println("Remnants: "+remnants.size());
		for(int i=0; i<remnants.size(); i++) {
			getFloor(remnants.get(i), rand);
		}
	}
	
	public void createSphere(Block block, int radius) {
		Block rel = block;
		Vector v = new Vector(0, 0, 0);
		for(int x=-radius; x<=radius; x++)
			for(int z=-radius; z<=radius; z++)
				for(int y=-radius; y<=radius; y++) {
					Vector d = new Vector(x, y, z);
					rel = block.getRelative(x, y, z);
					// Create the hollow
					if(v.distance(d) < radius) {
						if(rel.getType() != Material.AIR && rel.getType() != Material.BEDROCK && rel.getType() != Material.WATER && rel.getType() != Material.STATIONARY_WATER)
							rel.setType(Material.AIR);
					} else {
						// Cracked brick lined (and sandstone lined on desert at surface
						if(rel.getType() == Material.STONE) {
							rel.setType(Material.SMOOTH_BRICK);
							rel.setData((byte) 2);
						}
						else if(rel.getType() == Material.SAND)
							rel.setType(Material.SANDSTONE);
						}
					}
	}
	
	public void getFloor(Block block, Random rand) {
		if(block.getType() == Material.WATER)
			return;
		if(block.getType() == Material.STATIONARY_WATER)
			return;
		while(block.getType() == Material.AIR) {
			block = block.getRelative(BlockFace.DOWN);
		}
		createRemnants(block, rand);
	}
	
	public void createRemnants(Block floor, Random rand) {
		Block rel;
		for(int x=-3; x<3; x++)
			for(int z=-16; z<16; z++) {
				rel = floor.getRelative(x, 0, z);
				if(rel.getType() == Material.STONE || rel.getType() == Material.SMOOTH_BRICK)
					rel.setType(Material.DIRT);
		}
		rel = floor.getRelative(BlockFace.UP);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.WORKBENCH);
		rel = floor.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
		if(rel.getType() == Material.AIR)
			rel.setType(Material.FURNACE);
		// Set furnace contents
		if(rel.getState() instanceof Furnace) {
			Furnace f = (Furnace) rel.getState();
			f.getInventory().addItem(new ItemStack(remnants[rand.nextInt(remnants.length)], 1+rand.nextInt(63)));
			f.update();
		}
		// And a lone torch
		rel = floor.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
		if(rel.getType() == Material.AIR) {
			rel.setType(Material.TORCH);
			rel.setTypeId(Material.TORCH.getId());
			rel.setTypeId(Material.TORCH.getId(), true);
		}
		// And now a buried chest!
		rel = floor.getRelative(BlockFace.DOWN);
		if(rel.getType() == Material.STONE || rel.getType() == Material.SMOOTH_BRICK && rand.nextBoolean()) {
			rel.setType(Material.CHEST);
			if(rel.getState() instanceof Chest) {
				Chest chest = (Chest) rel.getState();
				int stacks = 1+rand.nextInt(23);
				for(int i=0; i<stacks; i++) {
					chest.getInventory().addItem(new ItemStack(remnants[rand.nextInt(remnants.length)], 1+rand.nextInt(14)));
				}
				chest.update();
			}
		}
	}
	
	public List<Integer[]> getXYList() {
		List<Integer[]> pairs = new ArrayList<Integer[]>();
		for(int i=0; i<360*8; i++) {
			double d = i/2.0;
			Integer[] xy = getXY(d);
			pairs.add(xy);
		}
		return pairs;
	}
	
	public Integer[] getXY(double i) {
		double r = Math.toRadians(i);
		double o = Math.sin(r)*i;
		double a = Math.cos(r)*i;
		
		Integer[] xy = new Integer[2];
		xy[0] = (int) (o/16);
		xy[1] = (int) (a/16);
		
		return xy;
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
