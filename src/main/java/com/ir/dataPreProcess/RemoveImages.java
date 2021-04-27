package com.ir.dataPreProcess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data clean image files:
 * https://www.kaggle.com/c/petfinder-adoption-prediction/data
 */
public class RemoveImages {

    public RemoveImages() {
    }

    public List<String> getXMLdirectory(String folderName){
        // "static/csv" == folderName
        ClassLoader classLoader = this.getClass().getClassLoader();
        String path  = Objects.requireNonNull(classLoader.getResource(folderName)).getPath();
        File dir = new File(path); //Directory where xml file exists
        File[] files = dir.listFiles();
        List<String> paths = new ArrayList<>();


        for(File file : files) {
            // You can validate file name with extension if needed
            if(file.isFile() && file.getName().endsWith(".jpg")) {
                paths.add(file.toString());
            }
        }
        return paths;
    }

    public static void main(String[] args) throws IOException {
//        String[] fileDirs = new String[10];
        RemoveImages removeImages = new RemoveImages();
        List<String> fileDirs =  removeImages.getXMLdirectory("static/image");
        System.out.println("number of files: " + fileDirs.size());
        int count = 0;
        for (String fileDir : fileDirs){
            System.out.println("fileDir: " + fileDir);
            String[] myArray = fileDir.split("-");
            if (myArray.length <= 1) continue;
            File file = new File(fileDir); // File (or directory) with old name
            System.out.println("myArray[1]: " + myArray[1]);
            if (myArray[1] != null && !myArray[1].equals("2.jpg")){
                if(file.delete()) {
                    count++;
//                    System.out.println("File deleted successfully");
                }
                else {
                    System.out.println("Failed to delete the file");
                }
            }
            else {
                // It's the right file, but has to be renamed.
                // File (or directory) with new name
                File file2 = new File(myArray[0] + ".jpg");
                if (file2.exists())
                    continue;
//                    throw new IOException("file exists");

                // Rename file (or directory)
                boolean success = file.renameTo(file2);

                if (!success) {
                    System.out.println("File was not successfully renamed");
                }
            }
        }
        System.out.println("files delete: " + count);

    }
}
