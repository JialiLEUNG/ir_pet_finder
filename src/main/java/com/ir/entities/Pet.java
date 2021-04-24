package com.ir.entities;

import java.util.Date;

// adhere to bean properties, so we can use jackson bean introspection
public class Pet {
//    private static final Faker faker = Faker.instance(Locale.US);

    private String id;
    private String Type;
    private String Name;
    private double Age = 0.0;
//    private String[] Breeds;
    private String Breed1;
    private String Breed2;
    private String Gender;
//    private String[] Colors;
    private String Color1;
    private String Color2;
    private String Color3;
    private String MaturitySize;
    private String FurLength;
    private String Vaccinated;
    private String Dewormed;
    private String Sterilized;
    private String Health;
    private String Quantity;
    private double Fee = 0.0;
    private String State;
    private String RescuerID;
    private String VideoAmt;
    private String Description;
    private String PetID;
    private String PhotoAmt;
    private String imageDir;
//    private int LastUpdated;
//    private Date LastUpdated = faker.date().past(365, TimeUnit.DAYS);
    private Date LastUpdated;
    private String LastUpdatedStr;


    public Pet() {
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

//    public String[] getBreeds() {
//        return Breeds;
//    }
//
//    public void setBreeds(String[] breeds) {
//        Breeds = breeds;
//    }

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

//    public String[] getColors() {
//        return Colors;
//    }
//
//    public void setColors(String[] colors) {
//        Colors = colors;
//    }

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

    public double getFee() {
        return Fee;
    }

    public void setFee(double fee) {
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
        return imageDir;
    }

    public void setImageDir(String imageDir) {
        this.imageDir = imageDir;
    }

    public Date getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        LastUpdated = lastUpdated;
    }

    public String getLastUpdatedStr() {
        return LastUpdatedStr;
    }

    public void setLastUpdatedStr(String lastUpdatedStr) {
        LastUpdatedStr = lastUpdatedStr;
    }

    public String getContent() {
        return Type + " " +
                Name + " " +
                Age + " " +
//                Breeds + "" +
                Breed1 + " " +
                Breed2 + " " +
                Gender + " " +
                Color1 + " " +
                Color2 + " " +
                Color3 + " " +
                MaturitySize + " " +
                FurLength + " " +
                Vaccinated + " " +
                Dewormed + " " +
                Sterilized + " " +
                Health + " " +
                Quantity + " " +
                Fee + " " +
                State + " " +
                RescuerID + " " +
                VideoAmt + " " +
                Description + " " +
                PhotoAmt + " " +
                PetID;
    }

//    @Override
//    public String toString() {
//        return "Record{" +
//                "Type='" + Type + '\'' +
//                ", Name='" + Name + '\'' +
//                ", Age='" + Age + '\'' +
//                ", Breed1='" + Breed1 + '\'' +
//                ", Breed2='" + Breed2 + '\'' +
//                ", Gender='" + Gender + '\'' +
//                ", Color1='" + Color1 + '\'' +
//                ", Color2='" + Color2 + '\'' +
//                ", Color3='" + Color3 + '\'' +
//                ", MaturitySize='" + MaturitySize + '\'' +
//                ", FurLength='" + FurLength + '\'' +
//                ", Vaccinated='" + Vaccinated + '\'' +
//                ", Dewormed='" + Dewormed + '\'' +
//                ", Sterilized='" + Sterilized + '\'' +
//                ", Health='" + Health + '\'' +
//                ", Quantity='" + Quantity + '\'' +
//                ", Fee='" + Fee + '\'' +
//                ", State='" + State + '\'' +
//                ", RescuerID='" + RescuerID + '\'' +
//                ", VideoAmt='" + VideoAmt + '\'' +
//                ", Description='" + Description + '\'' +
//                ", PetID='" + PetID + '\'' +
//                ", PhotoAmt='" + PhotoAmt + '\'' +
//                '}';
//    }
}
