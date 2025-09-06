package demo;

public class SeedProvider {
    private static long seed;

    public static long getSeed() {
        return seed;
    }

    public static void setSeed(long seed) {
        SeedProvider.seed = seed;
    }
}
