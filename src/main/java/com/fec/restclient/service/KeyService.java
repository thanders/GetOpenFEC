package com.fec.restclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class KeyService {

    @Autowired
    FileReaderService fileReaderService;
    @Autowired
    FileWriterService fileWriterService;

    String keyName;
    String userInputKey;
    String userData = System.getenv("SNAP_USER_COMMON");
    File keyFile = new File(this.userData + "/keys.txt");
    Map<String, String> keyMap = new HashMap<>();


    public void start(String keyName, String userInputKey) {

        this.keyName = keyName;
        this.userInputKey = userInputKey;

        fileWriterService.setFileName(this.keyFile.getPath());

        // If key file already exists
        if (this.keyFile.exists()) {

            System.out.println("Keyfile already exists");
            // Read existing file
            this.keyMap = this.fileReaderService.readFile(this.keyFile);

            System.out.println("user input -  " + this.keyName + "  " + this.userInputKey);

            // PUT or REPLACE key in the key map
            if (this.keyMap.containsKey(this.keyName)) {
                this.keyMap.replace(this.keyName, this.userInputKey);
            } else {
                this.keyMap.put(this.keyName, this.userInputKey);
            }
            // Delete existing file
            fileWriterService.deleteFile(this.keyFile.getPath());

            // Create new file
            fileWriterService.createFile(this.keyFile.getPath());

            this.writeKeysToFile();

        }

        // File keyfile does not already exist
        else {

            System.out.println("Keyfile - Doesn't exist...");
            fileWriterService.createFile(this.keyFile.getPath());

            // add user inputted keys to map
            System.out.println("Your inputted values " + this.keyName + " " + this.userInputKey);
            this.keyMap.put(this.keyName, this.userInputKey);

            this.writeKeysToFile();

        }

        System.out.println("Finished writing acceskey");
        this.keyMap = this.fileReaderService.readFile(this.keyFile);
        this.keyMap.entrySet().forEach(el -> System.out.println(el));

    }

    public void writeKeysToFile() {

        System.out.println("Writing keys to file... ");
        System.out.println(this.keyMap.entrySet());

        fileWriterService.writekeys(this.keyMap);

        fileWriterService.close();

    }

    public Map<String, String> getKeys() {

        // Get latest keys from keyfile
        this.keyMap = this.fileReaderService.readFile(this.keyFile);

        return this.keyMap;
    }

    public String getKey(String keyName){
            this.keyMap = this.fileReaderService.readFile(this.keyFile);

        return this.keyMap.get(keyName);
    }
}
