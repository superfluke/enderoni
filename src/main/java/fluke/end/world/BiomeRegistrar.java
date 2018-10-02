package fluke.end.world;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager.BiomeType;
import fluke.end.world.biomes.EndJungle;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeRegistrar 
{
	
	public static final Biome END_JUNGLE = new EndJungle();
	
	public static void registerBiomes()
	{
		initBiome(END_JUNGLE, "End Jungle");
	}
	
	private static void initBiome(Biome biome, String name)
	{
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
	}
}
