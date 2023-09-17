package poppy;

import java.util.Random;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.TreeType;


public class CustomTreePopulator extends BlockPopulator
{
    @Override
    public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion)
    {
        final int amount = random.nextInt((5 - 0) + 1) + 0;

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

            limitedRegion.generateTree(location, random, TreeType.TREE);
        }
    }
}
