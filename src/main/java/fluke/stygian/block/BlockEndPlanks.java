package fluke.stygian.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import fluke.stygian.util.Reference;

public class BlockEndPlanks extends Block 
{
	public static final String REG_NAME = "endplanks";
	
	public BlockEndPlanks()
	{
		super(Material.WOOD);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
        setUnlocalizedName(Reference.MOD_ID + ".endplanks"); 
		setRegistryName(REG_NAME);
	}
	
	public void initModel() 
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
