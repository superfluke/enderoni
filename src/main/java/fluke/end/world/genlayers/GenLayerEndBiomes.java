package fluke.end.world.genlayers;

import fluke.end.End;
import fluke.end.world.BiomeRegistrar;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerEndBiomes extends GenLayer
{
	private final int SKY_ID;
	private final int END_FOREST_ID;
	private final int END_VOLCANO_ID;
	private final int PLACEHOLDER;
	
	static 
	{

//		SKY_ID = Biome.getIdForBiome(Biomes.SKY);
//		END_FOREST_ID = Biome.getIdForBiome(Biomes.MESA);
//		END_VOLCANO_ID = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND);
//		PLACEHOLDER = SKY_ID;
	}
	
    public GenLayerEndBiomes(long seed, GenLayer parent)
    {
        super(seed);
        this.parent = parent;  
		SKY_ID = Biome.getIdForBiome(Biomes.SKY);
		END_FOREST_ID = Biome.getIdForBiome(BiomeRegistrar.END_JUNGLE);
		END_VOLCANO_ID = Biome.getIdForBiome(BiomeRegistrar.END_VOLCANO);
		PLACEHOLDER = SKY_ID;
    }
    
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        int[] inLayer = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] outLayer = IntCache.getIntCache(areaWidth * areaHeight);
    
        for (int i = 0; i < areaHeight; ++i)
        {
            for (int j = 0; j < areaWidth; ++j)
            {
            	this.initChunkSeed((long)(j + areaX), (long)(i + areaY));
                int biomeInt = inLayer[j + i * areaWidth];
                
                if(biomeInt == 0 || (areaX < 10 && areaX > -10 && areaY < 10 && areaY > -10))
                {
                	outLayer[j + i * areaWidth] = SKY_ID;
                }
                else if(biomeInt == 1)
                {
                	outLayer[j + i * areaWidth] = END_FOREST_ID;
                }
                else if(biomeInt == 3)
                {
                	outLayer[j + i * areaWidth] = END_VOLCANO_ID;
                }
                else if(biomeInt == 4)
                {
                	outLayer[j + i * areaWidth] = PLACEHOLDER;
                }
                else
                {
                	End.LOGGER.warn("Shit: biome id " + biomeInt + " found in genlayer");
                	outLayer[j + i * areaWidth] = SKY_ID;
                }
            	
            }
            
        }
        
        return outLayer;
    
    }
	
}
