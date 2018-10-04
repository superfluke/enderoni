package fluke.end.world.biomes;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.client.Minecraft;
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
import fluke.end.world.feature.WorldGenEnderCanopy;
import fluke.end.world.feature.WorldGenReplaceEndSurface;
import fluke.end.world.feature.WorldGenSurfacePatch;

public class EndJungle extends Biome
{
	public static BiomeProperties properties = new BiomeProperties("End Jungle");
	public WorldGenerator endGrassGen;
	public WorldGenerator endGrassRemoval;
	public WorldGenerator endCanopyTree;
	private static final IBlockState AIR = Blocks.AIR.getDefaultState();
	private static final IBlockState END_STONE = Blocks.END_STONE.getDefaultState();
	private static final IBlockState END_GRASS = ModBlocks.endGrass.getDefaultState();
	private static final int GRASS_PER_CHUNK = 3;
	
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
        this.endGrassGen = new WorldGenReplaceEndSurface(END_GRASS, END_STONE, true);
        this.endGrassRemoval = new WorldGenSurfacePatch(END_STONE, END_GRASS, 1);
        this.endCanopyTree = new WorldGenEnderCanopy(true);
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
    	Random randy = new Random();
		endGrassGen.generate(worldIn, rand, pos.add(8, 0, 8));
		endGrassRemoval.generate(worldIn, rand, pos.add(8, 0, 8));
		
		if(randy.nextInt(14) == 0)
		{
			int yHeight = getEndSurfaceHeight(worldIn, pos.add(16, 0, 16));
			if(yHeight > 0)
				endCanopyTree.generate(worldIn, rand, pos.add(16, yHeight+1, 16));
		}
		
		super.decorate(worldIn, rand, pos);
    }
    
    private int getEndSurfaceHeight(World world, BlockPos pos)
    {
    	int maxY = 70;
    	int minY = 45;
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
