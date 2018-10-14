package fluke.stygian;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fluke.stygian.block.fluid.ModFluids;
import fluke.stygian.proxy.CommonProxy;
import fluke.stygian.util.DebugCommand;
import fluke.stygian.util.Reference;
import fluke.stygian.world.BiomeRegistrar;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptableRemoteVersions="*")
public class Stygian 
{
	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

	@Instance
	public static Stygian instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	static 
	{
		FluidRegistry.enableUniversalBucket();
	}
	
	@EventHandler
	public static void preInit(FMLInitializationEvent event)
	{
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		proxy.init();
		BiomeRegistrar.registerBiomes();
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event)
	{
	}
	
	@EventHandler
	public void startServer(FMLServerStartingEvent event) 
	{
		//event.registerServerCommand(new DebugCommand()); //TODO delete
	}
}