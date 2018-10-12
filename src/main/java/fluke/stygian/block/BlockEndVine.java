package fluke.stygian.block;

import fluke.stygian.util.Reference;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BlockEndVine extends BlockVine
{
	public static final String REG_NAME = "endvine";
	
	public BlockEndVine()
    {
        super();
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        setUnlocalizedName(Reference.MOD_ID + ".endvine");
        setRegistryName(REG_NAME);
    }
	
	public void initModel() 
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
