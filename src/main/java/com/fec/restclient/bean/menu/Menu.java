package com.fec.restclient.bean.menu;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fec.restclient.bean.Candidate;
import com.fec.restclient.bean.command.CandidateDBSave;
import com.fec.restclient.bean.command.CandidateGetRequestCommand;
import com.fec.restclient.bean.command.Command;
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
                "1. Add new OpenFEC API key" + "\n" +
                "2. View existing API key" +"\n" +
                "3. Get data from OpenFEC API and save it to AWS DynamoDB database" +"\n" +
                "4. Get descriptive information about the Candidate table" +"\n"+
                "5. Exit");


        Scanner input = new Scanner(System.in);

        int result = input.nextInt();

        return result;
    }

    public void chooseOption(int choice){

        Scanner input = new Scanner(System.in);

        if (choice == 1) {
            System.out.println(" --- Note: This application assumes you have already setup your AWS CLI for DynamoDB --- \n Enter OpenFEC API key ");

            String apiKey = input.next();

            this.setApiKey(apiKey);

            this.showMenu();
            //menu.runCommand("Create");
        }

        if (choice == 2) {
            System.out.println("Current OpenFEC API key:" + "\n" + restClientService.getApiKey());
            this.showMenu();
        }

        if(choice == 3){
            System.out.println("-----------  Starting  --------");

            this.candidates = candidateGetRequestCommand.executeCandidateGetRequest();

            candidateDBSave.executeCandidateDBSave(candidates);

            this.showMenu();

        }

        if(choice == 4){

            // Print out values openFEC api data
            candidates.forEach(candidate -> { System.out.println(candidate.getCandidate_id() + " " +candidate.getName()); });

            Long itemCount = dataProcessService.getAmazonDynamoDB().describeTable("Candidate").getTable().getItemCount();
            System.out.println("Rows in Candidate table " + itemCount + " -- only updated every six hours");
            this.showMenu();
        }

        if (choice == 5) {
            System.out.println("Thanks for using OpenFEC RestClient");
            System.exit(0);
        }
    }

    public void showMenu(){
        int result = this.printOptions();
        this.chooseOption(result);
    }

}
