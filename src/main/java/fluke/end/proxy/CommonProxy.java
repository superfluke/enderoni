package fluke.end.proxy;

import fluke.end.world.WorldProviderEndBiomes;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class CommonProxy 
{
	
	public void init() 
	{
		overrideEnd();
	}

	
	public void overrideEnd()
	{
    	DimensionManager.unregisterDimension(1);
        DimensionType endBiomes = DimensionType.register("End", "_end", 1, WorldProviderEndBiomes.class, false);
        DimensionManager.registerDimension(1, endBiomes);
    }

}