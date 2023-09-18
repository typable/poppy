package poppy;

import java.util.Random;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.Material;
import org.bukkit.block.Biome;


public class CustomGrassPopulator extends BlockPopulator
{
    @Override
    public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion)
    {
        int amount = random.nextInt((30 - 5) + 1) + 5;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.GRASS_BLOCK && y > 0)
            {
                y--;
            }

            if (y > 0 && limitedRegion.getBiome(x, y, z) != Biome.DESERT)
            {
                limitedRegion.setType(x, y, z, Material.GRASS);
            }
        }

        amount = random.nextInt((5 - 0) + 1) + 0;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.GRASS_BLOCK && y > 0)
            {
                y--;
            }

            if (y > 0 && limitedRegion.getBiome(x, y, z) != Biome.DESERT)
            {
                limitedRegion.setType(x, y, z, Material.OXEYE_DAISY);
            }
        }

        amount = random.nextInt((6 - 0) + 1) + 0;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.GRASS_BLOCK && y > 0)
            {
                y--;
            }

            if (y > 0 && limitedRegion.getBiome(x, y, z) != Biome.DESERT)
            {
                limitedRegion.setType(x, y, z, Material.POPPY);
            }
        }

        amount = random.nextInt((3 - 0) + 1) + 0;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.GRASS_BLOCK && y > 0)
            {
                y--;
            }

            if (y > 0 && limitedRegion.getBiome(x, y, z) != Biome.DESERT)
            {
                limitedRegion.setType(x, y, z, Material.CORNFLOWER);
            }
        }

        amount = random.nextInt((4 - 0) + 1) + 0;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.GRASS_BLOCK && y > 0)
            {
                y--;
            }

            if (y > 0 && limitedRegion.getBiome(x, y, z) != Biome.DESERT)
            {
                limitedRegion.setType(x, y, z, Material.DANDELION);
            }
        }

        amount = random.nextInt((12 - 1) + 1) + 1;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.WATER && y > 0)
            {
                y--;
            }

            if (y > 0 && limitedRegion.getBiome(x, y, z) != Biome.DESERT)
            {
                limitedRegion.setType(x, y, z, Material.LILY_PAD);
            }
        }

        amount = random.nextInt((120 - 40) + 1) + 40;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.STONE && y > 0)
            {
                y--;
            }

            if (y > 1)
            {
                final int depth = random.nextInt((y - 1 - 1) + 1) + 1;

                limitedRegion.setType(x, depth, z, Material.COAL_ORE);
            }

        }

        amount = random.nextInt((80 - 20) + 1) + 20;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.STONE && y > 0)
            {
                y--;
            }

            if (y > 1)
            {
                final int depth = random.nextInt((y - 1 - 1) + 1) + 1;

                limitedRegion.setType(x, depth, z, Material.IRON_ORE);
            }

        }

        amount = random.nextInt((160 - 60) + 1) + 60;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.STONE && y > 0)
            {
                y--;
            }

            if (y > 1)
            {
                final int depth = random.nextInt((y - 1 - 1) + 1) + 1;

                limitedRegion.setType(x, depth, z, Material.COPPER_ORE);
            }

        }

        amount = random.nextInt((40 - 15) + 1) + 15;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.STONE && y > 0)
            {
                y--;
            }

            if (y > 1)
            {
                final int depth = random.nextInt((y - 1 - 1) + 1) + 1;

                limitedRegion.setType(x, depth, z, Material.GOLD_ORE);
            }

        }

        amount = random.nextInt((4 - 2) + 1) + 2;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.STONE && y > 0)
            {
                y--;
            }

            if (y > 1)
            {
                final int depth = random.nextInt((y - 1 - 1) + 1) + 1;

                limitedRegion.setType(x, depth, z, Material.EMERALD_ORE);
            }

        }

        amount = random.nextInt((8 - 4) + 1) + 4;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.STONE && y > 0)
            {
                y--;
            }

            if (y > 1)
            {
                final int depth = random.nextInt((y - 1 - 1) + 1) + 1;

                limitedRegion.setType(x, depth, z, Material.DIAMOND_ORE);
            }

        }

        amount = random.nextInt((50 - 20) + 1) + 20;

        for (int i = 0; i < amount; i++)
        {
            final int x = random.nextInt(16) + chunkX * 16;
            final int z = random.nextInt(16) + chunkZ * 16;

            int y = 100;

            while (limitedRegion.getType(x, y - 1, z) != Material.STONE && y > 0)
            {
                y--;
            }

            if (y > 1)
            {
                final int depth = random.nextInt((y - 1 - 1) + 1) + 1;

                limitedRegion.setType(x, depth, z, Material.REDSTONE_ORE);
            }

        }
    }
}
