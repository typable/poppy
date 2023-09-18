package poppy;

import java.util.Random;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.TreeType;
import org.bukkit.block.Biome;


public class CustomTreePopulator extends BlockPopulator
{
    @Override
    public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion)
    {
        final int amount = random.nextInt((6 - 0) + 1) + 0;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.GRASS_BLOCK && y > 0)
            {
                y--;
            }

            final Location location = new Location(Bukkit.getWorld(worldInfo.getUID()), x, y, z);
            final TreeType treeType = this.getTreeType(limitedRegion.getBiome(location));

            if (treeType != null)
            {
                limitedRegion.generateTree(location, random, treeType);
            }
        }
    }

    protected TreeType getTreeType(final Biome biome)
    {
        switch (biome)
        {
            case PLAINS:
                return TreeType.TREE;
            case BIRCH_FOREST:
                return TreeType.BIRCH;
            case FOREST:
                return TreeType.REDWOOD;
            case DESERT:
                return null;
            case JUNGLE:
                return TreeType.JUNGLE;
            case SNOWY_PLAINS:
                return TreeType.REDWOOD;
            case FLOWER_FOREST:
                return TreeType.TREE;
            default:
                return null;
        }
    }
}
