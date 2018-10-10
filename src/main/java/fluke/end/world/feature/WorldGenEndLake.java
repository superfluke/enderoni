package fluke.end.world.feature;

import java.util.Random;

import fluke.end.util.FastNoise;
import fluke.end.util.FastNoise.NoiseType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenEndLake extends WorldGenerator
{
	private FastNoise perlin;
	private final IBlockState fluid;
	private final IBlockState rim;
	private static final IBlockState AIR = Blocks.AIR.getDefaultState();
	
	public WorldGenEndLake(IBlockState fluid, IBlockState rim)
	{
		this.fluid = fluid;
		this.rim = rim;
		perlin = new FastNoise();
		perlin.SetNoiseType(NoiseType.Perlin);
		perlin.SetFrequency(0.075F);
		
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) 
	{
		
		short[] lake = genLakeShape(world, rand, pos);
		//lake = createLakeRim(lake);
		if(lake == null)
			return false;
		
		for(int x=-16; x<16; x++)
		{
			for(int z=-16; z<16; z++)
			{
				short depth = lake[arrayIndex(x, z)];
				if(depth == -1)
				{
					if(world.getBlockState(pos.add(x, 0, z)) == AIR)
						world.setBlockState(pos.add(x, 0, z), rim);
				}
				else
				{
					for(int d=0; d<depth; d++)
						world.setBlockState(pos.add(x, -d, z), fluid);
				}

			}
		}


		return true;
	}
	
	private short[] createLakeRim(short[] lake) 
	{
		for(int x=-16; x<16; x++)
		{
			for(int z=-16; z<16; z++)
			{
				if(lake[arrayIndex(x, z)] > 0)
				{
					for(int xSide=-1; xSide<=1; xSide++)
					{
						for(int zSide=-1; zSide<=1; zSide++)
						{
							//only check cardinal directions
							if((Math.abs(xSide) == 1) ^ (Math.abs(zSide) == 1))
							{
								//ensure we are inside array bounds
								if(x+xSide >= -16 &&
										x+xSide < 16 &&
										z+zSide >= -16 &&
										z+zSide < 16)
								{
									//if a lake block is touching air, set that air block to -1 to place a solid block there later
									if(lake[arrayIndex(x+xSide, z+zSide)] == 0)
										lake[arrayIndex(x, z)] = -1;
								}
							}
						}
					}
				}
			}	
		}
		return lake;
	}

	private short[] genLakeShape(World world, Random rand, BlockPos pos)
	{
		short[] lake = new short[1024];
		
		for(int n=0; n<1; n++)
		{
			int radius;
			int posXoffset = 0;
			int posZoffset = 0;
			
			if(n == 0)
			{
				radius = 6+rand.nextInt(4);
			}
			else
			{
				radius = 4+rand.nextInt(3);
				
				posXoffset = 6+rand.nextInt(3);
				posZoffset = 6+rand.nextInt(3);
				if(rand.nextBoolean())
					posXoffset *= -1;
				if(rand.nextBoolean())
					posZoffset *= -1;
				
				//pos = pos.add(posXoffset, 0, posZoffset);

			}
			
			double maxDist = radius*radius - 1;
			double deepDist = (radius-1)*(radius-2)-2;
			
			for(int x=-radius-3-posXoffset; x<=radius+3+posXoffset; x++)
			{
				
				for(int z=-radius-3-posZoffset; z<=radius+3+posZoffset; z++)
				{
					if(lake[arrayIndex(x, z)] == 0)
					{
						int xOffset = (int)(perlin.GetNoise(pos.getX()+x+posXoffset*2, pos.getX()+z+posZoffset*2)*4 + 0.5);
						double xDist = (x+xOffset)*(x+xOffset);
						int zOffset = (int)(perlin.GetNoise(pos.getZ()+x+posXoffset*2, pos.getZ()+z+posZoffset*2)*4 + 0.5);
						double zDist = (z+zOffset)*(z+zOffset);		
						
						short depth = 0;
						
						if(zDist+xDist < deepDist)
							depth = 2;
						else if(zDist+xDist <= maxDist)
							depth = 1;
						
						if(depth > 0)
						{
							if(world.getBlockState(pos.add(x, -depth-1, z)) == AIR)
								return null;
							else //if(n==0)
								lake[arrayIndex(x+posXoffset, z+posZoffset)] = depth;
//							else
//								lake[arrayIndex(x+posXoffset, z+posZoffset)] = -1;
						}
					}
				}
			}
		}
		
		return lake;
	}
	
	//convert x, z in range [-16, 16) into 1D array index
	private int arrayIndex(int x, int z)
	{
		int arryPos = (x+16) + (z+16)*32;
		if(arryPos < 0)
			return 0;
		else if(arryPos >= 1024)
			return 1023;
		
		return arryPos;
	}

}
