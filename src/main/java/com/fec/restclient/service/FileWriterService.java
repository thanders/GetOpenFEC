package com.fec.restclient.service;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class FileWriterService {


    String lineValue;
    PrintWriter writer;

    public boolean createFile(String filename){

        File tmpDir = new File(filename);

        // if file doesn't exist, create it
        if(!tmpDir.exists()) {
            try {
                this.writer = new PrintWriter(filename, "UTF-8");
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        else{
            System.out.println("File already exists");

        }
        return false;
    }

    public void writeLine(String line){

        this.writer.println(line);

    }

    public void close(){

        this.writer.close();
    }

    public String getAwsAccessKey(String fileName, String keyName) {

        ArrayList keyList = new ArrayList();

        try {
            Files.lines(Paths.get(fileName))
                    .map(s -> s.trim())
                    .filter(s -> s.startsWith("awsAccessKey"))
                    .forEach(keyList::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(keyList.get(0).toString());
        return keyList.get(0).toString();

    }

        public void readFile(String fileName){


        System.out.println("Printing lines which starts with" );
        try {
            Files.lines(Paths.get(fileName))
                    .map(s -> s.trim())
                    .filter(s -> s.startsWith("aws"))
                            .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> replaceLine(String keyName, String fileName){

        /*
        try {
            Files.lines(Paths.get(fileName))
                    .map(line -> line.trim())
                    .filter(line -> line.startsWith("aws"))
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        String replaceString = "dingle";

        try {
            return Files.lines(Paths.get(fileName)) //Get each line from source file

                    //in this .map for each line check if it starts with new orders. if it does then replace that with our String
                    .map(line -> {if(line.startsWith(keyName )){
                        return replaceString;
                    } else {
                        return line;
                    }
                    } )

                    //peek to print values in console. This can be removed after testing
                    .peek(System.out::println)
                    //finally put everything in a collection and send it back
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
