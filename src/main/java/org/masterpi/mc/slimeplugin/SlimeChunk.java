package org.masterpi.mc.slimeplugin;

/*
    This file is part of SlimePlugin

    SlimePlugin is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SlimePlugin is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SlimePlugin.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Random;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;

public class SlimeChunk {

    public static void main (String args[]) {
        long seed = Long.parseLong(args[0]);
        double xPosition = Double.parseDouble(args[1]);
        double zPosition = Double.parseDouble(args[2]);
        System.out.print("Nearest slime chunk: ");
        System.out.println(nearestSlimeChunk(seed,xPosition,zPosition));
        System.exit(isSlimeChunk(seed,xPosition,zPosition)?0:1);
    }

    public static boolean isSlimeChunk(long seed,
            double xPosition, double zPosition) {

        long xChunk = toChunk(xPosition);
        long zChunk = toChunk(zPosition);

        return isSlimeChunk(seed, new ChunkCoords(xChunk,zChunk));
    }

    public static boolean isSlimeChunk(long seed,
            ChunkCoords c) {

        long xChunk = c.getX();
        long zChunk = c.getZ();

        Random rnd = new Random(seed
            + (long) (xChunk * xChunk * 0x4c1906L)
            + (long) (xChunk * 0x5ac0dbL)
            + (long) (zChunk * zChunk) * 0x4307a7L
            + (long) (zChunk * 0x5f24f) ^ 0x3ad8025fL);

        return rnd.nextInt(10) == 0;
    }

    public static class ChunkCoords {
        private final long x,z;
        private ChunkCoords(long x, long z) {
            this.x=x;
            this.z=z;
        }
        public long getX() { return x; }
        public long getZ() { return z; }
        public boolean equals(Object other) {
            if (other instanceof ChunkCoords) {
                ChunkCoords othr = (ChunkCoords)other;
                return this.x==othr.x && this.z==othr.z;
            }
            return false;
        }
        public int hashCode() {
            return (int)(x*256*256+z);
        }
        public String toString() {
            return "("+x+", "+z+")";
        }
    }

    private static long toChunk(double position) {
        return (long) Math.floor(position / 16.0);
    }

    public static ChunkCoords nearestSlimeChunk(long seed, double xPosition,
            double zPosition) {

        long xChunk = toChunk(xPosition);
        long zChunk = toChunk(zPosition);

        PriorityQueue<ChunkCoords> queue = new PriorityQueue<ChunkCoords>(
                100, new ChunkDistanceComparator(xPosition,zPosition));
        HashSet<ChunkCoords> seen = new HashSet<ChunkCoords>(100);

        queue.add(new ChunkCoords(xChunk,zChunk));
        while(!queue.isEmpty()) {
            ChunkCoords curr = queue.poll();
            if (isSlimeChunk(seed,curr)) return curr;
            for(long x=curr.getX()-1;x<=curr.getX()+1;x++) {
                for(long z=curr.getZ()-1;z<=curr.getZ()+1;z++) {
                    ChunkCoords neighbor = new ChunkCoords(x,z);
                    if (seen.contains(neighbor)) continue;
                    seen.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return null;

    }

    private static class ChunkDistanceComparator
            implements Comparator<ChunkCoords>{

        private double xPosition, zPosition;

        private ChunkDistanceComparator(double xPosition, double zPosition) {
            this.xPosition = xPosition;
            this.zPosition = zPosition;
        }

        private static double distSqr(double x1,double y1,double x2,double y2) {
            return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
        }

        private static double closestChunkPos(double pos, long chunkCoord) {
            double chunk1 = chunkCoord*16;
            double chunk2 = (chunkCoord+1)*16;
            double chunkMin = Math.min(chunk1,chunk2);
            double chunkMax = Math.max(chunk1,chunk2);
            if ( pos <= chunkMin ) return chunkMin;
            if ( pos >= chunkMax ) return chunkMax;
            return pos;
        }

        public int compare(ChunkCoords c1, ChunkCoords c2) {
            double c1x = closestChunkPos(xPosition,c1.getX());
            double c1z = closestChunkPos(zPosition,c1.getZ());
            double c2x = closestChunkPos(xPosition,c2.getX());
            double c2z = closestChunkPos(zPosition,c2.getZ());
            double c1d = distSqr(xPosition,zPosition,c1x,c1z);
            double c2d = distSqr(xPosition,zPosition,c2x,c1z);
            return Double.compare(c1d,c2d);
        }
    }
}
