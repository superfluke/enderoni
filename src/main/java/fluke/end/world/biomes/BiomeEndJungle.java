package fluke.end.world.biomes;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fluke.end.block.ModBlocks;
import fluke.end.world.feature.WorldGenEndPlant;
import fluke.end.world.feature.WorldGenEnderCanopy;
import fluke.end.world.feature.WorldGenSurfacePatch;

public class BiomeEndJungle extends Biome
{
	public static BiomeProperties properties = new BiomeProperties("End Jungle");
	public WorldGenerator endTallGrass;
	public WorldGenerator endGlowGrass;
	public WorldGenerator endGrassRemoval;
	public WorldGenerator endCanopyTree;
	private static final IBlockState AIR = Blocks.AIR.getDefaultState();
	private static final IBlockState END_STONE = Blocks.END_STONE.getDefaultState();
	private static final IBlockState END_GRASS = ModBlocks.endGrass.getDefaultState();
	private Random randy;
	
	static {
		properties.setTemperature(Biomes.SKY.getDefaultTemperature());
		properties.setRainfall(Biomes.SKY.getRainfall());
		properties.setRainDisabled();
	}
	
    public BiomeEndJungle()
    {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 10, 4, 4));
        this.topBlock = END_GRASS;
        this.fillerBlock = END_STONE;
        this.decorator = new BiomeEndDecorator();
        this.endGrassRemoval = new WorldGenSurfacePatch(END_STONE, END_GRASS, 1);
        this.endCanopyTree = new WorldGenEnderCanopy(true);
        endTallGrass = new WorldGenEndPlant(ModBlocks.endTallGrass.getDefaultState());
        endGlowGrass = new WorldGenEndPlant(ModBlocks.endGlowPlant.getDefaultState());
        randy = new Random();
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
    
    public void decorate(World world, Random rand, BlockPos pos)
    {	
    	for(int x=0; x<16; x++)
    	{
    		for(int z=0; z<16; z++)
    		{
    			int plantRoll = randy.nextInt(100);
    			if(plantRoll <= 17)
    			{
    				int terrainHeight = getEndSurfaceHeight(world, pos.add(x+8, 0, z+8), 52-randy.nextInt(5), 70);
    				if(terrainHeight > 0)
    				{
    					BlockPos plantPos = pos.add(x+8, terrainHeight, z+8);
    					if(plantRoll == 17)
    						endGlowGrass.generate(world, randy, plantPos);
    					else
    						endTallGrass.generate(world, randy, plantPos);
    				}
    			}
    		}
    	}
    				
		
		if(randy.nextInt(7) != 0)
			endGrassRemoval.generate(world, rand, pos.add(8, 0, 8));
		
		if(randy.nextInt(7) == 0)
		{
			int yHeight = getEndSurfaceHeight(world, pos.add(16, 0, 16), 50, 70);
			if(yHeight > 0)
				endCanopyTree.generate(world, rand, pos.add(16, yHeight+1, 16));
		}
		
		super.decorate(world, rand, pos);
    }
    
    private int getEndSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
    	int maxY = max;
    	int minY = min;
    	int currentY = maxY;
    	
    	while(currentY >= minY)
    	{
    		if(!world.isAirBlock(pos.add(0, currentY, 0)))
    				return currentY;
    		currentY--;
    	}
    	return 0;
    }
}
