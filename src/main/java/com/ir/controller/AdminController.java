package com.ir.controller;

import com.ir.service.PetIndexService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The Admin controller exposes the admin endpoint for elasticsearch indexing
 * the @Controller annotation is mapped to the following two paths:
 * /admin
 * /index_data
 *
 * To autowire a bean (e.g., indexService) into our code, we use @Inject.
 */

@Controller("/admin")
public class AdminController {

//    private final PetIndexService indexService;
    private final PetIndexService indexService;


    @Inject
    public AdminController(PetIndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * This is an api for indexing the data
     * @param numberOfPets
     * @return
     * @throws IOException
     */
    @Post("/index_data")
    public CompletableFuture<HttpStatus> index(@QueryValue Integer numberOfPets) throws IOException {
        if (numberOfPets <= 0) {
            numberOfPets = 50000;
        }
        return indexService.indexPets(numberOfPets);
    }

    @Post(value = "configure_synonyms", consumes = MediaType.APPLICATION_JSON)
    public CompletableFuture<HttpStatus> index(@Body Map<String, String> synonyms) throws IOException {
        return indexService.configureSynonyms(synonyms.get("synonyms"));
    }

}
