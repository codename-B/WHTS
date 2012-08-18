package de.bananaco.gen.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class OreVeinPopulator extends BlockPopulator {

	int chance = 70;

	private Material common = Material.COAL_ORE;
	private Material sparse = Material.IRON_ORE;
	private Material rare = Material.GOLD_ORE;
	private Material extra = Material.DIAMOND_ORE;

	//private int c = 25;
	//private int s = 15;
        private int c = 15;
        private int s = 10;
	private int r = 5;
	private int e = 2;

	private int[] cl = {23, 24, 25, 32, 35, 36, 40, 41, 42, 43, 49, 50, 51, 60, 64};
	private int[] sl = {14, 26, 29, 30, 31, 33, 35, 40, 50, 60, 61};
	private int[] rl = {12, 14, 16, 20, 25, 40, 60};
	private int[] el = {8, 12, 14};

	private static boolean populating = false;

	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		if(populating)
			return;
		if(rand.nextInt(100) > chance)
			return;
		populating = true;
		// common
		for(int i=0; i<c; i++) {
			Block block = chunk.getBlock(rand.nextInt(16), cl[rand.nextInt(cl.length)], rand.nextInt(16));
			if(block.getType() == Material.STONE)
				block.setType(common);
		}
		// spare
		for(int i=0; i<s; i++) {
			Block block = chunk.getBlock(rand.nextInt(16), sl[rand.nextInt(sl.length)], rand.nextInt(16));
			if(block.getType() == Material.STONE)
				block.setType(sparse);
		}
		// rare
		for(int i=0; i<r; i++) {
			Block block = chunk.getBlock(rand.nextInt(16), rl[rand.nextInt(rl.length)], rand.nextInt(16));
			if(block.getType() == Material.STONE)
				block.setType(rare);
		}
		// extra-rare
		for(int i=0; i<e; i++) {
			Block block = chunk.getBlock(rand.nextInt(16), el[rand.nextInt(el.length)], rand.nextInt(16));
			if(block.getType() == Material.STONE)
				block.setType(extra);
		}
		populating = false;
	}

}
