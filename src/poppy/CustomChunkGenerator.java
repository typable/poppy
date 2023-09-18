package poppy;

import java.util.Random;
import java.util.List;
import java.util.Arrays;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.bukkit.Material;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.World;
import org.bukkit.block.Biome;


public class CustomChunkGenerator extends ChunkGenerator
{
    final int WATER_HEIGHT = 25;

	@Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData)
    {
        final SimplexOctaveGenerator generator = new SimplexOctaveGenerator(random, 10); // new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 10);

        final int offset = random.nextInt((20 - 0) + 1) + 0;
        final double scale = 0.005 + (0.02 - 0.005) * random.nextDouble();

        generator.setScale(scale);

        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                final int height = (int) (generator.noise(chunkX * 16 + x, chunkZ * 16 + z, 0.5, 0.5) * 20 + 30) + offset;
                final Biome biome = chunkData.getBiome(x, height, z);

                if (height < WATER_HEIGHT)
                {
                    for (int i = WATER_HEIGHT;  i > height; i--)
                    {
                        if (i == WATER_HEIGHT)
                        {
                            switch (biome)
                            {
                                case SNOWY_PLAINS:
                                    chunkData.setBlock(x, i, z, Material.ICE);
                                    break;
                                default:
                                    chunkData.setBlock(x, i, z, Material.WATER);
                                    break;
                            }
                            continue;
                        }

                        chunkData.setBlock(x, i, z, Material.WATER);
                    }
                }

                if (height < WATER_HEIGHT + 2)
                {
                    switch (biome)
                    {
                        case SNOWY_PLAINS:
                            chunkData.setBlock(x, height + 1, z, Material.SNOW);
                            chunkData.setBlock(x, height, z, Material.GRAVEL);
                            chunkData.setBlock(x, height - 1, z, Material.GRAVEL);
                            break;
                        default:
                            chunkData.setBlock(x, height, z, Material.SAND);
                            chunkData.setBlock(x, height - 1, z, Material.SANDSTONE);
                            break;
                    }
                }
                else
                {
                    switch (biome)
                    {
                        case SNOWY_PLAINS:
                            chunkData.setBlock(x, height + 1, z, Material.SNOW);
                            chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);
                            chunkData.setBlock(x, height - 1, z, Material.DIRT);
                            break;
                        case DESERT:
                            chunkData.setBlock(x, height, z, Material.SAND);
                            chunkData.setBlock(x, height - 1, z, Material.SANDSTONE);
                            break;
                        default:
                            chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);
                            chunkData.setBlock(x, height - 1, z, Material.DIRT);
                            break;
                    }
                }

                for (int i = height - 2;  i >= 0; i--)
                {
                    chunkData.setBlock(x, i, z, Material.STONE);
                }

                for (int i = -1;  i > -64; i--)
                {
                    chunkData.setBlock(x, i, z, Material.DEEPSLATE);
                }

                chunkData.setBlock(x, -64, z, Material.BEDROCK);
            }
        }
    }

    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo)
    {
    	return new CustomBiomeProvider();
    }

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world)
	{
		return Arrays.asList((BlockPopulator) new CustomTreePopulator(), (BlockPopulator) new CustomGrassPopulator());
	}
}
