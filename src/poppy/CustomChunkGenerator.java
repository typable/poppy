package poppy;

import java.util.Random;
import java.util.List;
import java.util.Arrays;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.World;


public class CustomChunkGenerator extends ChunkGenerator
{
    final int MAX_HEIGHT = 15;

	@Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData)
    {
        final boolean is_mountain = (random.nextInt((10 - 0) + 1) + 0) < 2;
        final boolean is_high_mountain = (random.nextInt((20 - 0) + 1) + 0) < 1;
        final int height = is_mountain ? (is_high_mountain ? random.nextInt((45 - 30) + 1) + 30 : random.nextInt((30 - 15) + 1) + 15) : random.nextInt((15 - 0) + 1) + 0;
        
    	for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                if (height + 2 < 4)
                {
                    for (int y = height + 1; y <= 4; y++)
                    {
                        chunkData.setBlock(x, y, z, Material.WATER);
                    }

                    chunkData.setBlock(x, height + 1, z, Material.SAND);

                    final boolean isStone = random.nextBoolean();

                    chunkData.setBlock(x, height, z, isStone ? Material.STONE : Material.SAND);

                    for (int y = 1; y < height; y++)
                    {
                        chunkData.setBlock(x, y, z, Material.STONE);
                    }

                    chunkData.setBlock(x, 0, z, Material.BEDROCK);
                }
                else
                {
                    chunkData.setBlock(x, height + 2, z, Material.GRASS_BLOCK);
                    chunkData.setBlock(x, height + 1, z, Material.DIRT);

                    final boolean isStone = random.nextBoolean();

                    chunkData.setBlock(x, height, z, isStone ? Material.STONE : Material.DIRT);

                    for (int y = 1; y < height; y++)
                    {
                        chunkData.setBlock(x, y, z, Material.STONE);
                    }
                
                    chunkData.setBlock(x, 0, z, Material.BEDROCK);
                }
            }
        }
    }

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world)
	{
		return Arrays.asList((BlockPopulator) new CustomTreePopulator(), (BlockPopulator) new CustomGrassPopulator());
	}
}
