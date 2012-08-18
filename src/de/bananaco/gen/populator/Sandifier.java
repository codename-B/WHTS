package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class Sandifier extends BlockPopulator {
	
	public BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		for(int x=0; x<16; x++) {
			for(int z=0; z<16; z++) {
				Block b = getHighestBlock(chunk, x, z);
				if(b.getType() == Material.WATER || b.getType() == Material.STATIONARY_WATER) {
					for(BlockFace face : faces) {
						Block f = b.getRelative(face);
						if(f.getType() == Material.GRASS || f.getType() == Material.DIRT) {
							f.setType(Material.SAND);
						}
						Block f2 = f.getRelative(face);
						if(f2.getType() == Material.GRASS || f.getType() == Material.DIRT) {
							f2.setType(Material.SAND);
						}
					}
				}
			}
		}
	}
	
	public Block getHighestBlock(Chunk chunk, int x, int z) {
		for(int i=chunk.getWorld().getMaxHeight()-1; i>=0; i--) {
			Block b = chunk.getBlock(x, i, z);
			if(b.getType() != Material.AIR) {
				return b;
			}
		}
		return chunk.getBlock(x, 0, z);
	}

}
