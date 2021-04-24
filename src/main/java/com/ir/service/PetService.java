package com.ir.service;

import com.ir.entities.Pet;

import java.io.IOException;

public interface PetService {
    Pet findById(String id) throws IOException;
    Page<Pet> search(String query) throws IOException;
    Page<Pet> next(Page page) throws IOException;
    void save(Pet Pet) throws IOException;
}
