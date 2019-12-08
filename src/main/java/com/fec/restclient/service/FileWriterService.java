package com.fec.restclient.service;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


@Component
public class FileWriterService {


    String lineValue;
    PrintWriter writer;

    public void createFile(String filename){

        File tmpDir = new File(filename);

        // if file doesn't exist, create it
        if(!tmpDir.exists()) {
            try {
                this.writer = new PrintWriter(filename, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        else{
            System.out.println("File already exists");
        }

    }

    public void writeLine(String line){

        this.writer.println(line);

    }

    public void close(){

        this.writer.close();
    }

    public void getAwsAccessKey(String fileName, String keyName) {

        try {
            Files.lines(Paths.get(fileName))
                    .map(s -> s.trim())
                    .filter(s -> s.startsWith("awsAccessKey"))
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

}
