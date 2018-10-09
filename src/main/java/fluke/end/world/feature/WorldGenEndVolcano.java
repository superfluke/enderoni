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
		perlin.SetFrequency(0.1F);
	}
	
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		int radius = 9+rand.nextInt(6);
		int maxHeight = radius*2 - 1 - rand.nextInt(2);
		IBlockState volcBlock = volcMainState;
		
		perlin.SetFrequency(0.1F);
		for(int y=0; y < maxHeight; y++)
		{
//			if(y > 0 && rand.nextInt(radius-y + 2) == 0)
//				radius++;
			
			int layerRadius = radius - y/2;
			double maxDist = layerRadius*layerRadius;
			
			for(int x=-layerRadius; x<=layerRadius; x++)
			{
				double xDist = x*x;
				
				for(int z=-layerRadius; z<=layerRadius; z++)
				{
					double zDist = z*z;
					double noiseMod = (perlin.GetNoise(pos.getX()+x, y*2, pos.getZ()+z)+1)/2.0 + 0.5;
					double noisyDist = xDist*noiseMod + zDist*noiseMod;
					
					if(noisyDist > maxDist)
						continue;
					
					if(x == 0 && z == 0)
						volcBlock = volcLiquid;
					else if(rand.nextInt(14) == 0)
						volcBlock = volcSecondaryState;
					else
						volcBlock = volcMainState;
					
					IBlockState downState = world.getBlockState(pos.add(x, y, z).down());
					if(y == 0 ||  volcBlock == volcLiquid || downState == volcMainState || downState == volcSecondaryState)
						world.setBlockState(pos.add(x, y, z), volcBlock);
					
				}
			}
		}
		return true;
	}

}
