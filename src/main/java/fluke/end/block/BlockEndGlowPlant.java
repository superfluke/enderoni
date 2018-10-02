package fluke.end.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import fluke.end.util.Reference;

public class BlockEndGlowPlant extends BlockBush implements net.minecraftforge.common.IShearable
{
    protected static final AxisAlignedBB TALL_GRASS_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
    public static final String REG_NAME = "endglow";

    public BlockEndGlowPlant()
    {
    	super(Material.VINE, MapColor.PURPLE);
    	this.setSoundType(SoundType.PLANT);
    	this.setLightLevel(0.5F);
    	setUnlocalizedName(Reference.MOD_ID + ".endglow");
        setRegistryName(REG_NAME);
    }
    
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return TALL_GRASS_AABB;
    }
    
    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        {
            Block soil = worldIn.getBlockState(pos.down()).getBlock();
            return soil == Blocks.END_STONE || soil == ModBlocks.endGrass;
        }
        return canSustainBush(worldIn.getBlockState(pos.down()));
    }
    
    @Override
    protected boolean canSustainBush(IBlockState state)
    {
    	Block soil = state.getBlock();
    	return soil == Blocks.END_STONE || soil == ModBlocks.endGrass;
    }
    
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }
    
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }
    
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(ModBlocks.endGlowPlant, 1, 0));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }
    
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, 0);
    }
    
    @Override public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
    { 
    	return true; 
    }
    
    @Override
    public NonNullList<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        return NonNullList.withSize(1, new ItemStack(ModBlocks.endGlowPlant, 1, 0));
    }
    
    public void initModel() 
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
