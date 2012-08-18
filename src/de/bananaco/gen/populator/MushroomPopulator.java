/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Tropic.
 * 
 * Tropic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Tropic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Tropic If not, see <http://www.gnu.org/licenses/>.
 */
package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class MushroomPopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk source) {
		int chance = random.nextInt(100);
		if (chance < 10) {
			int type = random.nextInt(100);
			Material mushroom;
			if (type < 33) {
				mushroom = Material.getMaterial(100);
			}
			else {
				mushroom = Material.getMaterial(100);
			}
			int mushroomcount = random.nextInt(3)+2;
			int placed = 0;
			for (int t = 0; t <= mushroomcount; t++) {
				for (int flower_x = 0; flower_x < 16; flower_x++) {
					for (int flower_z = 0; flower_z < 16; flower_z++) {
						Block handle = world.getBlockAt(flower_x+source.getX()*16, getHighestEmptyBlockYAtIgnoreTreesAndFoliage(world, flower_x+source.getX()*16, flower_z+source.getZ()*16), flower_z+source.getZ()*16);
						if (handle.getRelative(BlockFace.DOWN).getType().equals(Material.GRASS) && isRelativeTo(handle, Material.LOG) && handle.isEmpty()) {
							handle.setType(mushroom);
							placed++;
							if (placed >= mushroomcount) {
								return;	
							}
						}
					}
				}
			}
		}
	}
	
	private boolean isRelativeTo(Block block, Material material) {
	    for (BlockFace blockFace : BlockFace.values()) {
	        if (block.getRelative(blockFace).getType().equals(material)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private int getHighestEmptyBlockYAtIgnoreTreesAndFoliage(World w, int x, int z){
		for (int y = w.getMaxHeight(); y >= 1; y--) {
			Block handle = w.getBlockAt(x, y-1, z);
			int id = handle.getTypeId();
			if (id != 0 &&  id != 17 && id != 18 && id != 37 && id != 38) {
				return y;
			}
		}
		return 0;
	}
}
