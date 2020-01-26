package com.fec.restclient.service;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Component
public class FileReaderService {

    public Map<String, String> readFile(File keysFile) {

        Map<String, String> keys = new HashMap<>();

        // read lines, create a key value pair for each line
        try {
            keys = Files.lines(Paths.get(keysFile.getPath()))
                    .map(line -> line.split(","))
                    .collect(toMap(line -> line[0], line -> line[1]));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return keys;



    }
}
