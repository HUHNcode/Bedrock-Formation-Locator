# Bedrock Finder Configuration (`config.json`)

This document explains the configuration options in the `config.json` file for the Bedrock Finder tool.

## General Settings

- `seed`: The Minecraft world seed to search in.
- `searchRadiusX`: The radius in the X direction to search from the `startX` coordinate.
- `searchRadiusZ`: The radius in the Z direction to search from the `startZ` coordinate.
- `startX`: The starting X coordinate for the search.
- `startZ`: The starting Z coordinate for the search.
- `threads`: The number of threads to use for the search.  
  **Warning:** Setting this value too high can cause crashes or freezing of your system / PC. You should be careful with values over 16.

## Pattern Configuration

The Bedrock Finder can search for multi-level patterns. You can define patterns for different Y-levels and then specify which levels to use in the search.

### Defining Patterns

- `pattern-60`, `pattern-61`, `pattern-62`, `pattern-63`: These fields define the patterns for the corresponding Y-levels (-60, -61, -62, -63).
- Each pattern is a 2D array of strings.
- `'1'` represents a bedrock block.
- `'0'` represents a non-bedrock block (air).
- `'?'` represents any block (wildcard).

**Example Pattern:**

```json
"pattern-60": [
  "000110",
  "001000",
  "100000",
  "010010",
  "011000",
  "010000"
]
```

This defines a 6x6 pattern for Y-level -60. The orientation of the pattern is important:

```
        NORTH
    +------------+
    |  "000110"  |
    |  "001000"  |
WEST|  "100000"  |OST
    |  "010010"  |
    |  "011000"  |
    |  "010000"  |
    +------------+
        SOUTH
```

- `patternWidth`: The width of the patterns (number of characters in each string). This must be consistent for all defined patterns.

### Selecting Patterns for Search

- `usePatternLevels`: This is an array of integers that specifies which Y-levels to use in the search. For example, `[60, 61]` will search for a combined pattern on levels -60 and -61.

## Search Logic

The tool searches for **connected patterns** across the specified Y-levels. The search logic is as follows:

1.  It identifies the **top-most layer** from the `usePatternLevels` array (the level with the smallest Y-value, e.g., -60 is above -61).
2.  It searches for the pattern of the top-most layer.
3.  When a match for the top-most pattern is found at a specific (X, Z) coordinate, it then immediately checks for the patterns of all other specified layers at the **same (X, Z) coordinate**.
4.  If and only if **all** patterns in `usePatternLevels` match at the same (X, Z) coordinate, it reports a successful find.

**Note on single-layer searches:** It is possible to search for a pattern on a single layer by only providing one level in `usePatternLevels`. However, be aware that with large search areas and small `patternWidth`, this can lead to a high number of false positives.
