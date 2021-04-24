package com.ir.service;

import com.ir.classes.FileConst;
import com.ir.dataPreProcess.*;
import com.ir.entities.Pet;
import io.micronaut.http.HttpStatus;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestStatus;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Singleton
public class PetIndexService {
    private static final String index = "my_index";
    private static RestHighLevelClient client;
    private static PetServiceImpl petService;

    @Inject
    public PetIndexService(RestHighLevelClient client) {
        this.client = client;
    }

    public static void setClient(RestHighLevelClient client) {
        PetIndexService.client = client;
    }

    /**
     * Indexing the docs through Elasticsearch
     * Indexing here is the process of adding data to Elasticsearch.
     * This is because when we feed data into Elasticsearch,
     * the data is placed into Apache Lucene indexes.
     * Elasticsearch uses the Lucene indexes to store and retrieve its data.
     * @return
     * @throws Exception
     */
    public void run() throws Exception {
//        petPreIndex();
        startPetService();
        deletePetIndex();

        List<Pet> pets = WriteIndex();

        petService.save(pets);

        client.indices().refresh(new RefreshRequest(index), RequestOptions.DEFAULT);
    }


    /**
     * Create some random pets,
     * the user can specify how many (TODO)
     *
     * @param count Number of pets to be created
     * @throws IOException
     */
    public CompletableFuture<HttpStatus> indexProducts(int count) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                petPreIndex();
                startPetService();
                deletePetIndex();

                List<Pet> pets = WriteIndex();

                petService.save(pets);

                client.indices().refresh(new RefreshRequest(index), RequestOptions.DEFAULT);
                return HttpStatus.OK;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }


    public void deletePetIndex() throws Exception {
        try {
            // Synchronous execution
            // When executing a DeleteIndexRequest in the following manner,
            // the client waits for the DeleteIndexResponse to be returned
            // before continuing with code execution:
            client.indices()
                    .delete(new DeleteIndexRequest(index),
                            RequestOptions.DEFAULT);
        } catch (ElasticsearchStatusException e) {
            // If the index was not found,
            // an ElasticsearchException will be thrown:
            e.printStackTrace();
            if (e.status() == RestStatus.NOT_FOUND) {
                System.out.println("[Error]: Index not found.");
            }
        }
    }

    public static void closeResources() throws Exception {
        client.close();
    }

    /**
     * index the docs before feeding the docs into ES.
     * This serves as a preliminary data preprocess,
     * which reads the raw csv files, tokenizes,
     * and normalizes (and stems) them,
     * and finally write them into result.txt (save data into disk)
     * The result.txt is then loaded onto ES for further indexing.
     * @throws Exception
     */
    public void petPreIndex() throws Exception{
        List<String> xmlFileDir = new ArrayList<>();
        for (String file : FileConst.dataFile) {
            xmlFileDir.add(getXMLdirectory(file, "static/csv", false));
        }

        Map<String, String> labelsFileDirs = new HashMap<>();
        for (String label : FileConst.labelFileName){
            labelsFileDirs.put(label, getXMLdirectory(label, "static/csv", false));
        }

        for (String xml : xmlFileDir){
            docDataPreProcess(xml, labelsFileDirs);
        }
    }

    /**
     * find out the directory of the csv files (data)
     * @return
     */
    public String getXMLdirectory(String fileName, String folderName, boolean findByExtension){
        // "static/csv" == folderName
        ClassLoader classLoader = getClass().getClassLoader();
        String path  = Objects.requireNonNull(classLoader.getResource(folderName)).getPath();
        File dir = new File(path); //Directory where xml file exists
        File[] files = dir.listFiles();
        String paths = "";

        if (findByExtension){
            for(File file : files) {
                // You can validate file name with extension if needed
            if(file.isFile() && file.getName().endsWith(".jpg")) {
//                if (file.isFile() && file.getName().equals(fileName)){
                    paths = file.toString();
                }
            }
        }
        else{
            for(File file : files) {
                // You can validate file name with extension if needed
//            if(file.isFile() && file.getName().endsWith(fileName) && !file.getName().endsWith("cfquery.xml")) {
                if (file.isFile() && file.getName().equals(fileName)){
                    paths = file.toString();
                }
            }

        }

        return paths;
    }


    public String getXMLdirectory(String folderName, String petId){
        // "static/csv" == folderName
        ClassLoader classLoader = getClass().getClassLoader();
        String path  = Objects.requireNonNull(classLoader.getResource(folderName)).getPath();
        File dir = new File(path); //Directory where xml file exists
        File[] files = dir.listFiles();
        String paths = "";

        for(File file : files) {
            // You can validate file name with extension if needed
            if (file.isFile() && file.getName().equals(petId)){
                paths = file.toString();
            }
        }
        return paths;
    }


    public void docDataPreProcess(String csvFileDir, Map<String, String> labelFileDirsMap) throws Exception {
        // load the csv files and initiate DocCollection
        CsvFileCollection corpus = new CsvFileCollection(csvFileDir, labelFileDirsMap);

        // Load stopword list and initiate the StopWordRemover and WordNormalizer
        StopwordRemover stopwordRemover = new StopwordRemover();
        WordNormalizer wordNormalizer = new WordNormalizer();

        // initiate the BufferedWriter to output result
        FilePathGenerator fpg = new FilePathGenerator("result.txt");
        String path = fpg.getPath();

        FileWriter fileWriter = new FileWriter(path, false);
        Map<String, Map<String, String>> curr_docs = corpus.nextDoc(); // doc_id:doc_content pairs
        Set<String> doc_ids = curr_docs.keySet();
        for (String doc_id : doc_ids){
            // write doc_id into the result file
            fileWriter.append(doc_id + "\n");

            // load doc content
            for (String field_name : FileConst.fields){
                String field_value = curr_docs.get(doc_id).getOrDefault(field_name, "");
                fileWriter.append(field_value + "\n");
            }
        }
        fileWriter.close();
    }


    /**
     * (1) indexing the doc, write the indexes to docs.
     * (2) turn each doc into Pet object, and put them into an arraylist
     * so that it could be used as a Json object (serialization/deserialization) later on
     * @throws Exception
     */
    public List<Pet> WriteIndex() throws Exception {
        // initiate corpus, which is a preprocessed collection of file reader
        CorpusReader corpus = new CorpusReader();

        // Map to hold doc_id and doc content;
        Map<String, String> doc;

        // turn each doc into json object, and put them in to a list
        List<Pet> petList = new ArrayList<>();

        // index the corpus, load the doc one by one
        while ((doc = corpus.NextDoc()) != null){
            // create a pet json object for the doc
            Pet pet = new Pet();
            pet.setId(doc.get("DOC_ID"));
            pet.setDescription(doc.getOrDefault("description", ""));
            pet.setType(doc.getOrDefault("type", ""));
            pet.setAge(Double.parseDouble(doc.getOrDefault("age", "0")));
//            pet.setBreeds(getBreedsOrColors(doc.getOrDefault("breeds", "")));
//            pet.setColors(getBreedsOrColors(doc.getOrDefault("colors", "")));
            pet.setBreed1(doc.getOrDefault("breed1", ""));
            pet.setBreed2(doc.getOrDefault("breed2", ""));
            pet.setColor1(doc.getOrDefault("color1", ""));
            pet.setColor2(doc.getOrDefault("color2", ""));
            pet.setColor3(doc.getOrDefault("color3", ""));
            pet.setDewormed(doc.getOrDefault("dewormed", ""));
            pet.setVideoAmt(doc.getOrDefault("videoAmt", ""));
            pet.setVaccinated(doc.getOrDefault("vaccinated", ""));
            pet.setSterilized(doc.getOrDefault("sterilized", ""));
            pet.setState(doc.getOrDefault("state", ""));
            pet.setRescuerID(doc.getOrDefault("rescuerId", ""));
            pet.setQuantity(doc.getOrDefault("quantity", ""));
            pet.setPhotoAmt(doc.getOrDefault("photoAmt", ""));
            pet.setName(doc.getOrDefault("name", ""));
            pet.setMaturitySize(doc.getOrDefault("maturitySize", ""));
            pet.setHealth(doc.getOrDefault("health", ""));
            pet.setGender(doc.getOrDefault("gender", ""));
            pet.setFurLength(doc.getOrDefault("furLength", ""));
            pet.setFee(Double.parseDouble(doc.getOrDefault("fee", "0")));
//            pet.setImageDir(getXMLdirectory("static/image", doc.get("DOC_ID") + ".jpg"));
            pet.setImageDir(doc.getOrDefault("imageDir", ""));
//            pet.setLastUpdated(Integer.parseInt(doc.getOrDefault("lastUpdated", "1" )));

            Date dt = new SimpleDateFormat("E MMM dd HH:mm:ss z yyy").parse(doc.getOrDefault("lastUpdated", ""));
            pet.setLastUpdated(dt);

            pet.setLastUpdatedStr(doc.getOrDefault("lastUpdated", ""));

            // put the Pet object into the list
            petList.add(pet);
        }
        return petList;
    }

    private String[] getBreedsOrColors(String myString){
        String[] myArray = myString.split(",");
        List<String> myList = Arrays.asList(myArray);
        //turn arraylist to string[];
        String[] result = new String[myList.size()];
        for (int i = 0; i < myList.size(); i++){
            result[i] = myList.get(i);
        }
        return result;
    }

    private static final NodeSelector INGEST_NODE_SELECTOR = nodes -> {
        final Iterator<Node> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            // roles may be null if we don't know, thus we keep the node in then...
            if (node.getRoles() != null && node.getRoles().isIngest() == false) {
                iterator.remove();
            }
        }
    };

    public static void startPetService() {
        petService = new PetServiceImpl(index, client);
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-synonym-tokenfilter.html
     * @param synonyms
     * @return
     */
    public CompletableFuture<HttpStatus> configureSynonyms(String synonyms){
        return CompletableFuture.supplyAsync(() -> {
            try {
                client.indices().close(new CloseIndexRequest(index), RequestOptions.DEFAULT);
                Settings settings = Settings.builder()
                        .putList("index.analysis.filter.my_synonym_filter.synonyms", synonyms.split("\n"))
                        .build();
                client.indices().putSettings(new UpdateSettingsRequest(index).settings(settings), RequestOptions.DEFAULT);
                client.indices().open(new OpenIndexRequest().indices(index), RequestOptions.DEFAULT);
                return HttpStatus.OK;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
