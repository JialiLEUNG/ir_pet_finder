package com.ir.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.ir.entities.Pet;

import java.io.IOException;

public class PetDeserializer extends JsonDeserializer<Pet> {
    @Override
    public Pet deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode petNode = jsonParser.getCodec().readTree(jsonParser);

        Pet pet = new Pet();

        if (petNode.has("Type")){
            pet.setType(petNode.get("Type").textValue());
        }
        if (petNode.has("Name")){
            pet.setName(petNode.get("Name").textValue());
        }
        if (petNode.has("Age")){
            pet.setAge(petNode.get("Age").doubleValue());
        }
//        if (petNode.has("Breeds")){
//            pet.setBreeds(petNode.get("Breeds").)
//        }
        if (petNode.has("Breed1")){
            pet.setBreed1(petNode.get("Breed1").textValue());
        }
        if (petNode.has("Breed2")){
            pet.setBreed2(petNode.get("Breed2").textValue());
        }
        if (petNode.has("Gender")){
            pet.setGender(petNode.get("Gender").textValue());
        }
        if (petNode.has("Color1")){
            pet.setColor1(petNode.get("Color1").textValue());
        }
        if (petNode.has("Color2")){
            pet.setColor2(petNode.get("Color2").textValue());
        }
        if (petNode.has("Color3")){
            pet.setColor3(petNode.get("Color3").textValue());
        }
        if (petNode.has("MaturitySize")){
            pet.setMaturitySize(petNode.get("MaturitySize").textValue());
        }
        if (petNode.has("FurLength")){
            pet.setFurLength(petNode.get("FurLength").textValue());
        }
        if (petNode.has("Vaccinated")){
            pet.setVaccinated(petNode.get("Vaccinated").textValue());
        }
        if (petNode.has("Dewormed")){
            pet.setDewormed(petNode.get("Dewormed").textValue());
        }
        if (petNode.has("Sterilized")){
            pet.setSterilized(petNode.get("Sterilized").textValue());
        }
        if (petNode.has("Health")){
            pet.setHealth(petNode.get("Health").textValue());
        }
        if (petNode.has("Quantity")){
            pet.setQuantity(petNode.get("Quantity").textValue());
        }
        if (petNode.has("Fee")){
            pet.setFee(petNode.get("Fee").doubleValue());
        }
        if (petNode.has("State")){
            pet.setState(petNode.get("State").textValue());
        }
        if (petNode.has("RescuerID")){
            pet.setRescuerID(petNode.get("RescuerID").textValue());
        }
        if (petNode.has("VideoAmt")){
            pet.setVideoAmt(petNode.get("VideoAmt").textValue());
        }
        if (petNode.has("PhotoAmt")){
            pet.setPhotoAmt(petNode.get("PhotoAmt").textValue());
        }
        if (petNode.has("Description")){
            pet.setDescription(petNode.get("Description").textValue());
        }
        if (petNode.has("Description")){
            pet.setDescription(petNode.get("Description").textValue());
        }
        if (petNode.has("PetID")){
            pet.setPetID(petNode.get("PetID").textValue());
        }
        if (petNode.has("ImageDir")){
            pet.setPetID(petNode.get("ImageDir").textValue());
        }
        return pet;
    }
}