package fluke.stygian.world;

import fluke.stygian.world.biomes.BiomeEndJungle;
import fluke.stygian.world.biomes.BiomeEndVolcano;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeRegistrar 
{
	
	public static final Biome END_JUNGLE = new BiomeEndJungle();
	public static final Biome END_VOLCANO = new BiomeEndVolcano();
	
	public static void registerBiomes()
	{
		initBiome(END_JUNGLE, "Stygian Growth");
		initBiome(END_VOLCANO, "Acidic Plains");
	}
	
	private static void initBiome(Biome biome, String name)
	{
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
	}
}
