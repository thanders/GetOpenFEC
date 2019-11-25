package com.fec.restclient;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fec.restclient.bean.Candidate;
import com.fec.restclient.bean.ResponseObj;

import com.fec.restclient.service.DataProcessService;
import com.fec.restclient.service.RestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Scanner;


@SpringBootApplication
public class RestclientApplication {

    @Autowired
    DataProcessService dataProcessService;

    @Value("${openfec}")
    String key;

    @PostConstruct
    public void startApplication() {

        System.out.println("--- OpenFEC restClient application ---"+ "\n");

        System.out.println("Options" + "\n" +
                "1. Input new OpenFEC API key" + "\n" +
                "2. Proceed to request OpenFEC data" + "\n" +
                "3. Exit");

        Scanner input = new Scanner(System.in);

        int router = input.nextInt();

        if (router == 1) {
            System.out.println("Enter OpenFEC API key \n --- Note: This application assumes you have already setup your AWS CLI for DynamoDB ---");

            String openFEC = input.nextLine();
        }

        if (router == 2) {
            System.out.println("-----------  Starting  --------");
            dataProcessService.setTableName(Candidate.class);
            dataProcessService.connect();
            dataProcessService.instantiateMapper();

            try {
                dataProcessService.createTable();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // For making a request to the OpenFEC API
            RestClientService request = new RestClientService();

            request.setKey(key);

            // create the Webclient request object
            WebClient.RequestBodySpec reqObj = request.createRequest();

            // initiate the request
            System.out.println("--- Initiating request WebClient request for openFEC API data");
            Mono<ResponseObj> result = request.sendRequest(reqObj);
            System.out.println("--- Webclient request finished");

            Iterable<Candidate> candidates = result.flatMapIterable(ResponseObj::getResults).toIterable();

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
    }

    public static void main(String[] args) throws InterruptedException {

        // Start the spring application (application context)
        SpringApplication.run(RestclientApplication.class, args);

    }

}
