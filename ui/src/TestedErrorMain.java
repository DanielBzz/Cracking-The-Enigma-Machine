

import java.io.File;
import java.util.Arrays;

public class TestedErrorMain {

    public static void main(String[] args) {

        // Creates an array in which we will store the names of files and directories
        String[] fileNames;

        // Creates a new File instance by converting the given pathname string

        String folderPath="C:\\Users\\DanielBazis\\IdeaProjects\\CarckingTheEnigma\\resources\\TestErrors\\Error Files\\";//replace with your own folder path

        File f = new File(folderPath);
        // Populates the array with names of files and directories
        fileNames=f.list();
        assert fileNames != null;
        Arrays.sort(fileNames);


        EnigmaSystemEngine engine = new EnigmaEngine();//enter your implement
        int i=0;
        // For each pathname in the pathnames array

        for (String fileName : fileNames) {
            try{
                System.out.println((++i) +" # "+fileName+":");

                engine.loadXmlFile(folderPath+fileName);//enter your implement
            } catch (Exception | Error e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
