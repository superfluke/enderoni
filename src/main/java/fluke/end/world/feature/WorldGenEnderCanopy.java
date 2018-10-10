package fluke.end.world.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fluke.end.block.ModBlocks;
import fluke.end.util.MathUtils;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

//TODO floating over lowering terrain/ roots
public class WorldGenEnderCanopy extends WorldGenAbstractTree  
{
	protected static final IBlockState LOG = ModBlocks.endLog.getDefaultState();
	protected static final IBlockState LEAF = ModBlocks.endLeaves.getDefaultState();
	private static final IBlockState END_GRASS = ModBlocks.endGrass.getDefaultState();

	public WorldGenEnderCanopy(boolean notify) 
	{
		super(notify);

	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) 
	{
		IBlockState inState = world.getBlockState(pos);
		if(inState != END_GRASS && inState != Blocks.END_STONE.getDefaultState())
			pos = pos.down();
		
		int trunkHeight = 16 + rand.nextInt(4);
		if(!isValidGenLocation(world, pos, trunkHeight))
			return false;
		
		List<BranchInfo> branchEndPos;
		buildTrunk(world, rand, pos, trunkHeight);
		branchEndPos = buildBranches(world, rand, pos, trunkHeight);
		buildCanopy(world, rand, pos, branchEndPos);
		return true;
	}
	
	public boolean isValidGenLocation(World world, BlockPos pos, int trunkHeight)
	{
		if(pos.getY() < 3 || pos.getY() + trunkHeight + 22 > 255)
			return false;

		for(BlockPos trunkBaseBlock : BlockPos.getAllInBoxMutable(pos.add(-5, 0, -5), pos.add(5, 2, 5)))
		{
			IBlockState state = world.getBlockState(trunkBaseBlock);
			if(state != END_GRASS && state != Blocks.END_STONE.getDefaultState() && !state.getBlock().isReplaceable(world, pos) && !isReplaceable(world, trunkBaseBlock, state))
			{
				//System.out.println(state.getBlock().getLocalizedName());
				return false;
			}
		}
		
		for(BlockPos trunkCoreBlock : BlockPos.getAllInBoxMutable(pos.add(-1, 3, -1), pos.add(1, trunkHeight-1, 3)))
		{
			if(!isReplaceable(world, trunkCoreBlock))
				return false;
		}
		
		for(BlockPos canopyBlock : BlockPos.getAllInBoxMutable(pos.add(-23, trunkHeight+7, -23), pos.add(23, trunkHeight+7, 23)))
		{
			if(!isReplaceable(world, canopyBlock))
				return false;
		}
		
		return true;
		
	}
	
	@Override
	public boolean isReplaceable(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return isReplaceable(world, pos, state);
    }
	
	private boolean isReplaceable(World world, BlockPos pos, IBlockState state)
	{
        return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos) || state.getBlock().isWood(world, pos) || canGrowInto(state.getBlock());
	}
	
	private void buildCanopy(World world, Random rand, BlockPos pos, List<BranchInfo> branchEnds) 
	{
		List<BlockPos> possibleVineSpots = new ArrayList<BlockPos>();
		
		for(BranchInfo branch : branchEnds)
		{
			double xAngleTranslation = Math.cos(Math.toRadians(branch.rotationAngle));
			double zAngleTranslation = Math.sin(Math.toRadians(branch.rotationAngle));
			int xAnglizer = (int)Math.round(1*xAngleTranslation);
			int zAnglizer = (int)Math.round(1*zAngleTranslation);
			for(int y=0; y<=2; y++)
			{
				int canopyRadius = 8;
				if(y == 0)
					canopyRadius -= 3;
				else if(y == 2)
					canopyRadius -= 2;
				
				int maxDist = canopyRadius*canopyRadius;
				int lesserMaxDist = (canopyRadius-1)*(canopyRadius);
				//int greaterMaxDist = (canopyRadius+1)*(canopyRadius);
				
				for(int x=-canopyRadius; x<=canopyRadius; x++)
				{
					for(int z=-canopyRadius; z<=canopyRadius; z++)
					{
						
						double xDist = x*x;
						double zDist = z*z; 
						
						double ratio;
						int num;
						int denom;
						if(Math.abs(x) > Math.abs(z))
						{
							ratio = (z*zAnglizer)/((x*xAnglizer)+0.001);
						}
						else
						{
							ratio = (x*xAnglizer)/((z*zAnglizer)+0.001);
						}
						
						ratio = 1-(ratio+1.0)/2.0;
						double squishFactor = MathHelper.clampedLerp(1.0, 1.55, ratio);
						xDist *= squishFactor;
						zDist *= squishFactor;
						
						//this actually makes an interesting shape
	//					double ratio = x/(z+0.001);
	//					ratio = ratio<1? 1: ratio;
	//					ratio = ratio>3? 3: ratio;
	//					zDist *= ratio;
	//					xDist *= ratio;
						
						//roughs up the edges of the canopy a bit
						int distortedMaxDistance;
						if(rand.nextBoolean())
							distortedMaxDistance = lesserMaxDist;
						else
							distortedMaxDistance = maxDist;
						
						if(xDist+zDist < distortedMaxDistance)
						{
							placeLeafAt(world, branch.endPoint.add(x, y, z));
							
							if(y < 2 && xDist+zDist > lesserMaxDist && rand.nextInt(4) == 0)
								possibleVineSpots.add(branch.endPoint.add(x, y, z));
						}
					}
				}
			}
		}
		
		for(BlockPos vinePos : possibleVineSpots)
		{
			placeVine(world, rand, vinePos);
		}
		
	}

	private void placeVine(World world, Random rand, BlockPos pos) 
	{
        //int length = rand.nextInt(rand.nextInt(12)+5);
        int length = rand.nextInt(22);
        int vineChance = rand.nextInt(1);
        
        if (vineChance == 0) 
        {
        	if(world.isAirBlock(pos.west()))
        		addHangingVine(world, pos.west(), BlockVine.EAST, length);
        	if(world.isAirBlock(pos.east()))
        		addHangingVine(world, pos.east(), BlockVine.WEST, length);
        	if(world.isAirBlock(pos.north()))
        		addHangingVine(world, pos.north(), BlockVine.SOUTH, length);
        	if(world.isAirBlock(pos.south()))
        		addHangingVine(world, pos.south(), BlockVine.NORTH, length);
        	
        	
		}
		
	}
	
	private void addHangingVine(World world, BlockPos pos, PropertyBool prop, int length)
    {
    	this.setBlockAndNotifyAdequately(world, pos, ModBlocks.endVine.getDefaultState().withProperty(prop, Boolean.valueOf(true)));

        for (BlockPos blockpos = pos.down(); world.isAirBlock(blockpos) && length > 0; length--)
        {
        	this.setBlockAndNotifyAdequately(world, blockpos, ModBlocks.endVine.getDefaultState().withProperty(prop, Boolean.valueOf(true)));
            blockpos = blockpos.down();
        }
    }

	private void buildTrunk(World world, Random rand, BlockPos center, int height)
	{
		//int thiccness = 11;
		int trunkCore = 5;
		int trunkDecoWidth = 7;
		for(int x=-trunkCore; x<=trunkCore; x++)
		{
			for(int z=-trunkCore; z<=trunkCore; z++)
			{
				int colHeight;
				
				//core will always be a min thickness of 3x3
				if(Math.abs(x)<=1 && Math.abs(z)<=1)
				{
					colHeight = height;
				}
				//sort of jagged manhattan distance to create trunk taper
				else if(Math.abs(x)<=Math.abs(z))
				{
					colHeight = 18 - Math.abs(x) - Math.abs(z)*3 - rand.nextInt(2);
				}
				else
				{
					colHeight = 18 - Math.abs(x)*3 - Math.abs(z) - rand.nextInt(2);
				}
				
				for(int y=0; y<colHeight; y++)
				{
					placeLogAt(world, center.add(x, y, z));
				}
			}
		}
	}

	private List<BranchInfo> buildBranches(World world, Random rand, BlockPos center, int trunkHeight)
	{
		List<BranchInfo> branchEndPos = new ArrayList<BranchInfo>();
		double xAngleTranslation;
		double zAngleTranslation;
		center = center.add(0, trunkHeight-2, 0);
		for(int n=0; n<7; n++)
		{
			//maybe we just don't bother making a branch this time
			if(rand.nextInt(21) == 0)
				continue;
			
			//only make branch 7 rarely
			if(n == 6 && rand.nextInt(8) != 0)
				continue;
			
			int branchLength;
			int branchHeight;
			int branchAngle;
			
			//first 4 branches further out and in the four diagonal directions
			if(n < 4)
			{
				branchLength = 14 + rand.nextInt(8);
				branchHeight = 8 + rand.nextInt(7);
				branchAngle = MathUtils.randIntBetween((45+90*n)-10, (45+90*n)+10, rand);
			}
			//next 2 branches closer and higher with more freedom of angle
			else if(n < 6)
			{
				branchLength = 9 + rand.nextInt(7);
				branchHeight = 15 + rand.nextInt(3);
				branchAngle = MathUtils.randIntBetween((90+180*(n-4))-35, (90+180*(n-4))+35, rand);
			}
			//last, less common, branch shorter and lower with no angle restriction 
			else
			{
				branchLength = 5 + rand.nextInt(5);
				branchHeight = 5 + rand.nextInt(5);
				branchAngle = rand.nextInt(360);
			}
			
			xAngleTranslation = Math.cos(Math.toRadians(branchAngle));
			zAngleTranslation = Math.sin(Math.toRadians(branchAngle));
			int xOffset = (int)Math.round(1*xAngleTranslation);
			int zOffset = (int)Math.round(1*zAngleTranslation);
			
			//current CurvedBresehnam only works in 2d, ignore z axis
			BlockPos branchStart = center.add(xOffset, 0, zOffset);
			BlockPos branchCurve = center.add(branchLength/3, branchHeight, 0);
			BlockPos branchEnd = center.add(branchLength+xOffset, branchHeight, 0);
						
			//add the actual branch end position to a list so we can add leaves to it later
			int rotEndPosX = (int)Math.round((branchLength+xOffset)* xAngleTranslation);
			int rotEndPosZ = (int)Math.round((branchLength+zOffset)* zAngleTranslation);
			BlockPos rotatedBranchEnd = branchStart.add(rotEndPosX, branchHeight, rotEndPosZ);
			branchEndPos.add(new BranchInfo(rotatedBranchEnd, branchAngle));
			
			BlockPos[] branchArray = MathUtils.getQuadBezierArray(branchStart, branchCurve, branchEnd);
			for(BlockPos pos : branchArray)
			{
				
				int pxXoffset = pos.getX() - branchStart.getX();
				int pxYoffset = pos.getY() - branchStart.getY();
				int pxZoffset = pos.getZ() - branchStart.getZ();
				int pxDistance = pxXoffset; 
				
				//get x, z positions for branches at specified angle
				int angledX = (int)Math.round(pxDistance * xAngleTranslation);
				int angledZ = (int)Math.round(pxDistance * zAngleTranslation);
				
				placeLogAt(world, branchStart.add(angledX, pxYoffset, angledZ));
				placeLogAt(world, branchStart.add(angledX+xOffset, pxYoffset, angledZ));
				placeLogAt(world, branchStart.add(angledX, pxYoffset, angledZ+zOffset));
				if(pxDistance <= 5)
					placeLogAt(world, branchStart.add(angledX-xOffset, pxYoffset, angledZ-zOffset));
				
//				if(pxDistance == branchLength - 3)// && rand.nextBoolean()) 
//				{//not working
//					BlockPos tipBranchStart = branchStart.add(angledX*xOffset, pxYoffset, angledZ*zOffset);
//					BlockPos tipBranchEnd = tipBranchStart.add(0, 8, 0);
//					BlockPos tipBranchCurve = tipBranchStart.add(0, 4, 0);
//					BlockPos[] tipBranchArray = MathUtils.getQuadBezierArray(tipBranchStart, tipBranchCurve, tipBranchEnd);
//					for(BlockPos pos2 : tipBranchArray)
//					{
//						int pxXoffset2 = pos2.getX() - tipBranchStart.getX();
//						int pxYoffset2 = pos2.getY() - tipBranchStart.getY();
//						int pxZoffset2 = pos2.getZ() - tipBranchStart.getZ();
//						BlockPos tipLogPos = new BlockPos(pos2.getX(), tipBranchStart.getY(), pos2.getY());
//						placeLogAt(world, pos2);
//					}
//				}
			}
		}
		
		return branchEndPos;
	}
	
	private void placeLogAt(World worldIn, BlockPos pos)
    {
        this.setBlockAndNotifyAdequately(worldIn, pos, LOG);
    }
	
	private void placeLeafAt(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);

        if (state.getBlock().isAir(state, worldIn, pos) || state.getBlock().isLeaves(state, worldIn, pos) || state == ModBlocks.endVine.getDefaultState())
        {
            this.setBlockAndNotifyAdequately(worldIn, pos, LEAF);
        }
    }
	
	//Draws a line from start to end with midsection pulled towards curvePos
	protected void drawCurvedBresehnam(World world, BlockPos start, BlockPos end, BlockPos curvePos, IBlockState state) 
	{
		for (BlockPos pixel : MathUtils.getQuadBezierArray(start, curvePos, end)) 
		{
			this.setBlockAndNotifyAdequately(world, pixel, state);
		}
	}
	
	private class BranchInfo
	{
		BlockPos endPoint;
		int rotationAngle;
		
		public BranchInfo(BlockPos endPoint, int rotationAngle)
		{
			this.endPoint = endPoint;
			this.rotationAngle = rotationAngle;
		}
	}

}
