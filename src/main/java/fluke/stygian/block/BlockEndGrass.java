package fluke.stygian.block;

import fluke.stygian.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BlockEndGrass extends Block 
{
	public static final String REG_NAME = "endgrass";
	
	public BlockEndGrass()
	{
        super(Material.ROCK);
        this.setSoundType(SoundType.GROUND);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(0.5F);
        setUnlocalizedName(Reference.MOD_ID + ".endgrass");
        setRegistryName(REG_NAME);
	}
	
	public void initModel() 
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
