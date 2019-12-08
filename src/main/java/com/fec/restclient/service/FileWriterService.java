package com.fec.restclient.service;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


@Component
public class FileWriterService {


    String lineValue;
    PrintWriter writer;
    String awsAccessLine = "awsAccessKey";

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

        //Instantiating the Scanner class to read the file
        Scanner sc = null;
        try {
            sc = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //instantiating the StringBuffer class
        StringBuffer buffer = new StringBuffer();
        //Reading lines of the file and appending them to StringBuffer
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()+System.lineSeparator());
        }
        String fileContents = buffer.toString();
        System.out.println("Contents of the file: "+fileContents);
        //closing the Scanner object
        sc.close();

        String oldLine = fileContents.lines().filter(line -> line.startsWith(this.awsAccessLine)).toString();

        String newLine = "Enjoy the free content";
        //Replacing the old line with new line
        fileContents = fileContents.replaceAll(oldLine, newLine);
        //instantiating the FileWriter class
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);

            System.out.println("");
            System.out.println("new data: "+fileContents);
            writer.append(fileContents);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
