package de.bananaco.gen.populator;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;


/**
 * BlockPopulator that turns deserts into sand and places cacti.
 */
public class DesertPopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
		
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int tx = (chunk.getX() << 4) + x;
                int tz = (chunk.getZ() << 4) + z;
                int y = world.getHighestBlockYAt(tx, tz);
                
                Block block = chunk.getBlock(x, y, z).getRelative(BlockFace.DOWN);
                
                // Generate cactus
                if (block.getType() == Material.SAND) {
                    if (random.nextInt(200) == 0) {
                        // Make sure it's surrounded by air
                        Block base = block.getRelative(BlockFace.UP);
                        if (base.getTypeId() == 0 && base.getRelative(BlockFace.NORTH).getTypeId() == 0 && base.getRelative(BlockFace.EAST).getTypeId() == 0 && base.getRelative(BlockFace.SOUTH).getTypeId() == 0 & base.getRelative(BlockFace.WEST).getTypeId() == 0) {
                            generateCactus(base, random.nextInt(4));
                        }
                    }
                }
            }
        }
    }

    private void generateCactus(Block block, int height) {
        for (int i = 0; i < height; ++i) {
            block.getRelative(BlockFace.UP, i).setType(Material.CACTUS);
        }
    }
    
}
