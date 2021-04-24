package com.ir.controller;

import com.ir.entities.Query;
import com.ir.service.PetQueryService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.elasticsearch.client.Response;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller("/search")
public class SearchController {

private final PetQueryService service;

    @Inject
    public SearchController(PetQueryService service) {
        this.service = service;
    }

    @Post(value = "pets_without_fulltext", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public CompletableFuture<Response> searchWithoutFullText(@Body Query query) throws IOException, ExecutionException, InterruptedException {
        return service.searchWithFilteredAggs(query, false, false);
    }

    @Post(value = "pets_only", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public CompletableFuture<Response> searchPetsOnly(@Body Query query) throws IOException, ExecutionException, InterruptedException {
        return service.searchPetsOnly(query);
    }

    @Post(value = "pets_with_filtered_aggs", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public CompletableFuture<Response> searchWithFilteredAggs(@Body Query query) throws IOException, ExecutionException, InterruptedException {
        return service.searchWithFilteredAggs(query, false, true);
    }

    @Post(value = "pets_with_filtered_aggs_with_boost", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public CompletableFuture<Response> searchWithFilteredAggsWithBoost(@Body Query query) throws IOException, ExecutionException, InterruptedException {
        return service.searchWithFilteredAggs(query, true, true);
    }
}
