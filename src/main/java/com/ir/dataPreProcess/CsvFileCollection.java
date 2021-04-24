package com.ir.dataPreProcess;

import com.opencsv.bean.CsvToBeanBuilder;
import com.ir.classes.FileConst;
import com.ir.classes.ModelConst;
import com.ir.models.Breed;
import com.ir.models.Color;
import com.ir.models.State;
import com.ir.models.Record;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * read and parse the CSV records directly into Java objects
 */
public class CsvFileCollection implements DocCollection{
    private List<Record> beans;
    private List<Breed> breeds;
    private List<Color> colors;
    private List<State> states;
    private Map<String, String> breedsMap = new HashMap<>();
    private Map<String, String> colorMap = new HashMap<>();
    private Map<String, String> stateMap = new HashMap<>();


    public CsvFileCollection(String csvFileDir, Map<String, String> labelFileDirsMap) throws Exception {
        beans = new CsvToBeanBuilder(new FileReader(csvFileDir))
                .withType(Record.class)
                .build()
                .parse();

        for (String label : FileConst.labelFileName){
            if (label.equals("BreedLabels.csv")){
                breeds = new CsvToBeanBuilder(new FileReader(labelFileDirsMap.get(label)))
                        .withType(Breed.class)
                        .build()
                        .parse();
            }
            else if (label.equals("ColorLabels.csv")){
                colors = new CsvToBeanBuilder(new FileReader(labelFileDirsMap.get(label)))
                        .withType(Color.class)
                        .build()
                        .parse();
            }
            else if (label.equals("StateLabels.csv")) {
                states = new CsvToBeanBuilder(new FileReader(labelFileDirsMap.get(label)))
                        .withType(State.class)
                        .build()
                        .parse();
            } else {
                System.out.println("no such file");
            }
        }
        readLabelFiles();
    }

    /**
     * read BreedLabels.csv, ColorLabels.csv, and StateLabels.csv,
     * in order to convert the numeric values in raw dataset
     * into more descriptive values (string)
     */
    public void readLabelFiles() {
        for (Breed breed : breeds) {
            String breedID = breed.getBreedID();
            String breedName = breed.getBreedName();
            breedsMap.put(breedID, breedName);
        }
        for (Color color : colors) {
            String colorId = color.getColorID();
            String colorName = color.getColorName();
            colorMap.put(colorId, colorName);
        }
        for (State state : states) {
            String stateId = state.getStateID();
            String stateName = state.getStateName();
            stateMap.put(stateId, stateName);
        }
    }


    /**
     * nextDoc loads one document from the corpus,
     * and return this document's id and content.
     * (doc_id: doc_content pair)
     * @return document's id and content, and return null when no document left
     * @throws IOException
     */

    public Map<String, Map<String, String>> nextDoc() throws Exception {
        Map<String, Map<String, String>> docMap = new HashMap<>(); // doc_id, doc_content pair

        for (Record bean : beans){
            String docId = bean.getPetID();
            if (docId == null || docId.length() == 0){
                docId = UUID.randomUUID().toString().replace("-", "");
            }
            // build map and put it in the value
            Map<String, String> recordMap = new HashMap<>();
            recordMap.put("age", String.valueOf(bean.getAge()));
            recordMap.put("breed1", breedsMap.getOrDefault(bean.getBreed1(), ""));
            recordMap.put("breed2", breedsMap.getOrDefault(bean.getBreed2(), ""));
            recordMap.put("color1", colorMap.getOrDefault(bean.getColor1(), ""));
            recordMap.put("color2", colorMap.getOrDefault(bean.getColor2(), ""));
            recordMap.put("color3", colorMap.getOrDefault(bean.getColor3(), ""));
            recordMap.put("dewormed", ModelConst.DEWORMED[Integer.parseInt(bean.getDewormed())]);
            recordMap.put("furLength", ModelConst.FUR_LENGTH[Integer.parseInt(bean.getFurLength())]);
            recordMap.put("gender", ModelConst.GENDER[Integer.parseInt(bean.getGender())]);
            recordMap.put("description", bean.getDescription());
            recordMap.put("health", ModelConst.HEALTH[Integer.parseInt(bean.getHealth())]);
            recordMap.put("maturitySize", ModelConst.MATURITY_SIZE[Integer.parseInt(bean.getMaturitySize())]);
            recordMap.put("name", bean.getName());
            recordMap.put("photoAmt", bean.getPhotoAmt());
            recordMap.put("quantity", bean.getQuantity());
            recordMap.put("rescuerId", bean.getRescuerID());
            recordMap.put("videoAmt", bean.getVideoAmt());
            recordMap.put("vaccinated", ModelConst.VACCINATED[Integer.parseInt(bean.getVaccinated())]);
            recordMap.put("sterilized", ModelConst.STERILIZED[Integer.parseInt(bean.getSterilized())]);
            recordMap.put("type", ModelConst.TYPE[Integer.parseInt(bean.getType())]);
            recordMap.put("state", stateMap.getOrDefault(bean.getState(), ""));
            recordMap.put("imageDir", "./image/" + docId + ".jpg");
            recordMap.put("lastUpdated", String.valueOf(bean.getLastUpdated()));

//            if (bean.getImageDir() == null){
////                recordMap.put("imageDir", getXMLdirectory("static/image", docId));
//                recordMap.put("imageDir", docId);
//            }
//

            if (bean.getFee() == null){
                recordMap.put("fee", "");
            } else if (bean.getFee().equals("0")) {
                recordMap.put("fee", ModelConst.FEE);
            } else {
                recordMap.put("fee", bean.getFee());
            }

            recordMap.put("lastUpdated", String.valueOf(bean.getLastUpdated()));

            docMap.put(docId, recordMap);
        }
        return docMap;
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

            if (file.isFile() && file.getName().equals(petId + ".jpg")){
                paths = file.toString();
            }
        }
        return paths;
    }

}
