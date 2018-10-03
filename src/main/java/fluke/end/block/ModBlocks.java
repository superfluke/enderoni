package fluke.end.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import fluke.end.util.Reference;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ModBlocks 
{
	@GameRegistry.ObjectHolder(BlockEndLog.REG_NAME)
    public static BlockEndLog endLog;
	
	@GameRegistry.ObjectHolder(BlockEndLeaves.REG_NAME)
    public static BlockEndLeaves endLeaves;
	
	@GameRegistry.ObjectHolder(BlockEndGrass.REG_NAME)
    public static BlockEndGrass endGrass;
	
	@GameRegistry.ObjectHolder(BlockEndTallGrass.REG_NAME)
    public static BlockEndTallGrass endTallGrass;
	
	@GameRegistry.ObjectHolder(BlockEndGlowPlant.REG_NAME)
    public static BlockEndGlowPlant endGlowPlant;
	
	@GameRegistry.ObjectHolder(BlockEndCanopySapling.REG_NAME)
    public static BlockEndCanopySapling endCanopySapling;
	
	@GameRegistry.ObjectHolder(BlockEndVine.REG_NAME)
    public static BlockEndVine endVine;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) 
	{
		IForgeRegistry<Block> reggy = event.getRegistry();
		reggy.register(new BlockEndLog());
		reggy.register(new BlockEndLeaves());
		reggy.register(new BlockEndGrass());
		reggy.register(new BlockEndTallGrass());
		reggy.register(new BlockEndGlowPlant());
		reggy.register(new BlockEndCanopySapling());
		reggy.register(new BlockEndVine());
	}
	
	@SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) 
	{
		IForgeRegistry<Item> reggy = event.getRegistry();
		reggy.register(new ItemBlock(ModBlocks.endLog).setRegistryName(ModBlocks.endLog.getRegistryName()));
		reggy.register(new ItemBlock(ModBlocks.endLeaves).setRegistryName(ModBlocks.endLeaves.getRegistryName()));
		reggy.register(new ItemBlock(ModBlocks.endGrass).setRegistryName(ModBlocks.endGrass.getRegistryName()));
		reggy.register(new ItemBlock(ModBlocks.endTallGrass).setRegistryName(ModBlocks.endTallGrass.getRegistryName()));
		reggy.register(new ItemBlock(ModBlocks.endGlowPlant).setRegistryName(ModBlocks.endGlowPlant.getRegistryName()));
		reggy.register(new ItemBlock(ModBlocks.endCanopySapling).setRegistryName(ModBlocks.endCanopySapling.getRegistryName()));
		reggy.register(new ItemBlock(ModBlocks.endVine).setRegistryName(ModBlocks.endVine.getRegistryName()));
	}
	
	@SideOnly(Side.CLIENT)
    public static void initModels() {
		endLog.initModel();
		endLeaves.initModel();
		endGrass.initModel();
		endTallGrass.initModel();
		endGlowPlant.initModel();
		endCanopySapling.initModel();
		endVine.initModel();
    }
}
	
