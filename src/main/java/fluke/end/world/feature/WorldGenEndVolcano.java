package fluke.end.world.feature;

import java.util.Random;

import fluke.end.util.FastNoise;
import fluke.end.util.FastNoise.NoiseType;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenEndVolcano extends WorldGenerator
{
	private final IBlockState volcMainState;
	private final IBlockState volcSecondaryState;
	private final IBlockState volcLiquid;
	private final FastNoise perlin;
	
	public WorldGenEndVolcano(IBlockState mainBlock, IBlockState subBlock, IBlockState liquid)
	{
		this.volcMainState = mainBlock;
		this.volcSecondaryState = subBlock;
		this.volcLiquid = liquid;
		perlin = new FastNoise();
		perlin.SetNoiseType(NoiseType.Perlin);
		perlin.SetFrequency(0.01F);
	}
	
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		int radius = 10;
		
		for(int y=0; radius-y > 1; y++)
		{
			int layerRadius = radius - y;
			double maxDist = layerRadius*layerRadius;
			
			for(int x=-layerRadius; x<=layerRadius; x++)
			{
				double xDist = x*x;
				
				for(int z=-layerRadius; z<=layerRadius; z++)
				{
					double zDist = z*z;
					if(xDist+zDist > maxDist)
						continue;
					
					world.setBlockState(pos.add(x, y*2, z), volcMainState);
					
				}
			}
		}
		return true;
	}

}
