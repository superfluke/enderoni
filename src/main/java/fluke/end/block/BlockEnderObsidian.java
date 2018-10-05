package fluke.end.block;

import fluke.end.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BlockEnderObsidian extends Block
{
	public static final String REG_NAME = "endobsidian";
	
	public BlockEnderObsidian()
    {
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(50.0F);
        this.setResistance(2000.0F);
        this.setSoundType(SoundType.STONE);
        setUnlocalizedName(Reference.MOD_ID + ".endobsidian"); 
		setRegistryName(REG_NAME);
    }
	
	public void initModel() 
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
