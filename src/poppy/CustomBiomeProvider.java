package poppy;

import java.util.List;
import java.util.Random;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class CustomBiomeProvider extends BiomeProvider
{
    final int WATER_HEIGHT = 25;

    @Override
    public Biome getBiome(WorldInfo worldInfo, int x, int y, int z)
    {
        final SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 10);
        generator.setScale(0.003);

        final double noise = generator.noise(x, z, 1, 1, true);

        if (noise < -0.6)
        {
            return Biome.PLAINS;
        }
        else if (noise < -0.2)
        {
            return Biome.BIRCH_FOREST;
        }
        else if (noise < 0.0)
        {
            return Biome.FOREST;
        }
        else if (noise < 0.2)
        {
            return Biome.DESERT;
        }
        else if (noise < 0.5)
        {
            return Biome.JUNGLE;
        }
        else if (noise < 0.7)
        {
            return Biome.SNOWY_PLAINS;
        }
        else if (noise < 1.0)
        {
            return Biome.FLOWER_FOREST;
        }

        return Biome.PLAINS;
    }

    @Override
    public List<Biome> getBiomes(WorldInfo worldInfo)
    {
    	return List.of(
            Biome.PLAINS,
            Biome.BIRCH_FOREST,
            Biome.FOREST,
            Biome.DESERT,
            Biome.JUNGLE,
            Biome.SNOWY_PLAINS,
            Biome.FLOWER_FOREST
        );
    }
}
