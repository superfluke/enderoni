package fluke.stygian.world.biomes;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEndDecorator;

public class BiomeDecoratorEndBiomes extends BiomeEndDecorator
{
	public BiomeDecoratorEndBiomes()
	{
	}
	
	@Override
	protected void genDecorations(Biome biomeIn, World worldIn, Random random)
	{
		super.genDecorations(biomeIn, worldIn, random);
	}

}
