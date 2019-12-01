package com.fec.restclient.bean.command;

/*
 * Concrete command to create file
 */

import com.fec.restclient.bean.Candidate;
import com.fec.restclient.bean.ResponseObj;
import com.fec.restclient.service.DataProcessService;
import com.fec.restclient.service.RestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CandidateGetRequestCommand implements Command{

    @Autowired
    RestClientService restClientService;

    @Autowired
    DataProcessService dataProcessService;

    @Autowired
    Candidate candidate;

    public Iterable<Candidate> executeCandidateGetRequest() {

        dataProcessService.setTableName(Candidate.class);
        dataProcessService.connectToDB();

        dataProcessService.instantiateMapper();
        // Create a DynamoDB table creation request for the desired object type
        dataProcessService.createTableRequest(candidate);

        try {
            dataProcessService.createTable();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // create the Webclient request object
        WebClient.RequestBodySpec reqObj = restClientService.createRequest();

        // initiate the request
        System.out.println("--- Initiating request WebClient request for openFEC API data");
        Mono<ResponseObj> result = restClientService.sendRequest(reqObj);

        System.out.println("--- Webclient request finished");

        Iterable<Candidate> candidates = result.flatMapIterable(ResponseObj::getResults).toIterable();

        return candidates;
    }

    @Override
    public void execute() {

    }
}
