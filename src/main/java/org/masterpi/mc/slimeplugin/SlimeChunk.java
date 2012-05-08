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

public class SlimeChunk {

    public static void main (String args[]) {
        long seed = Long.parseLong(args[0]);
        double xPosition = Double.parseDouble(args[1]);
        double zPosition = Double.parseDouble(args[2]);
        System.exit(isSlimeChunk(seed,xPosition,zPosition)?0:1);
    }

    public static boolean isSlimeChunk(long seed,
            double xPosition, double zPosition) {

        long xChunk = (long) Math.floor(xPosition / 16.0);
        long zChunk = (long) Math.floor(zPosition / 16.0);

        Random rnd = new Random(seed
            + (long) (xChunk * xChunk * 0x4c1906L)
            + (long) (xChunk * 0x5ac0dbL)
            + (long) (zChunk * zChunk) * 0x4307a7L
            + (long) (zChunk * 0x5f24f) ^ 0x3ad8025fL);

        return rnd.nextInt(10) == 0;
    }
}
