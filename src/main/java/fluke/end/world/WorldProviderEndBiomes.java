package fluke.end.world;

import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderEndBiomes extends WorldProviderEnd 
{
	@Override
	public void init()
    {
        this.biomeProvider = new BiomeProviderSingle(Biomes.SKY);
        //this.biomeProvider = new EndBiomeProvider();
//        NBTTagCompound nbttagcompound = this.world.getWorldInfo().getDimensionData(this.world.provider.getDimension());
//        this.dragonFightManager = this.world instanceof WorldServer ? new DragonFightManager((WorldServer)this.world, nbttagcompound.getCompoundTag("DragonFight")) : null;
    }
	
	@Override
    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkGeneratorEndBiomes(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed(), this.getSpawnCoordinate());
    }

}
