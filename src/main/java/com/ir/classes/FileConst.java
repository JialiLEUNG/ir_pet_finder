package com.ir.classes;

public class FileConst {
    public static String[] fields = {
            "age","breed1", "breed2",
            "color1","color2","color3", "dewormed", "fee",
            "furLength", "gender", "description", "health",
            "maturitySize", "name", "photoAmt", "quantity",
            "rescuerId", "videoAmt", "vaccinated", "sterilized",
            "type", "state", "imageDir", "lastUpdated"
    };

    public static String[] labelFileName = {
            "BreedLabels.csv",
            "ColorLabels.csv",
            "StateLabels.csv"
    };
    public static String[] dataFile = {
            "test.csv",
            "train.csv"
    };
    public static int number_of_fields = fields.length;

    private int topK_relevant_doc;

    public FileConst() {
    }

    public FileConst(int topK_relevant_doc) {
        this.topK_relevant_doc = topK_relevant_doc;
    }

}