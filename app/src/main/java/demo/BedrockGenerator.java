package demo;

import demo.extracted.AbstractRandom;
import demo.extracted.MathHelper;
import demo.extracted.RandomProvider;
import demo.extracted.Xoroshiro128PlusPlusRandom;
import demo.recreated.Identifier;

public class BedrockGenerator {
    Xoroshiro128PlusPlusRandom.RandomDeriver randomDeriver;
    BedrockType bedrockType;
    private final Xoroshiro128PlusPlusRandom reusableRandom = new Xoroshiro128PlusPlusRandom(0L, 0L);

    public BedrockGenerator(long seed, BedrockType bedrockType) {
        this.bedrockType = bedrockType;
        this.randomDeriver = (Xoroshiro128PlusPlusRandom.RandomDeriver) RandomProvider.XOROSHIRO
                .create(seed).createRandomDeriver()
                .createRandom(bedrockType.id.toString()).createRandomDeriver();
    }

    public boolean isBedrock(int x, int y, int z) {
        double probabilityValue = 0;

        if (bedrockType == BedrockType.BEDROCK_FLOOR) {
            if (y <= bedrockType.min) return true; // At or below -64 is always bedrock
            if (y > bedrockType.max) return false;  // Above -60 is never bedrock

            probabilityValue = MathHelper.lerpFromProgress(y, bedrockType.min, bedrockType.max, 1.0, 0.0);
        } else if (bedrockType == BedrockType.BEDROCK_ROOF) {
            if (y == bedrockType.min) return true;
            if (y < bedrockType.max) return false;

            probabilityValue = MathHelper.lerpFromProgress(y, bedrockType.max, bedrockType.min, 1.0, 0.0);
        }

        this.randomDeriver.reseedRandom(this.reusableRandom, x, y, z);
        return (double)this.reusableRandom.nextFloat() < probabilityValue;
    }

    public enum BedrockType {
        BEDROCK_FLOOR(new Identifier("bedrock_floor"), -64, -59),
        BEDROCK_ROOF(new Identifier("bedrock_roof"), 127, 123);

        public final Identifier id;
        public final int min;
        public final int max;

        BedrockType(Identifier id, int min, int max) {
            this.id = id;
            this.min = min;
            this.max = max;
        }
    }
}
