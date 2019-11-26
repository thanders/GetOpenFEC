package com.fec.restclient.service;

import com.fec.restclient.bean.Candidate;
import com.fec.restclient.bean.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserInputService {

    @Autowired
    DataProcessService dataProcessService;

    @Autowired
    RestClientService restClientService;

    @Autowired
    Candidate candidate;

    public void establishDynamoDBConnection() {

        dataProcessService.setTableName(Candidate.class);
        dataProcessService.connectToDB();
    }

    public void createTable() {

        dataProcessService.instantiateMapper();
        // Create a DynamoDB table creation request for the desired object type
        dataProcessService.createTableRequest(candidate);

        try {
            dataProcessService.createTable();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Iterable<Candidate> getCandidates() {

        // create the Webclient request object
        WebClient.RequestBodySpec reqObj = restClientService.createRequest();

        // initiate the request
        System.out.println("--- Initiating request WebClient request for openFEC API data");
        Mono<ResponseObj> result = restClientService.sendRequest(reqObj);

        System.out.println("--- Webclient request finished");

        Iterable<Candidate> candidates = result.flatMapIterable(ResponseObj::getResults).toIterable();

        return candidates;
    }

    public void setApiKey(String apiKey){

        restClientService.setApiKey(apiKey);
    }

}
