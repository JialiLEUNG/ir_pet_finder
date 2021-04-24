package com.ir.dataPreProcess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashSet;


/**
 * StopwordRemover loads and stores the stop words from the fileinputstream
 * See the address of stoplist.txt at Path.StopwordDir
 */
public class StopwordRemover {
    private BufferedReader bufferedReader;
    private HashSet<String> stopwordSet = null;

    public StopwordRemover() throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        String path  = classLoader.getResource("static/csv/stoplist.txt").getPath();

        FileInputStream fileinputstream = new FileInputStream(path);
        bufferedReader = new BufferedReader(new InputStreamReader(fileinputstream));
        String stopword = "";
        // not sure if the stoplist is a set, so build a hashset for it.
        stopwordSet = new HashSet<>();
        while ((stopword = bufferedReader.readLine()) != null){
            // TODO: Use stemmer to transform the stopword into its root form
            stopwordSet.add(stopword.trim());
        }
    }


    public boolean isStopword(String word) throws IOException{
        return stopwordSet.contains(word.trim());
    }


}
