package com.ir.classes;

public class ModelConst {
    // Gender - Gender of pet
    // 1 = Male, 2 = Female, 3 = Mixed, if profile represents group of pets
    public static final String[] GENDER = {"", "male", "female", "mixed"};
    //MaturitySize - Size at maturity
    // 1 = Small, 2 = Medium, 3 = Large, 4 = Extra Large, 0 = Not Specified
    public static final String[] MATURITY_SIZE = {"not specified", "small", "medium", "large", "extra large"};
    // FurLength - Fur length
    // 1 = Short, 2 = Medium, 3 = Long, 0 = Not Specified
    public static final String[] FUR_LENGTH = {"not specified", "small", "medium", "long"};
    // Vaccinated - Pet has been vaccinated
    // 1 = Yes, 2 = No, 3 = Not Sure
    public static final String[] VACCINATED = {"", "yes", "no", "not sure"};
    // Dewormed - Pet has been dewormed
    // 1 = Yes, 2 = No, 3 = Not Sure
    public static final String[] DEWORMED = {"", "yes", "no", "not sure"};
    // Sterilized - Pet has been spayed / neutered
    // 1 = Yes, 2 = No, 3 = Not Sure
    public static final String[] STERILIZED = {"", "yes", "no", "not sure"};
    // Health - Health Condition
    // 1 = Healthy, 2 = Minor Injury, 3 = Serious Injury, 0 = Not Specified
    public static final String[] HEALTH = {"not specified", "healthy",  "minor injury", "serious injury"};
    // Fee - Adoption fee (0 = Free)
    public static final String FEE = "0";
    // Type - Type of animal (1 = Dog, 2 = Cat)
    public static final String[] TYPE = {"", "Dog", "Cat"};

}
