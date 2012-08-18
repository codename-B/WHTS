// Package Declaration
package de.bananaco.gen.populator;

// Java Imports
import java.util.Random;

// Bukkit Imports
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;


/**
 * A glowstone populator.
 *
 * @author Markus 'Notch' Persson
 * @author iffa
 * @author Nightgunner5
 */
public class GlowstonePopulator extends BlockPopulator {
    // Variables
    private static final BlockFace[] faces = {BlockFace.DOWN, BlockFace.EAST,
        BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.WEST};

    /**
     * Populates a chunk with glowstone. Easily configurable (but results in
     * more rare glowstone) by modifying the suitable()-method. You could also
     * Remove the suitable()-check to have glowstone anywhere.
     * 
     * @param world World
     * @param random Random
     * @param source Source chunk
     */
    @Override
    public void populate(World world, Random random, Chunk source) {
		
        for (int i = 0; i < 2; i++) {
            int x = random.nextInt(16);
            // Changed to reflect our "ground-based" nether
            int y = 64+random.nextInt(32);
            int z = random.nextInt(16);
            while (!suitable(y)) {
                y = random.nextInt(128);
            }
            Block block = source.getBlock(x, y, z);
            // Only populates if the "target" location is air & there is netherrack
            // above it. (and only checked in Y 114-127 or 52-72 for efficiency,
            // might be changed later)
            if (block.getTypeId() != Material.AIR.getId()) {
                return;
            }
            if (block.getRelative(BlockFace.DOWN).getTypeId() != Material.NETHERRACK.getId()) {
                return;
            }
            block.setTypeId(Material.GLOWSTONE.getId());

            for (int j = 0; j < 1500; j++) {
                Block current = block.getRelative(random.nextInt(8) - random.nextInt(8),
                        random.nextInt(12),
                        random.nextInt(8) - random.nextInt(8));
                if (current.getTypeId() != Material.AIR.getId()) {
                    continue;
                }
                int count = 0;
                for (BlockFace face : faces) {
                    if (current.getRelative(face).getTypeId() == Material.GLOWSTONE.getId()) {
                        count++;
                    }
                }

                if (count == 1) {
                    current.setTypeId(Material.GLOWSTONE.getId());
                }
            }
        }
    }

    /**
     * Checks if the given Y-coordinate is "suitable" for glowstone generation. 
     * <br />
     * <br />
     * If the Y-coordinate is >51 it's ok
     *
     * @param y Y-coordinate
     * 
     * @return true if the given Y-coordinate is suitable for glowstone generation
     */
    private static boolean suitable(int y) {
        if (y > 51) {
        	return true;
        }
        return false;
    }
}