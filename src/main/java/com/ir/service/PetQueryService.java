package com.ir.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ir.entities.Query;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.termvectors.TermVectorsResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.SuppressForbidden;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MinAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * PetQueryService is to generate query request to ES
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-query-builders.html
 * This builds a client to consume the endpoints we creates (e.g., pets_without_fulltext).
 */
@Singleton
public class PetQueryService {
    private final RestHighLevelClient client;
    private static final String index = "my_index";
    private static final Logger LOG = LoggerFactory.getLogger(PetServiceImpl.class);

    @Inject
    public PetQueryService(RestHighLevelClient client) {
        this.client = client;
    }


    /**
     * search only across hits, don't include any aggregations
     * Here is an asynchronous computation:
     * Triggering a potentially time-consuming action inside a Future allows the caller Thread to
     * continue doing useful work instead of just waiting for the operation’s result.
     * @param query
     * @return
     * @throws IOException
     */
    public CompletableFuture<Response> searchPetsOnly(Query query) throws IOException, ExecutionException, InterruptedException {
        return asyncSearch(createFullTextSearchQuery(query), null, query.getFrom());
    }

    /**
     * This search filters for the specified aggregations, uses a post filter
     * The drawback of this solution is, that on selection of an aggregation,
     * this is only appended to the post filter,
     * so the aggregation counts never change
     *
     * Stock and Price are created as regular filters as part of the query,
     * which indeed will change the aggregations
     */
    public CompletableFuture<Response> searchWithAggs(Query query) throws IOException, ExecutionException, InterruptedException {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(createFullTextSearchQuery(query));
        // filter for price and stock, as they become range queries
        query.getFilters().stream().filter(filter -> List.of("Age", "Fee").contains(filter.getKey()))
                .forEach(filter -> queryBuilder.must(filter.toQuery()));

        MinAggregationBuilder minAgeAgg = AggregationBuilders.min("min_age").field("Age");
        MaxAggregationBuilder maxAgeAgg = AggregationBuilders.max("max_age").field("Age");
        TermsAggregationBuilder byBrand = AggregationBuilders.terms("by_breed").field("Breeds.keyword");
        TermsAggregationBuilder byColor = AggregationBuilders.terms("by_color").field("Colors.keyword");
        FiltersAggregator.KeyedFilter notInStockFilter = new FiltersAggregator.KeyedFilter("vaccinated", QueryBuilders.termQuery("Vaccinated", 0));
        FiltersAggregator.KeyedFilter inStockFilter = new FiltersAggregator.KeyedFilter("not_vaccinated", QueryBuilders.rangeQuery("stock").gt(0));
        FiltersAggregationBuilder inStockAgg = AggregationBuilders.filters("by_Vaccinated", inStockFilter, notInStockFilter);

        BoolQueryBuilder postFilterQuery = QueryBuilders.boolQuery();
        Map<String, List<Query.Filter>> byKey = query.getFilters().stream()
                .filter(filter -> List.of("Age", "Vaccinated").contains(filter.getKey()) == false)
                .collect(Collectors.groupingBy(Query.Filter::getKey));
        for (Map.Entry<String, List<Query.Filter>> entry : byKey.entrySet()) {
            BoolQueryBuilder orQueryBuilder = QueryBuilders.boolQuery();

            for (Query.Filter filter : entry.getValue()) {
                orQueryBuilder.should(QueryBuilders.termQuery(filter.getKey() + ".keyword", filter.getValue()));
            }

            postFilterQuery.filter(orQueryBuilder);
        }

        postFilterQuery = postFilterQuery.filter().isEmpty() ? null : postFilterQuery;

        return asyncSearch(
                queryBuilder,
                postFilterQuery,
                query.getFrom(),
                byColor,
                byBrand,
                minAgeAgg,
                maxAgeAgg,
                inStockAgg);
    }


    /**
     * This is the ultimate query,
     * where all facets are filtered based on the fields of the other facets.
     * This will result in a bigger query, but return proper numbers
     * see more documentation about filtered aggregations at:
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_bucket_aggregations.html
     */
    public CompletableFuture<Response> searchWithFilteredAggs(Query query, Boolean withBoost, Boolean withFullText) throws IOException, ExecutionException, InterruptedException {
        // this is the query for the total hits and the initial aggregations
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (withFullText){
            if (withBoost){
                queryBuilder.must(createFullTextSearchQueryWithBoost(query));
            } else {
                queryBuilder.must(createFullTextSearchQuery(query));
            }
        }


        // filter for age and adoption_fee, as they become range queries
        query.getFilters().stream().filter(filter -> List.of("Age", "Fee").contains(filter.getKey()))
                .forEach(filter -> queryBuilder.must(filter.toQuery()));

        // TODO these also need to be possibly filtered!
        MinAggregationBuilder minAgeAgg = AggregationBuilders.min("min_age").field("Age");
        MaxAggregationBuilder maxAgeAgg = AggregationBuilders.max("max_age").field("Age");

        FiltersAggregator.KeyedFilter freeAdoption = new FiltersAggregator.KeyedFilter("free_adoption",
                QueryBuilders.termQuery("Fee", 0));
        FiltersAggregator.KeyedFilter notFreeAdoption = new FiltersAggregator.KeyedFilter("not_free_adoption",
                QueryBuilders.rangeQuery("Fee").gt(0));
        FiltersAggregationBuilder byAdoptionFeeAgg = AggregationBuilders.filters("by_adoption_fee",
                notFreeAdoption, freeAdoption);

//        AggregationBuilder byBreedAgg = createPossiblyFilteredAgg(query,
//                "by_breed", "Breeds");
//        AggregationBuilder byColor = createPossiblyFilteredAgg(query,
//                "by_color", "Colors");
        AggregationBuilder byTypeAgg = createPossiblyFilteredAgg(query,
                "by_type", "Type");
        AggregationBuilder bySizeAgg = createPossiblyFilteredAgg(query,
                "by_size", "MaturitySize");
        AggregationBuilder byVaccinated = createPossiblyFilteredAgg(query,
                "by_vaccinated", "Vaccinated");

        // additional post filter for age and fee
        BoolQueryBuilder postFilterQuery = QueryBuilders.boolQuery();
        Map<String, List<Query.Filter>> byKey = query.getFilters().stream()
                .filter(filter -> List.of("Age", "Fee")
                        .contains(filter.getKey()) == false)
                .collect(Collectors.groupingBy(Query.Filter::getKey));
        for (Map.Entry<String, List<Query.Filter>> entry : byKey.entrySet()) {
            BoolQueryBuilder orQueryBuilder = QueryBuilders.boolQuery();

            for (Query.Filter filter : entry.getValue()) {
                LOG.info("Filter Key {}, Value {}", filter.getKey(), filter.getValue());
                orQueryBuilder.should(QueryBuilders.termQuery(
                        filter.getKey() + ".keyword",
                        filter.getValue()));
            }
            postFilterQuery.filter(orQueryBuilder);
        }
        postFilterQuery = postFilterQuery.filter().isEmpty() ? null : postFilterQuery;

        return asyncSearch(queryBuilder,
                postFilterQuery,
                query.getFrom(),
//                byColor,
//                byBreedAgg,
                byTypeAgg,
                bySizeAgg,
                byVaccinated,
                minAgeAgg,
                maxAgeAgg,
                byAdoptionFeeAgg
        );

        // prettify the response and log it.
//        String response = EntityUtils.toString(res.get().getEntity());
//        ObjectMapper mapper = new ObjectMapper();
//        Object json = mapper.readValue(response, Object.class);
//        String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//        LOG.info("RESPONSE {}" , indented);
//
//        return res;
    }


    private AggregationBuilder createPossiblyFilteredAgg(Query query, String aggregationName, String fieldName) {
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms(aggregationName).field(fieldName + ".keyword");
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        query.getFilters().stream()
                .filter(filter -> filter.getKey().equals(fieldName) == false) // filter out itself
                .forEach(filter -> queryBuilder.filter(filter.toQuery()));

        if (queryBuilder.filter().isEmpty() == false) {
            aggregationBuilder = AggregationBuilders.filter(aggregationName, queryBuilder).subAggregation(aggregationBuilder);
        }
        return aggregationBuilder;
    }


    /**
     * Creates regular text search query that covers all the fields,
     * but without boost on any particular field
     */
    private QueryBuilder createFullTextSearchQuery(Query query) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // In booluery,
        // Must is analogous to the boolean AND,
        // must_not is analogous to the boolean NOT,
        // and should is roughly equivalent to the boolean OR.
        // Note that should isn't exactly like a boolean OR,
        // but we can use it to that effect.
//        queryBuilder.should(QueryBuilders.multiMatchQuery(query.getQuery(),
////                "Description", "Breed1", "Breed2", "Color1", "Color2", "Color3")
//                "Description", "Breeds", "Colors")
//                .minimumShouldMatch("66%")
//                // when the user searches with mistyped keywords or misspellings.
//                // fuzziness allows minor misspellings to still return something to the user.
//                // I set it to the default AUTO. There's options like Fuzziness.ONE, edit_distance, etc.
//                // https://www.elastic.co/blog/found-fuzzy-search
//                .fuzziness(Fuzziness.AUTO));

        queryBuilder.should(QueryBuilders.multiMatchQuery(query.getQuery())
                .field("Description", 1.0f) // default is 1.0f
                .field("Breeds", 1.0f)
                .field("Colors", 1.0f)
//                .field("Vaccinated", 1.0f)
                .minimumShouldMatch("66%")
                .fuzziness(Fuzziness.AUTO));

        // increase scoring if we match in color, brand or material compared to pet name
//        queryBuilder.should(QueryBuilders.matchQuery("Description", query.getQuery()));
//        queryBuilder.should(QueryBuilders.matchQuery("Breed1", query.getQuery()));
//        queryBuilder.should(QueryBuilders.matchQuery("Breed2", query.getQuery()));
//        queryBuilder.should(QueryBuilders.matchQuery("Color1", query.getQuery()));
//        queryBuilder.should(QueryBuilders.matchQuery("Color2", query.getQuery()));
//        queryBuilder.should(QueryBuilders.matchQuery("Color3", query.getQuery()));

        return queryBuilder;
    }


    /**
     * Creates regular text search query that covers all the fields,
     * but WITH boost on some particular field (e.g., lastUpdated).
     */
    private QueryBuilder createFullTextSearchQueryWithBoost(Query query) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // In boolquery,
        // Must is analogous to the boolean AND,
        // must_not is analogous to the boolean NOT,
        // and should is roughly equivalent to the boolean OR.
        // Note that should isn't exactly like a boolean OR,
        // but we can use it to that effect.
//        queryBuilder.should(QueryBuilders.multiMatchQuery(query.getQuery(),
////                "Description", "Breed1", "Breed2", "Color1", "Color2", "Color3")
//                "Description", "Breeds", "Colors")
//                .minimumShouldMatch("66%")
//                // when the user searches with mistyped keywords or misspellings.
//                // fuzziness allows minor misspellings to still return something to the user.
//                // I set it to the default AUTO. There's options like Fuzziness.ONE, edit_distance, etc.
//                // https://www.elastic.co/blog/found-fuzzy-search
//                .fuzziness(Fuzziness.AUTO));

        queryBuilder.should(QueryBuilders.multiMatchQuery(query.getQuery())
                .field("Description", 1.0f) // default is 1.0f
                .field("Breeds", 1.0f)
                .field("Colors", 1.0f)
                .field("furLength", 1.0f)
                .field("health", 1.0f)
                .field("sterilized", 1.0f)
                .field("type", 1.0f)
//                .field("Vaccinated", 1.0f)
                .minimumShouldMatch("66%")
                .fuzziness(Fuzziness.AUTO));

        // Next, i boost the lastUpdated:
        // the lastUpdated has boost value set to 1.5,
        // making it more important than the other fields (see the multiMatchQuery above).
        // Note that it's possible to use wildcards and regex queries,
        // but performance-wise,
        // beware of memory consumption and response-time delay when dealing with wildcards,
        // because something like *_apples may cause a huge impact on performance.
        // I do not use this in the project.
        // The coefficient of importance is used to order the result set of hits returned
        // after executing the search() method.
        // https://www.baeldung.com/elasticsearch-java
        Random rnd = new Random();
        // current time
        long currentTimestamp = Instant.now().toEpochMilli();
        // subtract 30 DAYS to Instant
        long pastTimestamp = Instant.ofEpochMilli(currentTimestamp).atZone(ZoneId.systemDefault()).minusDays(30).toEpochSecond() * 1000;
        queryBuilder.should(QueryBuilders.rangeQuery("LastUpdated")
                .gt(pastTimestamp)
//                .lt(currentTimestamp)
                .boost(1.3f));


        // TODO: Using distance_feature to boost relevance score based on proximity of lastUpdated dates.
        // The closer to the current date, the higher the relevance.
        // The problem here is that:
        // ES failed to create query: Illegal data type of [long].
        // [distance_feature] query can only be run on a date, date_nanos or geo_point field type.
        // ES reads the Date object as long type.
        // I believe it's read as epoch initially, but the distance feature query reads it as long.
        // some useful documentations:
        // https://www.elastic.co/guide/en/elasticsearch/reference/current/date.html
        // https://www.elastic.co/guide/en/elasticsearch/reference/7.x/query-dsl-distance-feature-query.html
//        DistanceFeatureQueryBuilder.Origin origin = new DistanceFeatureQueryBuilder.Origin("now");
//        DistanceFeatureQueryBuilder lastUpdatedDistanceQuery = new DistanceFeatureQueryBuilder("LastUpdated", origin, "10d");
////
//        queryBuilder.should(lastUpdatedDistanceQuery);

        // So I switch to rank feature in order to boost the relevance score instead as an alternative.
        // The boost value is set to be 1.5 (this parameter is not validated yet).
        // More tuning is needed.
//        RankFeatureQueryBuilders lastUpdatedRankQuery = new RankFeatureQueryBuilders();
//        queryBuilder.should()

        // increase scoring if we match in color, brand or material compared to pet name
//        queryBuilder.should(QueryBuilders.matchQuery("Description", query.getQuery()));
//        queryBuilder.should(QueryBuilders.matchQuery("Breed1", query.getQuery()));
//        queryBuilder.should(QueryBuilders.matchQuery("Breed2", query.getQuery()));
//        queryBuilder.should(QueryBuilders.matchQuery("Color1", query.getQuery()));
//        queryBuilder.should(QueryBuilders.matchQuery("Color2", query.getQuery()));
//        queryBuilder.should(QueryBuilders.matchQuery("Color3", query.getQuery()));

        return queryBuilder;
    }


    private CompletableFuture<Response> asyncSearch(QueryBuilder queryBuilder,
                                                    QueryBuilder postFilterQuery,
                                                    int from, AggregationBuilder... aggs) throws IOException, ExecutionException, InterruptedException {
        SearchRequest request = search(queryBuilder, postFilterQuery, from, aggs);

        final CompletableFuture<Response> future = new CompletableFuture<>();
        ResponseListener listener = newResponseListener(future);

        Request lowLevelRequest = new Request(HttpPost.METHOD_NAME, index + "/_search");
        BytesRef source = XContentHelper.toXContent(request.source(), XContentType.JSON, ToXContent.EMPTY_PARAMS, true).toBytesRef();
//        LOG.info("QUERY {}", source.utf8ToString());
        String queryStr = source.utf8ToString();
        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(queryStr, Object.class);
        String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        LOG.info("QUERY {}", indented);

        lowLevelRequest.setEntity(new NByteArrayEntity(
                source.bytes,
                source.offset,
                source.length,
                createContentType(XContentType.JSON)));
        // performRequestAsync is asynchronous and
        // accepts a ResponseListener argument that it calls with a Response
        // when the request is successful or with an Exception if it fails.
        client.getLowLevelClient().performRequestAsync(lowLevelRequest, listener);

        // prettify the response and log it.
//        String response = EntityUtils.toString(future.get().getEntity());
//        Object jsonResponse = mapper.readValue(response, Object.class);
//        String indentedResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonResponse);
//        LOG.info("RESPONSE {}" , indentedResponse);

        return future;
    }

    // copied from RequestConverts.java, as it is private
    @SuppressForbidden(reason = "Only allowed place to convert a XContentType to a ContentType")
    private static ContentType createContentType(final XContentType xContentType) {
        return ContentType.create(xContentType.mediaTypeWithoutParameters(), (Charset) null);
    }


    private SearchRequest search(QueryBuilder queryBuilder,
                                 QueryBuilder postFilterQuery,
                                 int from,
                                 AggregationBuilder... aggs) {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(16);
        searchSourceBuilder.from(from);
        searchSourceBuilder.query(queryBuilder);
        if (postFilterQuery != null) {
            searchSourceBuilder.postFilter(postFilterQuery);
        }
        for (AggregationBuilder agg : aggs) {
            searchSourceBuilder.aggregation(agg);
        }
        request.source(searchSourceBuilder);
        return request;
    }

    private ResponseListener newResponseListener(final CompletableFuture<Response> future) {
        return new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                future.complete(response);
            }

            @Override
            public void onFailure(Exception exception) {
                future.completeExceptionally(exception);
            }
        };
    }


    public List<String> getTerms(TermVectorsResponse resp) throws IOException {

        List<String> termStrings = new ArrayList<>();
        Fields fields = resp.getFields();
        Iterator<String> iterator = fields.iterator();
        while (iterator.hasNext()) {
            String field = iterator.next();
            Terms terms = fields.terms(field);
            // The TermsEnum object provides further methods to
            // get some aggregated values for the current term
            TermsEnum termsEnum = terms.iterator();
            while(termsEnum.next() != null){
                BytesRef term = termsEnum.term();
                if (term != null) {
                    termStrings.add(term.utf8ToString());
                }
            }
        }
        return termStrings;
    }


}
