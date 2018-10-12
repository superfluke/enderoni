package fluke.stygian.block;

import java.util.Random;

import fluke.stygian.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;

public class BlockEndMagma extends Block
{
	public static final String REG_NAME = "endmagma";
	
	public BlockEndMagma()
	{
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setLightLevel(0.3F);
        this.setTickRandomly(true);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.STONE);
        setUnlocalizedName(Reference.MOD_ID + ".endmagma"); 
		setRegistryName(REG_NAME);
    }
	
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (entityIn instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase)entityIn))
        {
            entityIn.attackEntityFrom(DamageSource.GENERIC, 1.0F);
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    }
	
	public boolean canEntitySpawn(IBlockState state, Entity entityIn)
    {
        return false;
    }
	
	public void initModel() 
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
