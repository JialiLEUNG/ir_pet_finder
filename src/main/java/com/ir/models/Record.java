package com.ir.models;

import com.ir.entities.GenerateRandomDate;
import com.opencsv.bean.CsvBindByName;
import com.ir.entities.GenerateRandomDate;

import java.util.Date;
import java.util.Random;

public class Record {
    private String id;

    @CsvBindByName(column = "Type")
    private String Type;

    @CsvBindByName(column = "Name")
    private String Name;

    @CsvBindByName(column = "Age")
    private double Age;

    @CsvBindByName(column = "Breed1")
    private String Breed1;

    @CsvBindByName(column = "Breed2")
    private String Breed2;

    @CsvBindByName(column = "Gender")
    private String Gender;

    @CsvBindByName(column = "Color1")
    private String Color1;

    @CsvBindByName(column = "Color2")
    private String Color2;

    @CsvBindByName(column = "Color3")
    private String Color3;

    @CsvBindByName(column = "MaturitySize")
    private String MaturitySize;

    @CsvBindByName(column = "FurLength")
    private String FurLength;

    @CsvBindByName(column = "Vaccinated")
    private String Vaccinated;

    @CsvBindByName(column = "Dewormed")
    private String Dewormed;

    @CsvBindByName(column = "Sterilized")
    private String Sterilized;

    @CsvBindByName(column = "Health")
    private String Health;

    @CsvBindByName(column = "Quantity")
    private String Quantity;

    @CsvBindByName(column = "Fee")
    private String Fee;

    @CsvBindByName(column = "State")
    private String State;

    @CsvBindByName(column = "RescuerID")
    private String RescuerID;

    @CsvBindByName(column = "VideoAmt")
    private String VideoAmt;

    @CsvBindByName(column = "Description")
    private String Description;

    @CsvBindByName(column = "PetID")
    private String PetID;

    @CsvBindByName(column = "PhotoAmt")
    private String PhotoAmt;

    private String ImageDir;

    //generate random values from 0-365 from today
    private Date lastUpdated = new GenerateRandomDate().generateDate();


    public Record() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        if (Name == null){
            Name = "";
        }
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getAge() {
        return Age;
    }

    public void setAge(double age) {
        Age = age;
    }

    public String getBreed1() {
        if (Breed1 == null){
            Breed1 = "";
        }
        return Breed1;
    }

    public void setBreed1(String breed1) {
        Breed1 = breed1;
    }

    public String getBreed2() {
        if (Breed2 == null){
            Breed2 = "";
        }
        return Breed2;
    }

    public void setBreed2(String breed2) {
        Breed2 = breed2;
    }

    public String getGender() {
        if (Gender == null){
            Gender = "";
        }
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getColor1() {
        if (Color1 == null){
            Color1 = "";
        }
        return Color1;
    }

    public void setColor1(String color1) {
        Color1 = color1;
    }

    public String getColor2() {
        if (Color2 == null){
            Color2 = "";
        }
        return Color2;
    }

    public void setColor2(String color2) {
        Color2 = color2;
    }

    public String getColor3() {
        if (Color3 == null){
            Color3 = "";
        }
        return Color3;
    }

    public void setColor3(String color3) {
        Color3 = color3;
    }

    public String getMaturitySize() {
        if (MaturitySize == null){
            MaturitySize = "";
        }
        return MaturitySize;
    }

    public void setMaturitySize(String maturitySize) {
        MaturitySize = maturitySize;
    }

    public String getFurLength() {
        if (FurLength == null){
            FurLength = "";
        }
        return FurLength;
    }

    public void setFurLength(String furLength) {
        FurLength = furLength;
    }

    public String getVaccinated() {
        if (Vaccinated == null){
            Vaccinated = "";
        }
        return Vaccinated;
    }

    public void setVaccinated(String vaccinated) {
        Vaccinated = vaccinated;
    }

    public String getDewormed() {
        if (Dewormed == null){
            Dewormed = "";
        }
        return Dewormed;
    }

    public void setDewormed(String dewormed) {
        Dewormed = dewormed;
    }

    public String getSterilized() {
        if (Sterilized == null){
            Sterilized = "";
        }
        return Sterilized;
    }

    public void setSterilized(String sterilized) {
        Sterilized = sterilized;
    }

    public String getHealth() {
        if (Health == null){
            Health = "";
        }
        return Health;
    }

    public void setHealth(String health) {
        Health = health;
    }

    public String getQuantity() {
        if (Quantity == null){
            Quantity = "";
        }
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getFee() {
        if (Fee == null){
            Fee = "";
        }
        return Fee;
    }

    public void setFee(String fee) {
        Fee = fee;
    }

    public String getState() {
        if (State == null){
            State = "";
        }
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getRescuerID() {
        if (RescuerID == null){
            RescuerID = "";
        }
        return RescuerID;
    }

    public void setRescuerID(String rescuerID) {
        RescuerID = rescuerID;
    }

    public String getVideoAmt() {
        if (VideoAmt == null){
            VideoAmt = "";
        }
        return VideoAmt;
    }

    public void setVideoAmt(String videoAmt) {
        VideoAmt = videoAmt;
    }

    public String getDescription() {
        if (Description == null){
            Description = "";
        }
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPetID() {
        if (PetID == null){
            PetID = "";
        }
        return PetID;
    }

    public void setPetID(String petID) {
        PetID = petID;
    }

    public String getPhotoAmt() {
        if (PhotoAmt == null){
            PhotoAmt = "";
        }
        return PhotoAmt;
    }

    public void setPhotoAmt(String photoAmt) {
        PhotoAmt = photoAmt;
    }

    public String getImageDir() {
        return ImageDir;
    }

    public void setImageDir(String imageDir) {
        ImageDir = imageDir;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    @Override
    public String toString() {
        return "Record{" +
                "id='" + id + '\'' +
                ", Type='" + Type + '\'' +
                ", Name='" + Name + '\'' +
                ", Age=" + Age +
                ", Breed1='" + Breed1 + '\'' +
                ", Breed2='" + Breed2 + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Color1='" + Color1 + '\'' +
                ", Color2='" + Color2 + '\'' +
                ", Color3='" + Color3 + '\'' +
                ", MaturitySize='" + MaturitySize + '\'' +
                ", FurLength='" + FurLength + '\'' +
                ", Vaccinated='" + Vaccinated + '\'' +
                ", Dewormed='" + Dewormed + '\'' +
                ", Sterilized='" + Sterilized + '\'' +
                ", Health='" + Health + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", Fee='" + Fee + '\'' +
                ", State='" + State + '\'' +
                ", RescuerID='" + RescuerID + '\'' +
                ", VideoAmt='" + VideoAmt + '\'' +
                ", Description='" + Description + '\'' +
                ", PetID='" + PetID + '\'' +
                ", PhotoAmt='" + PhotoAmt + '\'' +
                ", ImageDir='" + ImageDir + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
