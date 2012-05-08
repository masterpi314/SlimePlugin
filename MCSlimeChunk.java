import java.util.Random;

public class MCSlimeChunk {
    public static void main (String args[]) {
        long seed = Long.parseLong(args[0]);
        long xPosition = (long) Math.floor(Double.parseDouble(args[1]) / 16.0);
        long zPosition = (long) Math.floor(Double.parseDouble(args[2]) / 16.0);
        Random rnd = new Random(seed
            + (long) (xPosition * xPosition * 0x4c1906L)
            + (long) (xPosition * 0x5ac0dbL)
            + (long) (zPosition * zPosition) * 0x4307a7L
            + (long) (zPosition * 0x5f24f) ^ 0x3ad8025fL);
        System.exit(rnd.nextInt(10));
    }
}
