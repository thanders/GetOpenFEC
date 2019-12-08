package com.fec.restclient.bean.menu;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fec.restclient.bean.Candidate;
import com.fec.restclient.bean.command.CandidateDBSave;
import com.fec.restclient.bean.command.CandidateGetRequestCommand;
import com.fec.restclient.bean.command.Command;
import com.fec.restclient.configuration.DynamoDBConfig;
import com.fec.restclient.service.DataProcessService;
import com.fec.restclient.service.FileWriterService;
import com.fec.restclient.service.RestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * Invoker class, which holds command object and invokes method
 */




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

    Iterable<Candidate> candidates;

    Map<String, Command> menuItems = new HashMap();

    String openFECkey;
    String awsAccessKey;
    String awsSecretKey;

    public void setCommand(String operation, Command cmd) {
        menuItems.put(operation, cmd);
    }

    public void runCommand(String operation){

        menuItems.get(operation).execute();
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
                "7. Exit" +"\n"+
                "8. Get Environment Variable");


        Scanner input = new Scanner(System.in);

        int result = input.nextInt();

        return result;
    }

    public void chooseOption(int choice) {

        Scanner input = new Scanner(System.in);

        if (choice == 1) {

            this.displayCurrentKeys();

            this.showMenu();

        }

        if (choice == 2) {

            System.out.println(" --- Note: This application assumes you have already setup your AWS CLI for DynamoDB ---"
                    + "\n" + consoleColors.BLUE + " Enter OpenFEC API key " + ConsoleColors.RESET);

            String apiKey = input.next();

            this.setApiKey(apiKey);
            this.openFECkey = apiKey;
            this.showMenu();
            //menu.runCommand("Create");

        }

        if (choice == 3) {

            System.out.println("\n" + consoleColors.BLUE
                    + "Input your AWS access key for DynamoDB:"
                    + ConsoleColors.RESET);

            String awsAccessKey = input.next();
            dynamoDBConfig.setAmazonAWSAccessKey(awsAccessKey);
            this.awsAccessKey = awsAccessKey;


            String userData= System.getenv("SNAP_USER_DATA");
            String keysFile= userData+"/keys.txt";
            fileWriterService.createFile(keysFile);
            fileWriterService.writeLine("awsAccessKey"+","+ awsAccessKey);
            fileWriterService.close();

            fileWriterService.getAwsAccessKey(keysFile, "awsAccessKey");

            this.showMenu();

        }

        if (choice == 4) {

            System.out.println("\n" + consoleColors.BLUE + "Input your AWS secret key for DynamoDB:" +
                    ConsoleColors.RESET + "\n");

            String awsSecretKey = input.next();
            dynamoDBConfig.setAmazonAWSSecretKey(awsSecretKey);
            this.awsSecretKey = awsSecretKey;

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
            System.out.println("Thanks for using GetOpenFEC. Goodbye.");
            System.exit(0);
        }

        if (choice == 8) {

            String myKey="testAwsAccess";
            String mySecretKey ="testAwsSecret";

            System.out.println("Environment variables:");
            System.out.println(System.getenv("SNAP_USER_DATA"));
            System.out.println(System.getenv("JAVA_HOME"));
            System.out.println(System.getenv("SNAP_USER_COMMON"));

            String userData= System.getenv("SNAP_USER_DATA");

            fileWriterService.createFile(userData+"/keys.txt");
            fileWriterService.writeLine("awsAccessKey"+","+ myKey);
            fileWriterService.writeLine("awsSecretKey"+","+ mySecretKey);
            fileWriterService.close();

            fileWriterService.readFile(userData+"/keys.txt");

            this.showMenu();

        }
    }



    public void showMenu(){
        int result = this.printOptions();
        this.chooseOption(result);
    }

    public void displayCurrentKeys() {

        System.out.println();
        // AWS Secret key
        if (this.openFECkey != null && this.openFECkey.length() > 2) {
            System.out.printf("%-10s %15s\n", "OpenFEC", "***" + this.openFECkey.substring(this.openFECkey.length() - 2));
        }
        else{
            System.out.printf("%-10s %10s %15s\n", "OpenFEC", ConsoleColors.RED, "key required" + ConsoleColors.RESET);
        }
        // AWS Access key
        if (this.awsAccessKey != null && this.awsAccessKey.length() > 2) {
            System.out.printf("%-10s  %15s\n", "AWS access", "***" + this.awsAccessKey.substring(this.awsAccessKey.length() - 2));
        } else {
            System.out.printf("%-10s %10s %15s\n", "AWS access", ConsoleColors.RED, "key required" + ConsoleColors.RESET);
        }

        // AWS Secret key
        if (this.awsSecretKey != null && this.awsSecretKey.length() > 2) {
            System.out.printf("%-10s %15s\n", "AWS secret", "***" + this.awsSecretKey.substring(this.awsSecretKey.length() - 2));
        } else {
            System.out.printf("%-10s  %9s %15s\n", "AWS secret", ConsoleColors.RED, "key required" + ConsoleColors.RESET);
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
