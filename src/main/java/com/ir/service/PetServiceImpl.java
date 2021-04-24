package com.ir.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ir.entities.Pet;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * TODO: use async in a real application
 */
public class PetServiceImpl implements PetService {
    private final String index;
    private final RestHighLevelClient client;
    private final ObjectMapper mapper;
    private static final Logger LOG = LoggerFactory.getLogger(PetServiceImpl.class);


    public PetServiceImpl(String index, RestHighLevelClient client) {
        this.index = index;
        this.client = client;
        this.mapper = createMapper();
    }

    /**
     * find a record by id
     * @param id
     * @return
     * @throws IOException
     */
    @Override
    public Pet findById(String id) throws IOException {
        final GetResponse response = client.get(new GetRequest(index, id), RequestOptions.DEFAULT);
        final Pet pet = mapper.readValue(response.getSourceAsBytes(), Pet.class);
        pet.setId(response.getId());
        return pet;
    }

    @Override
    public Page<Pet> search(String query) throws IOException {
        return createPage(createSearchRequest(query, 0, 10), query);
    }

    @Override
    public Page<Pet> next(Page page) throws IOException {
        int from = page.getFrom() + page.getSize();
        final SearchRequest request = createSearchRequest(page.getInput(), from, page.getSize());
        return createPage(request, page.getInput());
    }


    /**
     * Using the save(List<Pets>) method,
     * we group multiple updates into a single bulk requests.
     * However, this would require us to batch single entity operations into one big operation.
     * If you rely on the document to be indexed,
     * once the call returns,
     * you would need to have you calling code waiting until the bulk processor returns.
     * Now you could simplify the save() method for a single POJO like this
     * @param pet
     * @throws IOException
     */
    @Override
    public void save(Pet pet) throws IOException {
        save(Collections.singletonList(pet));
    }

    /**
     * After calling requestIndex(),
     * we have a first mismatch between our application and how Elasticsearch behaves.
     * Elasticsearch prefers many document updates at once,
     * from an application perspective you are usually working on a single entity
     * and update that one.
     * So the solution is:
     * we group multiple updates into a single bulk requests.
     * @param pets
     * @throws IOException
     */
    public void save(List<Pet> pets) throws IOException{
        // a bulk request is a collection of write requests (index, update, delete).
        // Grouping requests together will greatly improve performance.
        BulkRequest request = new BulkRequest();
        for (Pet pet : pets) {
            request.add(indexRequest(pet));
        }

//        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        final BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        LOG.info("INDEX | Finished indexing run. Indexed [{}] documents in [{}]", response.getItems().length, response.getTook());
        for (int i = 0; i < pets.size(); i++){
            pets.get(i).setId(response.getItems()[i].getId());
//            System.out.println("id in bulk request: " + pets.get(i).getId());
        }
    }

    /**
     * Serialize the POJO (pet object) to a byte array,
     * and optionally set the pet ID.
     * The main work is done by jackson using the above defined serializer.
     * @param pet
     * @return
     * @throws IOException
     */
    private IndexRequest indexRequest(Pet pet) throws IOException {
        final byte[] bytes = mapper.writeValueAsBytes(pet);
        final IndexRequest request = new IndexRequest(index);
        if (pet.getId() != null){
            request.id(pet.getId());
        }
        request.source(bytes, XContentType.JSON);
        return request;
    }

    /**
     * The next step is to execute the search request.
     * If there are no hits returned
     * or due to whatever reason the hits array in the response is empty,
     * a Page.EMTPY object is returned, indicating zero results.
     * If there is data to parse,
     * then each hit,
     * will be converted to a Product POJO
     * and the list of products will be returned within the Page object.
     * @param searchRequest
     * @param query
     * @return
     * @throws IOException
     */
    private Page<Pet> createPage(SearchRequest searchRequest, String query) throws IOException{
        final SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        if (response.getHits().getTotalHits().value == 0){
            return Page.EMPTY;
        }
        if (response.getHits().getHits().length == 0){
            return Page.EMPTY;
        }
        List<Pet> pets = new ArrayList<>(response.getHits().getHits().length);
        for (SearchHit hit : response.getHits().getHits()) {
            final Pet pet = mapper.readValue(hit.getSourceAsString(), Pet.class);
            pet.setId(hit.getId());
            pets.add(pet);
        }
        final SearchSourceBuilder source = searchRequest.source();
        return new Page(pets, query, source.from(), source.size());
    }


    /**
     *  First, a SearchRequest gets created,
     *  retrieving the first 10 hits for the input term
     *  and returning a Page object.
     *  This way, we can abstract away the pagination implementation,
     *  and we could replace it using search_after.
     *  The query itself is pretty simple,
     *  a match query on two fields, namely name and description.
     * @param input
     * @param from
     * @param size
     * @return
     */
    private SearchRequest createSearchRequest(String input, int from, int size) {
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        final QueryBuilder query = QueryBuilders.matchQuery("Description", input);
        searchSourceBuilder
                .from(from)
                .size(size)
                .query(query);
        return new SearchRequest(index).source(searchSourceBuilder);
    }


    static final ObjectMapper createMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Pet.class, new PetSerializer());
        module.addDeserializer(Pet.class, new PetDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}
