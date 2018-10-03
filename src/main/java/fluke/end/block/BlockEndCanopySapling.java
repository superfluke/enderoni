package fluke.end.block;

import java.util.Random;

import fluke.end.util.Reference;
import fluke.end.world.feature.WorldGenEnderCanopy;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.model.ModelLoader;

public class BlockEndCanopySapling extends BlockBush implements IGrowable
{
	public static final String REG_NAME = "endcanopysapling";
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
    
    public BlockEndCanopySapling()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, Integer.valueOf(0)));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        setUnlocalizedName(Reference.MOD_ID + ".endcanopysapling");
        setRegistryName(REG_NAME);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SAPLING_AABB;
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }
    
    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (((Integer)state.getValue(STAGE)).intValue() == 0)
        {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        }
        else
        {
            this.generateTree(worldIn, pos, state, rand);
        }
    }
    
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) 
        	return;
        
        WorldGenerator tree = new WorldGenEnderCanopy(true);
        tree.generate(worldIn, rand, pos);
    
    }
    
    /**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(STAGE, Integer.valueOf((meta & 8) >> 3));
	}
	
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | state.getValue(STAGE).intValue() << 3;
		return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { STAGE });
	}
	
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return (double)worldIn.rand.nextFloat() < 0.45D;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.grow(worldIn, pos, state, rand);
    }
    
    public void initModel() 
	{
		IStateMapper mappy = (new StateMap.Builder()).ignore(new IProperty[] { STAGE }).build();
		ModelLoader.setCustomStateMapper(this, mappy);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
