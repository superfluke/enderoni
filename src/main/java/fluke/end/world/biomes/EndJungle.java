package fluke.end.world.biomes;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fluke.end.block.ModBlocks;

public class EndJungle extends Biome
{
	public static BiomeProperties properties = new BiomeProperties("End Jungle");
	public WorldGenerator endGrassGen;
	private static final IBlockState AIR = Blocks.AIR.getDefaultState();
	private static final IBlockState END_STONE = Blocks.END_STONE.getDefaultState();
	private static final IBlockState END_GRASS = ModBlocks.endGrass.getDefaultState();
	
	static {
		properties.setTemperature(Biomes.SKY.getDefaultTemperature());
		properties.setRainfall(Biomes.SKY.getRainfall());
		properties.setRainDisabled();
	}
	
    public EndJungle()
    {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 10, 4, 4));
        this.topBlock = Blocks.DIRT.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.decorator = new BiomeEndDecorator();
        this.endGrassGen = new WorldGenMinable(ModBlocks.endGrass.getDefaultState(), 20, BlockMatcher.forBlock(Blocks.END_STONE));
    }
    
    @Override
	public BiomeDecorator createBiomeDecorator()
    {
		return new BiomeDecoratorEndBiomes();
	}

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature)
    {
        return 0;
    }
    
    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
    	super.decorate(worldIn, rand, pos);
    	//endGrassGen.generate(worldIn, rand, pos.add(rand.nextInt(16), 55, rand.nextInt(16)));
    	//replaceTopBlock(worldIn, pos);
    }
    
    public void replaceTopBlock(World world, BlockPos pos) //this is busted
    {
    	IBlockState blocky;
    	//reset y value to 0 and offset x/z to not die due to cascading
    	pos = pos.add(10, -pos.getY(), 10); 
    	for(int x=0; x<6; x++)
    	{
    		for(int z=0; x<6; z++)
    		{
    			//lets assume that end stone only happens between y80 and y60.... right?
    			for(int y=80; y>59; y--)
    			{
    				blocky = world.getBlockState(pos.add(x, y, z));
    				if(blocky == END_STONE)
    				{
    					world.setBlockState(pos.add(x, y, z), END_GRASS);
    					break;
    				}
    			}
    		}
    	}
    }

}
