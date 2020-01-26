package com.fec.restclient.service;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FileWriterService {


    String lineValue;
    PrintWriter writer;
    String awsAccessLine = "awsAccessKey";
    String fileName;

    public boolean createFile(String filename){

        System.out.println("Createfile - Checking filename...");
        File tmpDir = new File(filename);

        // if file doesn't exist, create it
        if(!tmpDir.exists()) {
            try {
                System.out.println("New file location "+filename);
                this.writer = new PrintWriter(filename, "UTF-8");
                this.fileName = filename;
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        else{
            System.out.println("File already exists");
            System.out.println("Location: "+ filename);
            this.fileName = filename;
            return false;

        }
        return false;
    }

    public void writekeys(Map keys){

        // Write to file
        try {
            FileWriter fileWriter = new FileWriter(this.fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            keys.forEach((k, v) -> printWriter.println(k +"," +v));

            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLine(String line) throws IOException {
        System.out.println("the line: "+ line);
        System.out.println("blank? "+ this.fileName);
        File tempFile = new File(this.fileName);
        boolean exists = tempFile.exists();
        System.out.println("exists? " + exists);

        FileWriter fwriter = new FileWriter(this.fileName);

        System.out.println("Writing to file...");
        this.writer = new PrintWriter(fwriter);
        System.out.println(this.writer.getClass());
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
                    .filter(s -> s.startsWith(keyName))
                    .forEach(keyList::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("WHERE IS THIS?");
        if (keyList.size()== 0){
            System.out.println("key file is empty");
        }
        else {
            return keyList.get(0).toString();
        }
        return "empty file";
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

    public void replaceLine(String keyName, String fileName, String keyValue){

        try {
            BufferedReader file = new BufferedReader(new FileReader(fileName));

            String line;
            String input = "";
            int count = 0;

            while ((line = file.readLine()) != null) {
                System.out.println("replace?");
                System.out.println("line: "+line);
                if (line.startsWith(keyName)) {
                    System.out.println("Replacing content...");
                    line = line.replace(line, "replaced content");
                }
                input += line + "\n";
                ++count;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public List<String> fileToArray(){
        List<String> result = null;

        System.out.println("filename set: "+ this.fileName);
        File file = new File(this.fileName);

        if(file.exists()) {
            System.out.println("File exists...");
            try {
                result = Files.readAllLines(Paths.get(this.fileName));
                if(result.size()== 0){
                    System.out.println("size was zero deleting...");
                    this.deleteFile(this.fileName);
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("File does not exist...");
        }
        return result;
    }

    public void deleteFile(String fileName){

        File tmpDir = new File(fileName);
        if (tmpDir.exists()){
            System.out.println("exists...");
            tmpDir.delete();
        }
        else{
            System.out.println("Doesn't exist");
        }
        System.out.println("File deleted...");
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
