package com.fec.restclient.bean.menu;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fec.restclient.bean.Candidate;
import com.fec.restclient.bean.command.CandidateDBSave;
import com.fec.restclient.bean.command.CandidateGetRequestCommand;
import com.fec.restclient.bean.command.Command;
import com.fec.restclient.configuration.DynamoDBConfig;
import com.fec.restclient.service.DataProcessService;
import com.fec.restclient.service.RestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    Iterable<Candidate> candidates;


    Map<String, Command> menuItems = new HashMap();

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

                "1. View existing API key" + "\n" +
                "2. Add new OpenFEC API key" +"\n" +
                "3. Add AWS accesskey" +"\n" +
                "4. Add AWS secretkey" +"\n" +
                "5. Get data from OpenFEC API and save it to AWS DynamoDB database" +"\n" +
                "6. Get descriptive information about the Candidate table" +"\n"+
                "7. Exit");


        Scanner input = new Scanner(System.in);

        int result = input.nextInt();

        return result;
    }

    public void chooseOption(int choice){

        Scanner input = new Scanner(System.in);

        if (choice == 1) {

            System.out.println("Current OpenFEC API key:" + "\n" + restClientService.getApiKey());
            this.showMenu();

        }

        if (choice == 2) {

            System.out.println(" --- Note: This application assumes you have already setup your AWS CLI for DynamoDB --- \n Enter OpenFEC API key ");

            String apiKey = input.next();

            this.setApiKey(apiKey);

            this.showMenu();
            //menu.runCommand("Create");

        }

        if (choice == 3) {

            System.out.println("\n" + "Input your AWS access key for DynamoDB:");

            String awsAccessKey = input.next();
            dynamoDBConfig.setAmazonAWSAccessKey(awsAccessKey);

            this.showMenu();

        }

        if (choice == 4) {

            System.out.println("\n" + "Input your AWS secret key for DynamoDB:" + "\n" + restClientService.getApiKey());

            String awsSecretKey = input.next();
            dynamoDBConfig.setAmazonAWSSecretKey(awsSecretKey);

            this.showMenu();

        }

        if(choice == 5){
            System.out.println("-----------  Starting  --------");

            this.candidates = candidateGetRequestCommand.executeCandidateGetRequest();

            candidateDBSave.executeCandidateDBSave(candidates);

            this.showMenu();

        }

        if(choice == 6){

            // Print out values openFEC api data
            candidates.forEach(candidate -> { System.out.println(candidate.getCandidate_id() + " " +candidate.getName()); });

            Long itemCount = dataProcessService.getAmazonDynamoDB().describeTable("Candidate").getTable().getItemCount();
            System.out.println("Rows in Candidate table " + itemCount + " -- only updated every six hours");
            this.showMenu();
        }

        if (choice == 7) {
            System.out.println("Thanks for using GetOpenFEC. Goodbye.");
            System.exit(0);
        }
    }

    public void showMenu(){
        int result = this.printOptions();
        this.chooseOption(result);
    }

}
