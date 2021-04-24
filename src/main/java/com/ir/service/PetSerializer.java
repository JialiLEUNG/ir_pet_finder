package com.ir.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ir.entities.Pet;

import java.io.IOException;


public class PetSerializer extends JsonSerializer<Pet> {
    @Override
    public void serialize(Pet pet, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("Type", pet.getType());
        jsonGenerator.writeStringField("Name", pet.getName());
        jsonGenerator.writeNumberField("Age", pet.getAge());
        jsonGenerator.writeFieldName("Breeds");
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString(pet.getBreed1());
        jsonGenerator.writeString(pet.getBreed2());
        jsonGenerator.writeEndArray();
//        jsonGenerator.writeStringField("Breed1", pet.getBreed1());
//        jsonGenerator.writeStringField("Breed2", pet.getBreed2());
        jsonGenerator.writeStringField("Gender", pet.getGender());
        jsonGenerator.writeFieldName("Colors");
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString(pet.getColor1());
        jsonGenerator.writeString(pet.getColor2());
        jsonGenerator.writeString(pet.getColor3());
        jsonGenerator.writeEndArray();
//        jsonGenerator.writeStringField("Color1", pet.getColor1());
//        jsonGenerator.writeStringField("Color2", pet.getColor2());
//        jsonGenerator.writeStringField("Color3", pet.getColor3());

        jsonGenerator.writeStringField("MaturitySize", pet.getMaturitySize());
        jsonGenerator.writeStringField("FurLength", pet.getFurLength());
        jsonGenerator.writeStringField("Vaccinated", pet.getVaccinated());
        jsonGenerator.writeStringField("Dewormed", pet.getDewormed());
        jsonGenerator.writeStringField("Sterilized", pet.getSterilized());
        jsonGenerator.writeStringField("Health", pet.getHealth());
        jsonGenerator.writeStringField("Quantity", pet.getQuantity());
        jsonGenerator.writeNumberField("Fee", pet.getFee());
        jsonGenerator.writeStringField("State", pet.getState());
        jsonGenerator.writeStringField("RescuerID", pet.getRescuerID());
        jsonGenerator.writeStringField("VideoAmt", pet.getVideoAmt());
        jsonGenerator.writeStringField("PhotoAmt", pet.getPhotoAmt());
        jsonGenerator.writeStringField("Description", pet.getDescription());
        jsonGenerator.writeStringField("ImageDir", pet.getImageDir());
//        jsonGenerator.writeNumberField("LastUpdated", pet.getLastUpdated());
//        System.out.println("$$$$$$$ start an object");
//        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("LastUpdated");
        jsonGenerator.writeObject(pet.getLastUpdated());
        jsonGenerator.writeStringField("LastUpdatedStr", pet.getLastUpdatedStr().substring(3,11) + pet.getLastUpdatedStr().substring(pet.getLastUpdatedStr().length() - 4));
        jsonGenerator.writeEndObject();
    }
}
