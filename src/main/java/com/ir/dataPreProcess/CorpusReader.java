package com.ir.dataPreProcess;

import com.ir.classes.FileConst;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CorpusReader {

    private BufferedReader bufferedReader;

    public CorpusReader() throws IOException {
        FilePathGenerator fpg = new FilePathGenerator("result.txt");
        String path = fpg.getPath();
        System.out.println("path of result.txt in Corpus Reader: " + path);
        InputStream inputStream = new FileInputStream(path); // Path.ResultAssignment1
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * make a <"DOC_ID", doc_id> and a <"CONTENT", content> map.
     * @return the map
     * @throws IOException
     */

    public Map<String, String> NextDoc() throws IOException {
        Map<String, String> docMap = new HashMap<>();

        String doc_id = "";
        String doc_content = "";

        if ((doc_id = bufferedReader.readLine()) != null){
            docMap.put("DOC_ID", doc_id);
            for (String field_name : FileConst.fields){
//                if (field_name.equals("Colors")){
//                    String color1 = bufferedReader.readLine();
//                    String color2 = bufferedReader.readLine();
//                    String color3 = bufferedReader.readLine();
//                    doc_content = color1 + "," + color2 + "," + color3;
//                }
//                else if (field_name.equals("Breeds")){
//                    String breed1 = bufferedReader.readLine();
//                    String breed2 = bufferedReader.readLine();
//                    doc_content = breed1 + "," + breed2;
//                }
//                else {
//                    doc_content = bufferedReader.readLine();
//                }
                doc_content = bufferedReader.readLine();
                docMap.put(field_name, doc_content);
            }
            return docMap;
        }

        if (bufferedReader != null){
            bufferedReader.close();
        }
        return null;
    }
}

