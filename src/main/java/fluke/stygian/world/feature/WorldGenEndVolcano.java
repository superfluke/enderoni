package fluke.stygian.world.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fluke.stygian.util.FastNoise;
import fluke.stygian.util.FastNoise.NoiseType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenEndVolcano extends WorldGenerator
{
	private final IBlockState volcMainState;
	private final IBlockState volcSecondaryState;
	private final IBlockState volcLiquid;
	private final FastNoise perlin;
	private final static IBlockState AIR = Blocks.AIR.getDefaultState();
	
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
		List<BlockPos> firstLayer = getFirstLayer(world, pos, radius);
		
		if(firstLayer == null)
			return false;
		else
		{
			for(BlockPos baseBlock : firstLayer)
			{
				world.setBlockState(baseBlock, volcBlock);
			}
		}
		
		for(int y=1; y < maxHeight; y++)
		{
			int layerRadius = radius - y/2;
			double maxDist = layerRadius*layerRadius;
			
			for(int x=-layerRadius-2; x<=layerRadius+2; x++)
			{
				double xDist = x*x;
				
				for(int z=-layerRadius-2; z<=layerRadius+2; z++)
				{
					double zDist = z*z;
					double noiseMod = (perlin.GetNoise(pos.getX()+x, y*2, pos.getZ()+z)+1)/2.0 + 0.5;
					double noisyDist = xDist*noiseMod + zDist*noiseMod;
					
					if(noisyDist > maxDist)
						continue;
					
					if(x == 0 && z == 0)
						volcBlock = volcLiquid;
					else if(y < 12 && xDist <= 1 && zDist <= 1)
						volcBlock = volcLiquid;
					else if(rand.nextInt(11) == 0)
						volcBlock = volcSecondaryState;
					else
						volcBlock = volcMainState;
					
					IBlockState downState = world.getBlockState(pos.add(x, y, z).down());
					if(y == 0 ||  volcBlock == volcLiquid || downState == volcMainState || downState == volcSecondaryState || (y==12 && (xDist <= 1 && zDist <= 1)))
						world.setBlockState(pos.add(x, y, z), volcBlock);
					
				}
			}
		}		
		return true;
	}
	
	//returns a list of block pos for first layer of volcano, may be up to -2 lower than starting pos if terrain lowers
	//will return null if terrain is not flat enough for volcano
	public List<BlockPos> getFirstLayer(World world, BlockPos pos, int radius)
	{
		List<BlockPos> firstLayer = new ArrayList<BlockPos>();
		double maxDist = radius*radius;
		
		for(int x=-radius-2; x<=radius+2; x++)
		{
			double xDist = x*x;
			
			for(int z=-radius-2; z<=radius+2; z++)
			{
				double zDist = z*z;
				double noiseMod = (perlin.GetNoise(pos.getX()+x, 0, pos.getZ()+z)+1)/2.0 + 0.5;
				double noisyDist = xDist*noiseMod + zDist*noiseMod;
				
				if(noisyDist > maxDist)
					continue;
				
				BlockPos checkPos = pos.add(x, 0, z).down();
				IBlockState downState = world.getBlockState(checkPos);
				if(downState != AIR)
				{
					firstLayer.add(pos.add(x, 0, z));
				}
				else if(world.getBlockState(checkPos.down()) != AIR)
				{
					firstLayer.add(pos.add(x, 0, z));
					firstLayer.add(pos.add(x, -1, z));
				}
				else if(world.getBlockState(checkPos.down().down()) != AIR)
				{
					firstLayer.add(pos.add(x, 0, z));
					firstLayer.add(pos.add(x, -1, z));
					firstLayer.add(pos.add(x, -2, z));
				}
				else
				{
					return null;
				}
				
			}
		}
		
		return firstLayer;
	}

}
