package fluke.stygian.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.minecraft.util.math.BlockPos;

public class MathUtils 
{
		//100% black magic
		//draws a line between start and end point, curving the line based on curvePoint location
		//returns array of block positions of points on the final, curved line
		//Only creates line along x/y axis, ignoring input z cords
		public static BlockPos[] getQuadBezierArray(BlockPos start, BlockPos curvePoint, BlockPos end)
		{  		
			ArrayList<BlockPos> linePoints = new ArrayList<BlockPos>();
			int x0 = start.getX();
			int y0 = start.getY();
			int x1 = curvePoint.getX();
			int y1 = curvePoint.getY();
			int x2 = end.getX();
			int y2 = end.getY();
			int z = start.getZ();
			int sx = x2-x1, sy = y2-y1;
			long xx = x0-x1, yy = y0-y1, xy;         /* relative values for checks */
			double dx, dy, err, ed, cur = xx*sy-yy*sx;                /* curvature */
		
			assert(xx*sx >= 0 && yy*sy >= 0);  /* sign of gradient must not change */
		
			if (sx*(long)sx+sy*(long)sy > xx*xx+yy*yy) 
			{ /* begin with longer part */ 
				x2 = x0; x0 = sx+x1; y2 = y0; y0 = sy+y1; cur = -cur; /* swap P0 P2 */
			}  
			if (cur != 0)
		    {                                                  /* no straight line */
				xx += sx; xx *= sx = x0 < x2 ? 1 : -1;          /* x step direction */
				yy += sy; yy *= sy = y0 < y2 ? 1 : -1;          /* y step direction */
				xy = 2*xx*yy; xx *= xx; yy *= yy;         /* differences 2nd degree */
				if (cur*sx*sy < 0) 
				{                          /* negated curvature? */
					xx = -xx; yy = -yy; xy = -xy; cur = -cur;
				}
				dx = 4.0*sy*(x1-x0)*cur+xx-xy;            /* differences 1st degree */
				dy = 4.0*sx*(y0-y1)*cur+yy-xy;
				xx += xx; yy += yy; err = dx+dy+xy;               /* error 1st step */
				do 
				{                              
					cur = Math.min(dx+xy,-xy-dy);
					ed = Math.max(dx+xy,-xy-dy);           /* approximate error distance */
					ed = 255/(ed+2*ed*cur*cur/(4.*ed*ed+cur*cur)); 
					linePoints.add(new BlockPos(x0, y0, z));          /* plot curve */
					if (x0 == x2 && y0 == y2) 
					{
						return linePoints.toArray(new BlockPos[0]);/* last pixel -> curve finished */
					}
					x1 = x0; cur = dx-err; y1 = 2*err+dy < 0? 1:0;
					if (2*err+dx > 0) 
					{                                    /* x step */
						if (err-dy < ed) 
							linePoints.add(new BlockPos(x0, y0+sy, z));
						x0 += sx; dx -= xy; err += dy += yy;
					}
					if (y1 != 0) 
					{                                              /* y step */
						if (cur < ed) 
							linePoints.add(new BlockPos(x1+sx, y0, z));
						y0 += sy; dy -= xy; err += dx += xx; 
					}
				} while (dy < dx);              /* gradient negates -> close curves */
		    }    
			/* plot remaining needle to end */
			Collections.addAll(linePoints, getBresehnamArrays(start.add(x0, y0, 0), start.add(x2, y2, 0))); 
			return linePoints.toArray(new BlockPos[0]);
		}
		
		//Get an array of values that represent a line from point A to point B
		public static BlockPos[] getBresehnamArrays(BlockPos src, BlockPos dest) 
		{
			return getBresehnamArrays(src.getX(), src.getY(), src.getZ(), dest.getX(), dest.getY(), dest.getZ());
		}
		
		//Get an array of values that represent a line from point A to point B
		public static BlockPos[] getBresehnamArrays(int x1, int y1, int z1, int x2, int y2, int z2) 
		{
			int i, dx, dy, dz, absDx, absDy, absDz, x_inc, y_inc, z_inc, err_1, err_2, doubleAbsDx, doubleAbsDy, doubleAbsDz;

			BlockPos pixel = new BlockPos(x1, y1, z1);
			BlockPos lineArray[];

			dx = x2 - x1;
			dy = y2 - y1;
			dz = z2 - z1;
			x_inc = (dx < 0) ? -1 : 1;
			absDx = Math.abs(dx);
			y_inc = (dy < 0) ? -1 : 1;
			absDy = Math.abs(dy);
			z_inc = (dz < 0) ? -1 : 1;
			absDz = Math.abs(dz);
			doubleAbsDx = absDx << 1;
			doubleAbsDy = absDy << 1;
			doubleAbsDz = absDz << 1;

			if ((absDx >= absDy) && (absDx >= absDz)) {
				err_1 = doubleAbsDy - absDx;
				err_2 = doubleAbsDz - absDx;
				lineArray = new BlockPos[absDx + 1];
				for (i = 0; i < absDx; i++) {
					lineArray[i] = pixel;
					if (err_1 > 0) {
						pixel = pixel.up(y_inc);
						err_1 -= doubleAbsDx;
					}
					if (err_2 > 0) {
						pixel = pixel.south(z_inc);
						err_2 -= doubleAbsDx;
					}
					err_1 += doubleAbsDy;
					err_2 += doubleAbsDz;
					pixel = pixel.east(x_inc);
				}
			} else if ((absDy >= absDx) && (absDy >= absDz)) {
				err_1 = doubleAbsDx - absDy;
				err_2 = doubleAbsDz - absDy;
				lineArray = new BlockPos[absDy + 1];
				for (i = 0; i < absDy; i++) {
					lineArray[i] = pixel;
					if (err_1 > 0) {
						pixel = pixel.east(x_inc);
						err_1 -= doubleAbsDy;
					}
					if (err_2 > 0) {
						pixel = pixel.south(z_inc);
						err_2 -= doubleAbsDy;
					}
					err_1 += doubleAbsDx;
					err_2 += doubleAbsDz;
					pixel = pixel.up(y_inc);
				}
			} else {
				err_1 = doubleAbsDy - absDz;
				err_2 = doubleAbsDx - absDz;
				lineArray = new BlockPos[absDz + 1];
				for (i = 0; i < absDz; i++) {
					lineArray[i] = pixel;
					if (err_1 > 0) {
						pixel = pixel.up(y_inc);
						err_1 -= doubleAbsDz;
					}
					if (err_2 > 0) {
						pixel = pixel.east(x_inc);
						err_2 -= doubleAbsDz;
					}
					err_1 += doubleAbsDy;
					err_2 += doubleAbsDx;
					pixel = pixel.south(z_inc);
				}
			}
			lineArray[lineArray.length - 1] = pixel;

			return lineArray;
		}
		
		//rotates a blockpos by x degrees keeping same y level
		public static BlockPos getDirectionalPoint(BlockPos startPoint, int degreesToRotate, int distanceFromCenter)
		{		
			double stupidRadians = Math.toRadians(degreesToRotate);
			int xDist = (int) Math.round(Math.cos(stupidRadians) * distanceFromCenter);
			int zDist = (int) Math.round(Math.sin(stupidRadians) * distanceFromCenter);
			zDist *= -1; //invert z value to match minecraft's -Z = north
			BlockPos endPoint = startPoint.add(xDist, 0, zDist);
			return endPoint;
		}
		
		//returns an int between min and max, inclusive
		public static int randIntBetween(int min, int max, Random rand)
		{
			if(max+1-min <= 0)
				return 0;
			else
				return rand.nextInt(max+1-min)+min;
		}
}
