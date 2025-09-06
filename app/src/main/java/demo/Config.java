package demo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Config {
    long seed;
    @SerializedName("pattern-60")
    String[] pattern60;
    @SerializedName("pattern-61")
    String[] pattern61;
    @SerializedName("pattern-62")
    String[] pattern62;
    @SerializedName("pattern-63")
    String[] pattern63;
    List<Integer> usePatternLevels;
    int patternWidth;
    int searchRadiusX;
    int searchRadiusZ;
    Integer startX;
    Integer startZ;
    Integer threads;
}