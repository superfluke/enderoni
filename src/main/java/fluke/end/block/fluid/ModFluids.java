package fluke.end.block.fluid;

import fluke.end.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids 
{
	
	public static final EnderFluid ACID = (EnderFluid) new EnderFluid("endacid",
			new ResourceLocation(Reference.MOD_ID, "endacid_still"), 
			new ResourceLocation(Reference.MOD_ID, "endacid_flow"))
					.setHasBucket(true)
					.setDensity(1100)
					.setGaseous(false)
					//.setLuminosity(9)
					.setViscosity(900)
					.setTemperature(300)
					.setUnlocalizedName("endacid");

	public static void registerFluids()
    {
		System.out.println("Registering acid");
		FluidRegistry.registerFluid(ACID);
		FluidRegistry.addBucketForFluid(ACID);
//        if (ACID.isBucketEnabled())
//        {
//            FluidRegistry.addBucketForFluid(ACID);
//        }
    }
}
