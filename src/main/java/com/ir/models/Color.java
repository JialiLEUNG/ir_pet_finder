package com.ir.models;

import com.opencsv.bean.CsvBindByName;

public class Color {
    @CsvBindByName(column = "ColorID")
    private String ColorID;

    @CsvBindByName(column = "ColorName")
    private String ColorName;

    public Color() {
    }

    public String getColorID() {
        if (ColorID == null){
            ColorID = "";
        }
        return ColorID;
    }

    public void setColorID(String colorID) {
        ColorID = colorID;
    }

    public String getColorName() {
        if (ColorName == null){
            ColorName = "";
        }
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }

    @Override
    public String toString() {
        return "Color{" +
                "ColorID='" + ColorID + '\'' +
                ", ColorName='" + ColorName + '\'' +
                '}';
    }
}
