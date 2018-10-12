package fluke.stygian.config;

import fluke.stygian.util.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID, category = "")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class Configs 
{
	public static ConfigWorldGen worldgen = new ConfigWorldGen();
	
	public static class ConfigWorldGen 
	{
		@Config.Comment({"Controls size of end biomes. Larger number = larger biomes", "Default: 4"})
		@Config.RequiresWorldRestart
		public int endBiomeSize = 4;
		
		@Config.Comment({"Controls how often large end trees generate. Larger number = less trees", "Default: 7"})
		@Config.RequiresWorldRestart
		public int treeFrequency = 7;
	}
	
	@SubscribeEvent
	public static void onConfigReload(ConfigChangedEvent.OnConfigChangedEvent event) 
	{
		if (Reference.MOD_ID.equals(event.getModID()))
			ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
	}

}
