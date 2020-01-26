package com.fec.restclient.bean.menu;

import com.fec.restclient.bean.Candidate;
import com.fec.restclient.bean.command.CandidateDBSave;
import com.fec.restclient.bean.command.CandidateGetRequestCommand;
import com.fec.restclient.bean.command.Command;
import com.fec.restclient.configuration.DynamoDBConfig;
import com.fec.restclient.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class Menu{

    @Autowired
    RestClientService restClientService;

    @Autowired
    Menu menu;

    @Autowired
    CandidateGetRequestCommand candidateGetRequestCommand;

    @Autowired
    CandidateDBSave candidateDBSave;

    @Autowired
    DataProcessService dataProcessService;

    @Autowired
    DynamoDBConfig dynamoDBConfig;

    @Autowired
    ConsoleColors consoleColors;

    @Autowired
    FileWriterService fileWriterService;

    @Autowired
    FileReaderService fileReaderService;

    @Autowired
    KeyService keyService;

    Iterable<Candidate> candidates;

    Map<String, Command> menuItems = new HashMap();

    Map<String, String> keyMap;
    String openFECkey;
    String awsAccessKey;
    String awsSecretKey;
    String userData = System.getenv("SNAP_USER_COMMON");
    File keysFile = new File(this.userData+"/keys.txt");


    public void setCommand(String operation, Command cmd) {
        menuItems.put(operation, cmd);
    }

    public void setApiKey(String apiKey){

        System.out.println("Setting api key...");
        restClientService.setApiKey(apiKey);

    }

    public int printOptions(){
        System.out.println("\n" +

                "1. View API keys" + "\n" +
                "2. Input new OpenFEC API key" +"\n" +
                "3. Input new AWS access key" +"\n" +
                "4. Input new AWS secret key" +"\n" +
                "5. Get data from OpenFEC API and save it to AWS DynamoDB database" +"\n" +
                "6. Get descriptive information about the Candidate table" +"\n"+
                "7. Delete keylist" +"\n"+
                "8. Exit");


        Scanner input = new Scanner(System.in);

        int result = input.nextInt();

        return result;
    }

    public void chooseOption(int choice) throws IOException {

        Scanner input = new Scanner(System.in);

        if (choice == 1) {

            this.displayCurrentKeys();

            this.showMenu();

        }

        if (choice == 2) {

            System.out.println(" --- Note: This application assumes you have already setup your AWS CLI for DynamoDB ---"
                    + "\n" + consoleColors.BLUE + " Enter OpenFEC API key " + ConsoleColors.RESET);

            String openFEC = input.next();
            String keyName = "openFEC";

            this.keyService.start(keyName, openFEC);

            // Assign key
            this.openFECkey = this.keyService.getKey(keyName);
            this.setApiKey(this.openFECkey);

            this.showMenu();

        }

        if (choice == 3) {

            System.out.println("\n" + consoleColors.BLUE
                    + "Input your AWS access key for DynamoDB:"
                    + ConsoleColors.RESET);

            String inputAwsAccessKey = input.next();

            dynamoDBConfig.setAmazonAWSAccessKey(inputAwsAccessKey);
            this.awsAccessKey = inputAwsAccessKey;
            String keyName = "awsAccessKey";

            this.keyService.start(keyName, inputAwsAccessKey);

            // Assign key
            this.awsAccessKey = this.keyService.getKey(keyName);

            this.showMenu();
        }

        if (choice == 4) {

            System.out.println("\n" + consoleColors.BLUE + "Input your AWS secret key for DynamoDB:" +
                    ConsoleColors.RESET + "\n");

            String awsSecretKey = input.next();
            String keyname = "awsSecretKey";
            this.keyService.start(keyname, awsSecretKey);

            // Get latest keymap
            this.keyMap = this.keyService.getKeys();

            // Assign key
            this.awsSecretKey = this.keyMap.get(keyname);

            dynamoDBConfig.setAmazonAWSSecretKey(this.awsSecretKey);

            this.showMenu();

        }

        if (choice == 5) {

            if (this.apiKeyCount() == 3) {

                System.out.println("-----------  Starting  --------");
                dataProcessService.setAwsAccessKey(this.awsAccessKey);
                dataProcessService.setAwsSecretKey(this.awsSecretKey);

                this.candidates = candidateGetRequestCommand.executeCandidateGetRequest();

                candidateDBSave.executeCandidateDBSave(candidates);

                this.showMenu();
            } else {
                System.out.println("Make sure all three required keys have been entered");
                this.showMenu();
            }
        }

        if (choice == 6) {

            if (candidates != null) {
                // Print out values openFEC api data
                candidates.forEach(candidate -> {
                    System.out.println(candidate.getCandidate_id() + " " + candidate.getName());
                });

                Long itemCount = dataProcessService.getAmazonDynamoDB().describeTable("Candidate").getTable().getItemCount();
                System.out.println("Rows in Candidate table " + itemCount + " -- only updated every six hours");
                this.showMenu();
            } else {
                System.out.println("\n" + "Candidates iterator is null, make request first before trying to view it" + "\n");
                this.showMenu();
            }
        }

        if (choice == 7) {

            // Create the file path
            String userData= System.getenv("SNAP_USER_COMMON");

            File keysFile = new File(userData+"/keys.txt");

            this.keysFile = keysFile;

            fileWriterService.deleteFile(this.keysFile.getPath());
            this.showMenu();
        }

        if (choice == 8) {
            System.out.println("Thanks for using GetOpenFEC. Goodbye.");
            System.exit(0);
        }

    }



    public void showMenu() throws IOException {
        int result = this.printOptions();
        this.chooseOption(result);
    }

    public void displayCurrentKeys() {

        if(this.keysFile.exists()){
            this.keyMap = this.fileReaderService.readFile(this.keysFile);
            System.out.println("Whoo");

            this.keyMap.entrySet().forEach(el -> System.out.println(el));

            System.out.println();

            // If OpenFEC API Key exists:
            if(this.keyMap.containsKey("openFEC")) {
                System.out.printf("%-10s %16s\n", "OpenFEC", "\u2713");
            }
            else{
                System.out.printf("%-10s %10s %15s\n", "OpenFEC", ConsoleColors.RED, "key required" + ConsoleColors.RESET);
            }

            // If awsAccessKey exists:
            if(this.keyMap.containsKey("awsAccessKey")) {
                System.out.printf("%-10s  %15s\n", "AWS access", "\u2713");
            }
            else {
                System.out.printf("%-10s %10s %15s\n", "AWS access", ConsoleColors.RED, "key required" + ConsoleColors.RESET);
            }

            // If AWS Secret key exists:
            if(this.keyMap.containsKey("awsSecretKey")) {
                System.out.printf("%-10s  %15s\n", "AWS Secret", "\u2713");
            }
            else {
                System.out.printf("%-10s  %9s %15s\n", "AWS Secret", ConsoleColors.RED, "key required" + ConsoleColors.RESET);
            }
        }

        else{
            System.out.println("You have not entered any keys, file doesn't exist");
        }
    }

    public int apiKeyCount(){

        this.openFECkey = null;
        this.awsAccessKey = null;
        this.awsSecretKey = null;

        int count = 0;

        if (restClientService.getApiKey().length() != 0) {
            openFECkey = restClientService.getApiKey();
            count++;
        }

        if (dynamoDBConfig.getAmazonAWSAccessKey().length() != 0) {
            awsAccessKey = dynamoDBConfig.getAmazonAWSAccessKey();
            count++;
        }

        if (dynamoDBConfig.getAmazonAWSSecretKey().length() != 0) {
            awsSecretKey = dynamoDBConfig.getAmazonAWSSecretKey();
            count++;
        }

        return count;
    }
}
