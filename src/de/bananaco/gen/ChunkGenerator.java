package de.bananaco.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import de.bananaco.gen.populator.*;



public class ChunkGenerator extends org.bukkit.generator.ChunkGenerator {
	double scale = 24.0; //how far apart the tops of the hills are
	double threshold = 0.0; // the cutoff point for terrain
	int middle = 64; // the "middle" of the road
	
	List<BlockPopulator> pops = new ArrayList<BlockPopulator>();
	
	public ChunkGenerator() {
		pops.add(new CavePopulator());
		pops.add(new OrePopulator());
		pops.add(new DungeonPopulator());
		pops.add(new LapisRing());
		pops.add(new LakePopulator());
		pops.add(new LakeAndCreek());
		pops.add(new Sandifier());
		pops.add(new SugarcanePopulator());
		pops.add(new WildGrassPopulator());
		pops.add(new MelonPopulator());
		pops.add(new PumpkinPopulator());
		pops.add(new FlowerPopulator());
		pops.add(new TreePopulator());
		pops.add(new WaterLily());
	}

	public List<BlockPopulator> getDefaultPopulators(World world) {
		return pops;
	}

	/*
	 * Gets a block in the chunk. If the Block section doesn't exist, it allocates it.
	 * [y>>4] the section id (y/16)
	 * the math for the second offset confuses me
	 */
	byte getBlock(int x, int y, int z, byte[][] chunk) {
		if (chunk[y>>4] == null)
			chunk[y>>4] = new byte[16*16*16];
		if (!(y<=256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0))
			return 0; //Out of bounds
		try {
			return chunk[y>>4][((y & 0xF) << 8) | (z << 4) | x];
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/*
	 * Sets a block in the chunk. If the Block section doesn't exist, it allocates it.
	 * [y>>4] the section id (y/16)
	 * the math for the second offset confuses me
	 */
	void setBlock(int x, int y, int z, byte[][] chunk, Material material) {
		if (chunk[y>>4] == null)
			chunk[y>>4] = new byte[16*16*16];
		if (!(y<=256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0))
			return; //Out of bounds
		try {
			chunk[y>>4][((y & 0xF) << 8) | (z << 4) | x] = (byte)material.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	//Generates block sections. Each block section is 16*16*16 blocks, stacked above each other. //There are world height / 16 sections. section number is world height / 16 (>>4)
	//returns a byte[world height / 16][], formatted [section id][Blocks]. If there are no blocks in a section, it need not be allocated.
	public byte[][] generateBlockSections(World world, Random rand,
			int ChunkX, int ChunkZ, BiomeGrid biomes) {
		Random random = new Random(world.getSeed());
		SimplexOctaveGenerator gen = new SimplexOctaveGenerator(random, 8);
		byte[][] chunk = new byte[world.getMaxHeight() / 16][];
		
		gen.setScale(1/scale); //The distance between peaks of the terrain. Scroll down more to see what happens when you play with this
		double threshold = this.threshold; //scroll down to see what happens when you play with this.

		int smidle = middle-32;
		int mmidle = middle+32;
		
		for (int x=0;x<16;x++) {
			for (int z=0;z<16;z++) {
				int real_x = x + ChunkX * 16;
				int real_z = z + ChunkZ * 16;
				
				for (int y=smidle; y<mmidle; y++) {
					double noise = gen.noise(real_x, y, real_z, 0.5, 0.5);
					double variable = (y-middle)/17.0; // this will generate the offset
					variable = Math.abs(variable); // skylands?
					noise = noise - variable; // scale the terrain	
					if(noise > threshold) //explained above
						setBlock(x, y, z, chunk, Material.STONE);
				}
				
				for (int y=smidle; y<mmidle; y++) {
					int id = getBlock(x, y, z, chunk);
					int idu = getBlock(x, y+1, z, chunk);
					if(id != 0 && idu == 0) {
						setBlock(x, y, z, chunk, Material.GRASS);
						if(getBlock(x, y-1, z, chunk) != 0)
							setBlock(x, y-1, z, chunk, Material.DIRT);
						if(getBlock(x, y-2, z, chunk) != 0)
							setBlock(x, y-2, z, chunk, Material.DIRT);
					}
				}
			}
		}
		return chunk;
	}
}