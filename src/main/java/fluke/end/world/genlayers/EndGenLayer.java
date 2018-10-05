package fluke.end.world.genlayers;

import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerDeepOcean;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerShore;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public abstract class EndGenLayer 
{
    /** seed from World#getWorldSeed that is used in the LCG prng */
    private long worldGenSeed;
    /** parent GenLayer that was provided via the constructor */
    protected GenLayer parent;
    /**
     * final part of the LCG prng that uses the chunk X, Z coords along with the other two seeds to generate
     * pseudorandom numbers
     */
    private long chunkSeed;
    /** base seed to the LCG prng provided via the constructor */
    protected long baseSeed;

    public static GenLayer[] initializeAllBiomeGenerators2(long seed, WorldType worldtype, ChunkGeneratorSettings genSettings)
    {
        GenLayer genlayer = new GenLayerIsland(1L);
        genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
        GenLayer genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
        GenLayer genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        GenLayer genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
        genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
        genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
        GenLayer genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland1);
        GenLayer genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
        GenLayer genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddsnow);
//        GenLayer genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, GenLayerEdge.Mode.COOL_WARM);
        
//        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
//        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        
        GenLayer genlayerzoom1 = new GenLayerZoom(2002L, genlayeraddisland2);
        genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
        
//        GenLayer genlayeraddisland3 = new GenLayerAddIsland(4L, genlayerzoom1);
//        GenLayer genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland3);
//        GenLayer genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        
        GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayerzoom1, 0);
        
        int biomeSize = 4;
        int riverSize = biomeSize;

        GenLayer lvt_7_1_ = GenLayerZoom.magnify(1000L, genlayer4, 0);
        GenLayer genlayerriverinit = lvt_7_1_;//new GenLayerRiverInit(100L, lvt_7_1_);
//        GenLayer genlayerbiomeedge = worldtype.getBiomeLayer(seed, genlayer4, genSettings);
        //----
//        GenLayer genlayerbiomeedge = GenLayerBiome(200L, genlayer4, worldtype, genSettings);
//        genlayerbiomeedge = GenLayerZoom.magnify(1000L, ret, 2);
        //----
        GenLayer endbiomes = new GenLayerEndBiomes(200L, genlayer4);
        endbiomes = GenLayerZoom.magnify(1000L, endbiomes, 2);
       // genlayer4 = GenLayerZoom.magnify(1000L, genlayer4, 0);
//        GenLayer lvt_9_1_ = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
//        GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, lvt_9_1_);
        GenLayer genlayer5 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer5 = GenLayerZoom.magnify(1000L, genlayer5, riverSize);
        //GenLayer genlayerriver = new GenLayerRiver(1L, genlayer5);
        GenLayer genlayerriver = genlayer5;
        GenLayer genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
//        genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);

        for (int k = 0; k < biomeSize; ++k)
        {
        	genlayerriver = new GenLayerZoom((long)(1000 + k), endbiomes);

            if (k == 0)
            {
            	genlayerriver = new GenLayerAddIsland(3L, endbiomes);
            }

//            if (k == 1 || biomeSize == 1)
//            {
//            	genlayer4 = new GenLayerShore(1000L, genlayer4);
//            }
        }

        GenLayer genlayersmooth1 = new GenLayerSmooth(1000L, endbiomes);
        GenLayer genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayer genlayer3 = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(seed);
        genlayer3.initWorldGenSeed(seed);
        return new GenLayer[] {genlayerrivermix, genlayer3, genlayerrivermix};
    }
    
    public static GenLayer[] initializeAllBiomeGenerators(long seed, WorldType p_180781_2_, ChunkGeneratorSettings p_180781_3_)
    {
        GenLayer genlayer = new GenLayerIsland(1L);
        genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
        GenLayer genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
        GenLayer genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        GenLayer genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
        genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
        genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
        GenLayer genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland1);
        GenLayer genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
        GenLayer genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddsnow);
        //GenLayer genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, GenLayerEdge.Mode.COOL_WARM);
        //genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        //genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayer genlayeredge = genlayeraddisland2;
        GenLayer genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
        genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
        GenLayer genlayeraddisland3 = new GenLayerAddIsland(4L, genlayerzoom1);
        //GenLayer genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland3);
        //GenLayer genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        GenLayer genlayerdeepocean = genlayeraddisland3;
        GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
        int i = 4;
        int j = i;

        if (p_180781_3_ != null)
        {
            i = p_180781_3_.biomeSize;
            j = p_180781_3_.riverSize;
        }

        if (p_180781_2_ == WorldType.LARGE_BIOMES)
        {
            i = 6;
        }

        i = getModdedBiomeSize(p_180781_2_, i);

        GenLayer lvt_7_1_ = GenLayerZoom.magnify(1000L, genlayer4, 0);
        GenLayer genlayerriverinit = new GenLayerRiverInit(100L, lvt_7_1_);
        //GenLayer genlayerbiomeedge = p_180781_2_.getBiomeLayer(seed, genlayer4, p_180781_3_);
        GenLayer genlayerbiomeedge = new GenLayerEndBiomes(200L, genlayer4);
        GenLayer lvt_9_1_ = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, lvt_9_1_);
        GenLayer genlayer5 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer5 = GenLayerZoom.magnify(1000L, genlayer5, j);
        GenLayer genlayerriver = new GenLayerRiver(1L, genlayer5);
        GenLayer genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        //genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);

        for (int k = 0; k < i; ++k)
        {
            genlayerhills = new GenLayerZoom((long)(1000 + k), genlayerhills);

            if (k == 0)
            {
                genlayerhills = new GenLayerAddIsland(3L, genlayerhills);
            }

            if (k == 1 || i == 1)
            {
                genlayerhills = new GenLayerShore(1000L, genlayerhills);
            }
        }

        GenLayer genlayersmooth1 = new GenLayerSmooth(1000L, genlayerhills);
        GenLayer genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayer genlayer3 = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        //GenLayer genlayer3 = GenLayerZoom.magnify(10L, genlayerrivermix, 1);
        genlayerrivermix.initWorldGenSeed(seed);
        genlayer3.initWorldGenSeed(seed);
        //end biomes seem non existent in genlayer3 after voronoiZoom
        //who tf knows why tho BECAUSE THIS WHOLE STRUCTURE IS BLACK FUCKING MAGIC
        return new GenLayer[] {genlayerrivermix, genlayer3, genlayerrivermix};
    }


    public EndGenLayer(long seed)
    {
        this.baseSeed = seed;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += seed;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += seed;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += seed;
    }

    /**
     * Initialize layer's local worldGenSeed based on its own baseSeed and the world's global seed (passed in as an
     * argument).
     */
    public void initWorldGenSeed(long seed)
    {
        this.worldGenSeed = seed;

        if (this.parent != null)
        {
            this.parent.initWorldGenSeed(seed);
        }

        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
    }

    /**
     * Initialize layer's current chunkSeed based on the local worldGenSeed and the (x,z) chunk coordinates.
     */
    public void initChunkSeed(long xSeed, long zSeed)
    {
        this.chunkSeed = this.worldGenSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += xSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += zSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += xSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += zSeed;
    }

    /**
     * Generates a pseudo random number between 0 and another integer.
     */
    protected int nextInt(int max)
    {
        int i = (int)((this.chunkSeed >> 24) % (long)max);

        if (i < 0)
        {
            i += max;
        }

        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return i;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or Biome ID's based on the particular GenLayer subclass.
     */
    public abstract int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight);

    protected static boolean biomesEqualOrMesaPlateau(int biomeIDA, int biomeIDB)
    {
        if (biomeIDA == biomeIDB)
        {
            return true;
        }
        else
        {
            Biome biome = Biome.getBiome(biomeIDA);
            Biome biome1 = Biome.getBiome(biomeIDB);

            if (biome != null && biome1 != null)
            {
                if (biome != Biomes.MESA_ROCK && biome != Biomes.MESA_CLEAR_ROCK)
                {
                    return biome == biome1 || biome.getBiomeClass() == biome1.getBiomeClass();
                }
                else
                {
                    return biome1 == Biomes.MESA_ROCK || biome1 == Biomes.MESA_CLEAR_ROCK;
                }
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * returns true if the biomeId is one of the various ocean biomes.
     */
    protected static boolean isBiomeOceanic(int biomeID)
    {
        return net.minecraftforge.common.BiomeManager.oceanBiomes.contains(Biome.getBiome(biomeID));
    }

    /* ======================================== FORGE START =====================================*/
    protected long nextLong(long par1)
    {
        long j = (this.chunkSeed >> 24) % par1;

        if (j < 0)
        {
            j += par1;
        }

        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return j;
    }

    public static int getModdedBiomeSize(WorldType worldType, int original)
    {
        net.minecraftforge.event.terraingen.WorldTypeEvent.BiomeSize event = new net.minecraftforge.event.terraingen.WorldTypeEvent.BiomeSize(worldType, original);
        net.minecraftforge.common.MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.getNewSize();
    }
    /* ========================================= FORGE END ======================================*/

    /**
     * selects a random integer from a set of provided integers
     */
    protected int selectRandom(int... p_151619_1_)
    {
        return p_151619_1_[this.nextInt(p_151619_1_.length)];
    }

    /**
     * returns the most frequently occurring number of the set, or a random number from those provided
     */
    protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_)
    {
        if (p_151617_2_ == p_151617_3_ && p_151617_3_ == p_151617_4_)
        {
            return p_151617_2_;
        }
        else if (p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_3_)
        {
            return p_151617_1_;
        }
        else if (p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_4_)
        {
            return p_151617_1_;
        }
        else if (p_151617_1_ == p_151617_3_ && p_151617_1_ == p_151617_4_)
        {
            return p_151617_1_;
        }
        else if (p_151617_1_ == p_151617_2_ && p_151617_3_ != p_151617_4_)
        {
            return p_151617_1_;
        }
        else if (p_151617_1_ == p_151617_3_ && p_151617_2_ != p_151617_4_)
        {
            return p_151617_1_;
        }
        else if (p_151617_1_ == p_151617_4_ && p_151617_2_ != p_151617_3_)
        {
            return p_151617_1_;
        }
        else if (p_151617_2_ == p_151617_3_ && p_151617_1_ != p_151617_4_)
        {
            return p_151617_2_;
        }
        else if (p_151617_2_ == p_151617_4_ && p_151617_1_ != p_151617_3_)
        {
            return p_151617_2_;
        }
        else
        {
            return p_151617_3_ == p_151617_4_ && p_151617_1_ != p_151617_2_ ? p_151617_3_ : this.selectRandom(p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_);
        }
    }
}