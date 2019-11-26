package com.fec.restclient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fec.restclient.bean.Candidate;

import com.fec.restclient.service.DataProcessService;
import com.fec.restclient.service.RestClientService;
import com.fec.restclient.service.UserInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Scanner;


@SpringBootApplication
public class RestclientApplication {

    @Autowired
    UserInputService userInputService;

    @Autowired
    RestClientService restClientService;

    @Autowired
    DataProcessService dataProcessService;

    @Value("${openfec}")
    String openFECkey;

    @PostConstruct
    public void startApplication() {

        System.out.println("--- OpenFEC RestClient application ---"+ "\n");

        System.out.println("Options" + "\n" +
                "1. Input new OpenFEC API key" + "\n" +
                "2. Proceed to request OpenFEC data" + "\n" +
                "3. Exit");

        Scanner input = new Scanner(System.in);

        int router = input.nextInt();

        if (router == 1) {
            System.out.println(" --- Note: This application assumes you have already setup your AWS CLI for DynamoDB --- \n Enter OpenFEC API key ");

            String apiKey = input.next();

            userInputService.setApiKey(apiKey);
        }

        if (router == 2) {

        }

        if (router == 3) {
            System.out.println("Thanks for using OpenFEC RestClient");
            System.exit(0);
        }


        System.out.println("-----------  Starting  --------");

        userInputService.establishDynamoDBConnection();
        userInputService.createTable();

        Iterable<Candidate> candidates = userInputService.getCandidates();

        // Print out values openFEC api data
        candidates.forEach(candidate -> { System.out.println(candidate.getCandidate_id() + " " +candidate.getName()); });

        System.out.println("--- Starting iterable<candidate> batchsave to AWS DynamoDB ---");
        List<DynamoDBMapper.FailedBatch> batchSaveStatus = dataProcessService.getDynamoDBMapper().batchSave(candidates);

        if (batchSaveStatus.isEmpty()) {
            System.out.println("--- Finished iterable<candidate> batchsave to AWS DynamoDB ---");
        }

        Long itemCount = dataProcessService.getAmazonDynamoDB().describeTable("Candidate").getTable().getItemCount();
        System.out.println("Rows in Candidate table " + itemCount + " -- only updated every six hours");
    }

    public static void main(String[] args) throws InterruptedException {

        // Start the spring application (application context)
        SpringApplication.run(RestclientApplication.class, args);

    }

}
