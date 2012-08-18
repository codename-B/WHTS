package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.CreatureType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class NetherTunnel extends BlockPopulator {
	
	int maxLength = 256;
	int height = 5;
	int seaLevel = 64;
	int percentChance = 15;
	
	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		// Percentage check
		if(rand.nextInt(1000) > percentChance)
			return;
		// Do ONE BLOCK per chunk
		Block start = getHighestBlock(chunk, rand.nextInt(16), rand.nextInt(16));
		// If sea level or below return
		if(start.getY() <= seaLevel+height)
			return;
		
		start = start.getRelative(0, -height, 0);
		// Reference the spawner;
		Block spawner = start;
		// Loop through up to max length and create the tunnel
		for(int i=0; i<maxLength && start.getType() != Material.AIR && start.getType() != Material.WATER && start.getType() != Material.STATIONARY_WATER; i++) {			
			// Create the chamber (floor and lava)
			if(i<10)
				createRoom(start);
			else
				createChamber(start, Material.LAVA);
			// Do lighting of the chamber
			if(i % 3 == 1)
				light(start);
			// Next
			start = start.getRelative(1, 0, 0);
		}
		
		// 50% chance of forking
		if(rand.nextBoolean()) {
			// Reference back
			start = spawner.getRelative(3, 0, 3);
			// Loop through up to max length and create the tunnel
			for(int i=3; i<maxLength && start.getType() != Material.AIR && start.getType() != Material.WATER && start.getType() != Material.STATIONARY_WATER; i++) {			
				createChamber2(start, Material.LAVA);
				// Do lighting of the chamber
				if(i % 3 == 1)
					light2(start);
				// Next
				start = start.getRelative(0, 0, 1);
			}
		}
		if(spawner != null) {
			// Get where we want it
			spawner = spawner.getRelative(3, 1, 0);
			// Set it to a spawner
			spawner.setType(Material.MOB_SPAWNER);
			if(spawner.getState() instanceof CreatureSpawner) {
				CreatureSpawner cs = (CreatureSpawner) spawner.getState();
				cs.setCreatureType(CreatureType.BLAZE);
				cs.update();
				// Chest underneath
				Block chest = spawner.getRelative(0, -1, 0);
				chest.setType(Material.CHEST);
				// Fill the chest with ridiculous goodies
				Chest chs = (Chest) chest.getState();
				Inventory chi = chs.getInventory();
				chi.addItem(new ItemStack(Material.GOLDEN_APPLE, rand.nextInt(64)));
				for(int i=0; i<rand.nextInt(10); i++)
					chi.addItem(new ItemStack(Material.GOLD_NUGGET, rand.nextInt(64)));
				chi.addItem(new ItemStack(Material.GOLD_AXE));
				chi.addItem(new ItemStack(Material.GOLD_PICKAXE));
				chi.addItem(new ItemStack(Material.GOLD_SWORD));
				chi.addItem(new ItemStack(Material.GOLD_HELMET));
				chi.addItem(new ItemStack(Material.GOLD_CHESTPLATE));
				chi.addItem(new ItemStack(Material.GOLD_BOOTS));
				chs.update();
				System.out.println("spawner placed: "+spawner.getX()+","+spawner.getY()+","+spawner.getZ());
			} else {
				System.err.println("spawner error: "+spawner.getX()+","+spawner.getY()+","+spawner.getZ());	
			}
		}
	}
	
	public void createChamber(Block point, Material floor) {
		for(int z=-2; z<=2; z++)
			for(int y=0; y<2; y++) {
				// Nether brick floor
				if(y==0)
					point.getRelative(0, y, z).setType(floor);
				// Else hollow out
				else if(Math.abs(z) < 2)
					point.getRelative(0, y, z).setType(Material.AIR);
			}
	}
	
	public void createRoom(Block point) {
		for(int z=-2; z<=2; z++)
			for(int y=0; y<=height; y++) {
				if(y==height)
					point.getRelative(0, y, z).setType(Material.NETHERRACK);
				// Nether brick floor
				else if(y==0)
					point.getRelative(0, y, z).setType(Material.NETHER_BRICK);
				// Else hollow out
				else
					point.getRelative(0, y, z).setType(Material.AIR);
			}
		// Then the chamber walls to be sure
		for(int y=0; y<height; y++) {
			point.getRelative(0, y, 3).setType(Material.NETHERRACK);
			point.getRelative(0, y, -3).setType(Material.NETHERRACK);
		}
	}
	
	public void light(Block point) {
		Block left = point.getRelative(0, 1, 2);
		if(left.getType() != Material.AIR)
			left.setType(Material.GLOWSTONE);
		Block right = point.getRelative(0, 1, -2);
		if(right.getType() != Material.AIR)
			right.setType(Material.GLOWSTONE);
	}
	
	public void createChamber2(Block point, Material floor) {
		for(int x=-2; x<=2; x++)
			for(int y=0; y<2; y++) {
				// Nether brick floor
				if(y==0)
					point.getRelative(x, y, 0).setType(floor);
				// Else hollow out
				else if(Math.abs(x) < 2)
					point.getRelative(x, y, 0).setType(Material.AIR);
			}
	}
	
	public void light2(Block point) {
		Block left = point.getRelative(2, 1, 0);
		if(left.getType() != Material.AIR)
			left.setType(Material.GLOWSTONE);
		Block right = point.getRelative(-2, 1, 0);
		if(right.getType() != Material.AIR)
			right.setType(Material.GLOWSTONE);
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
