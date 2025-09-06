package demo;

import com.google.gson.Gson;
import demo.BedrockGenerator.BedrockType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {

    private enum PatternType {
        BEDROCK,
        NO_BEDROCK,
        ANY
    }

    private static int patternWidth;
    private static int patternHeight;

    public static void main(String[] args) {
        Gson gson = new Gson();
        Config config = null;

        try (FileReader reader = new FileReader("config.json")) {
            config = gson.fromJson(reader, Config.class);
        } catch (FileNotFoundException e) {
            System.err.println("Error: config.json not found in the execution directory.");
            System.err.println("Please create a config.json file based on the example.");
            return;
        } catch (IOException e) {
            System.err.println("Error reading config.json: " + e.getMessage());
            return;
        }

        if (config == null) {
            System.err.println("Error: Failed to parse config.json.");
            return;
        }

        long seed = config.seed;
        patternWidth = config.patternWidth;
        int searchRadiusX = config.searchRadiusX;
        int searchRadiusZ = config.searchRadiusZ;
        int startX = (config.startX != null) ? config.startX : 0;
        int startZ = (config.startZ != null) ? config.startZ : 0;
        int numThreads = (config.threads != null && config.threads > 0) ? config.threads : 1;

        Map<Integer, PatternType[]> levelPatterns = new HashMap<>();
        if (config.usePatternLevels != null) {
            for (int level : config.usePatternLevels) {
                String[] patternRows = null;
                if (level == 60) patternRows = config.pattern60;
                else if (level == 61) patternRows = config.pattern61;
                else if (level == 62) patternRows = config.pattern62;
                else if (level == 63) patternRows = config.pattern63;

                if (patternRows != null && patternRows.length > 0 && patternWidth > 0) {
                    String patternStr = String.join("", patternRows);
                    if (patternStr.length() % patternWidth != 0) {
                        System.err.println("Error: Pattern string length is not divisible by pattern width for level " + level);
                        continue;
                    }
                    patternHeight = patternStr.length() / patternWidth;
                    PatternType[] pattern = new PatternType[patternStr.length()];
                    for (int i = 0; i < patternStr.length(); i++) {
                        char c = patternStr.charAt(i);
                        if (c == '1') pattern[i] = PatternType.BEDROCK;
                        else if (c == '0') pattern[i] = PatternType.NO_BEDROCK;
                        else pattern[i] = PatternType.ANY;
                    }
                    levelPatterns.put(level, pattern);
                }
            }
        }

        if (levelPatterns.isEmpty()) {
            System.err.println("Error: No valid patterns found in config.json.");
            return;
        }

        System.out.println("Searching for patterns with " + numThreads + " threads...");

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        final long finalSeed = seed;

        long searchMinX = (long)startX - searchRadiusX;
        long searchMaxX = (long)startX + searchRadiusX;
        long searchMinZ = (long)startZ - searchRadiusZ;
        long searchMaxZ = (long)startZ + searchRadiusZ;

        long totalWidth = searchMaxX - searchMinX;
        long stripWidth = totalWidth / numThreads;

        final int topLevel = Collections.min(levelPatterns.keySet());

        for (int i = 0; i < numThreads; i++) {
            final long xMin = searchMinX + (i * stripWidth);
            final long xMax = (i == numThreads - 1) ? searchMaxX : xMin + stripWidth - 1;

            executor.submit(() -> {
                BedrockGenerator bedrockGenerator = new BedrockGenerator(finalSeed, BedrockType.BEDROCK_FLOOR);
                for (long x = xMin; x <= xMax; x++) {
                    for (long z = searchMinZ; z <= searchMaxZ; z++) {
                        if (checkPattern((int)x, (int)z, topLevel, levelPatterns.get(topLevel), bedrockGenerator)) {
                            boolean allMatch = true;
                            for (Map.Entry<Integer, PatternType[]> entry : levelPatterns.entrySet()) {
                                if (entry.getKey() == topLevel) continue;
                                if (!checkPattern((int)x, (int)z, entry.getKey(), entry.getValue(), bedrockGenerator)) {
                                    allMatch = false;
                                    break;
                                }
                            }
                            if (allMatch) {
                                System.out.println("Found matching patterns at (X,Z): (" + x + "," + z + ") for levels " + levelPatterns.keySet());
                            }
                        }
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Search was interrupted.");
        }

        System.out.println("Search finished.");
    }

    private static boolean checkPattern(int x, int z, int y, PatternType[] pattern, BedrockGenerator bedrockGenerator) {
        for (int pz = 0; pz < patternHeight; pz++) {
            for (int px = 0; px < patternWidth; px++) {
                PatternType bedrockExpected = pattern[pz * patternWidth + px];
                if (bedrockExpected != PatternType.ANY) {
                    boolean bedrockActual = bedrockGenerator.isBedrock(x + px, -y, z + pz);
                    if ((bedrockExpected == PatternType.BEDROCK) != bedrockActual) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
