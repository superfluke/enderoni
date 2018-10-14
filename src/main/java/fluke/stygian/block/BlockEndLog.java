package fluke.stygian.block;

import java.util.Random;

import fluke.stygian.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockEndLog extends Block 
{
	public static final String REG_NAME = "endlog";
	private final Random randy;
	
	public BlockEndLog()
	{
		super(Material.WOOD);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
        setUnlocalizedName(Reference.MOD_ID + ".endlog"); 
		setRegistryName(REG_NAME);
		randy = new Random();
	}
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = 4;
        int j = 5;

        if (worldIn.isAreaLoaded(pos.add(-5, -5, -5), pos.add(5, 5, 5)))
        {
            for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-4, -4, -4), pos.add(4, 4, 4)))
            {
                IBlockState iblockstate = worldIn.getBlockState(blockpos);

                if (iblockstate.getBlock().isLeaves(iblockstate, worldIn, blockpos))
                {
                    iblockstate.getBlock().beginLeavesDecay(iblockstate, worldIn, blockpos);
                }
            }
        }
        
        if(randy.nextInt(20) == 0 && worldIn.provider.getDimension() == 1)
        {
        	Entity endermite = new EntityEndermite(worldIn);
        	endermite.setLocationAndAngles((double)pos.getX() + 0.5F, (double)pos.getY() + 0.5F, (double)pos.getZ() + 0.5F, randy.nextFloat() * 360.0F, 0.0F);
            worldIn.spawnEntity(endermite);
        }
    }
	
	@Override 
	public boolean canSustainLeaves(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos)
	{ 
		return true; 
	}
	
    @Override 
    public boolean isWood(net.minecraft.world.IBlockAccess world, BlockPos pos)
    { 
    	return true; 
    }

	public void initModel() 
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
