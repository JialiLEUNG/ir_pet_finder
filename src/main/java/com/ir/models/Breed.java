package com.ir.models;

import com.opencsv.bean.CsvBindByName;

public class Breed {
    @CsvBindByName(column = "BreedID")
    private String BreedID;

    @CsvBindByName(column = "Type")
    private String Type;

    @CsvBindByName(column = "BreedName")
    private String BreedName;

    public Breed() {
    }

    public String getType() {
        if (Type == null){
            Type = "";
        }
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getBreedID() {
        if (BreedID == null){
            BreedID = "";
        }
        return BreedID;
    }

    public void setBreedID(String breedID) {
        BreedID = breedID;
    }

    public String getBreedName() {
        if (BreedName == null){
            BreedName = "";
        }
        return BreedName;
    }

    public void setBreedName(String breedName) {
        BreedName = breedName;
    }

    public String getContent() {
        return Type + " " +
                BreedID + " " +
                BreedName + " ";
    }

    @Override
    public String toString() {
        return "Breed{" +
                "Type='" + Type + '\'' +
                ", BreedID='" + BreedID + '\'' +
                ", BreedName='" + BreedName + '\'' +
                '}';
    }
}
