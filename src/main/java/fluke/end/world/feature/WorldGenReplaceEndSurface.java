package fluke.end.world.feature;

import java.util.Random;

import fluke.end.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenReplaceEndSurface extends WorldGenerator
{
	private final IBlockState surfaceBlockState;
	private final IBlockState replacementBlockState;
	private final boolean addEndFoliage;
	private final Random randy;
	private final static IBlockState END_TALL_GRASS = ModBlocks.endTallGrass.getDefaultState();
	private final static IBlockState END_GLOW_PLANT = ModBlocks.endGlowPlant.getDefaultState();

    public WorldGenReplaceEndSurface(IBlockState generate, IBlockState replace, boolean addEndFoliage)
    {
        this.surfaceBlockState = generate;
        this.replacementBlockState = replace;
        this.addEndFoliage = addEndFoliage;
        randy = new Random(800813);
    }
    
    public boolean generate(World world, Random rand, BlockPos pos)
    {
		for(int x=0; x<16; x++)
		{
			for(int z=0; z<16; z++)
			{
					
				BlockPos current = pos.add(x, 70, z);
				
				//find the surface block
				int minY = 52 - randy.nextInt(5);
				for (; current.getY() > minY && world.isAirBlock(current); current = current.down())
		        {
		            ;
		        }
				
				if(world.getBlockState(current) == replacementBlockState)
				{
					world.setBlockState(current, surfaceBlockState, 2);
					if(addEndFoliage)
						genEndPlants(world, current);
						
				}
			}
		}
    	return true;
    }
    
    //faster to put this here rather than its own WorldGenerator since we already know what the surface block location is
    private void genEndPlants(World world, BlockPos pos)
    {
    	int plantRng = randy.nextInt(16);
    	
    	if(plantRng <= 5)
    		world.setBlockState(pos.up(), END_TALL_GRASS, 2);
    	else if(plantRng <= 6)
    		world.setBlockState(pos.up(), END_GLOW_PLANT, 2);
    }

}
