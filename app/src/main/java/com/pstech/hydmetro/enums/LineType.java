package com.pstech.hydmetro.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pagrawal on 26-11-2017.
 */

public enum LineType {

    GREEN(0),
    RED(1),
    YELLOW(2),
    BLUE(3),
    INVALID(4);

    private int lineId;
    private static final Map<String, LineType> lineMap = new HashMap<>();
    static {
        for (LineType lineType : LineType.values()) {
            lineMap.put(lineType.toString().toLowerCase(), lineType);
        }
    }

    LineType(int id) {
        this.lineId = id;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static LineType getLineType(String name) {
        if (name == null)
            return INVALID;
        LineType result = lineMap.get(name.toLowerCase());
        return result == null ? INVALID : result;
    }

    public int getLineId() {
        return lineId;
    }
}
