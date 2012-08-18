package de.bananaco.gen.populator;

import java.util.Random;
import org.bukkit.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;
/**
 * Borrowed from the "Christmas" plugin
 * 
 * Call it a present ;)
 */
public class IceTreePopulator extends BlockPopulator
{

    @SuppressWarnings("unused")
	private static final int TREE_CHANCE = 45;
    @SuppressWarnings("unused")
	private static final int MAX_WIDTH = 6;

    public void populate(World world, Random rand, Chunk chunk)
    {
		
        if(rand.nextInt(100) <= 75)
        {
            int x = chunk.getX() * 16;
            int z = chunk.getZ() * 16;
            int y;
            for(y = 128; y > 0; y--)
            {
            	int id = chunk.getBlock(chunk.getX(), y - 1, chunk.getZ()).getTypeId();
                if(id != 0 && id != Material.SNOW.getId())
                {
                    break;
                }
            }

            for(int i = 0; i < 10; i++)
            {
                int j = (x + rand.nextInt(16)) - rand.nextInt(16);
                int m = (z + rand.nextInt(16)) - rand.nextInt(16);
                int k = (y + rand.nextInt(4)) - rand.nextInt(4);
                generateTree(world, rand, j, k, m);
            }

        }
    }

    public static boolean generateTree(World world, Random rand, int i, int j, int k)
    {
        double width = rand.nextInt(6) + 3;
        double height = width * 2D;
        double height2 = height / 5D;
        if(height2 < 1.0D)
        {
            height2 = 1.0D;
        }
        height2--;
        int id = world.getBlockAt(i, j - 1, k).getTypeId();
        
        if(id == Material.SNOW_BLOCK.getId() || id == Material.SNOW.getId() || id == Material.ICE.getId()) {
        	
        } else {
        	return false;
        }
        for(int l = (int)((double)i - width); (double)l < (double)i + width; l++)
        {
            for(int m = (int)((double)j + height2); (double)m < (double)j + height2 + height; m++)
            {
                for(int n = (int)((double)k - width); (double)n < (double)k + width; n++)
                {
                    int o = world.getBlockTypeIdAt(l, m, n);
                    if(o != Material.AIR.getId() && o != Material.LEAVES.getId() && o != Material.SNOW.getId())
                    {
                        return false;
                    }
                }

            }

        }

        double h = 0.0D;
        int cycles = (int)width;
        for(int c = 0; c < cycles; c++)
        {
            h += 1.5D;
            for(int w = c; (double)w < (width * 2D - (double)c) + 1.0D; w++)
            {
                for(int l = c; (double)l < (width * 2D - (double)c) + 1.0D; l++)
                {
                    int x = (int)((double)(i + w) - width);
                    int y = (int)((double)j + height2 + h);
                    int z = (int)((double)(k + l) - width);
                    Vector vec1 = new Vector(i, y, k);
                    Vector vec2 = new Vector(x, y, z);
                    if(vec1.distance(vec2) <= width - (double)c)
                    {
                        world.getBlockAt(x, y, z).setTypeIdAndData(Material.LEAVES.getId(), (byte)1, false);
                        world.getBlockAt(x, y + 1, z).setTypeIdAndData(Material.LEAVES.getId(), (byte)1, false);
                        world.getBlockAt(x, y + 1, z).setTypeIdAndData(Material.SNOW.getId(), (byte)1, false);
                    }
                    if(c == 0 && vec1.distance(vec2) <= width - (double)c - 2D)
                    {
                        world.getBlockAt(x, y, z).setType(Material.LOG);
                    }
                }

            }

        }

        for(int m = j-1; (double)m < (double)j + height; m++)
        {
            world.getBlockAt(i, m, k).setType(Material.LOG);
        }

        world.getBlockAt(i, (int)((double)j + height), k).setType(Material.SNOW);
        return true;
    }
}
