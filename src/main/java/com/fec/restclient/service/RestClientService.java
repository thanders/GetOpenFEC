package com.fec.restclient.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.fec.restclient.bean.Candidate;
import com.fec.restclient.bean.ResponseObj;
import com.fec.restclient.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;


@Service
public class RestClientService {

   private String key;

    @Autowired
    private CandidateRepository repository;

    @Value("${openfec}")
    private String testKey;


    public WebClient.RequestBodySpec createRequest(){


        WebClient requestClient = WebClient
                .builder()
                .baseUrl("https://api.open.fec.gov")
                //.defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .build();

        System.out.println("YOUR API KEY: "+ this.key);
        WebClient.RequestBodySpec request = requestClient
                .method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/candidates/")
                        .queryParam("is_active_candidate", "true")
                        .queryParam("sort_hide_null", "true")
                        .queryParam("page", "1")
                        .queryParam("sort_nulls_last", "false")
                        .queryParam("sort_null_only", "false")
                        .queryParam("per_page", "100")
                        .queryParam("sort", "name")
                        .queryParam("election_year", "2020")
                        .queryParam("candidate_status", "C")
                        .queryParam("office", "P")
                        .queryParam("api_key", this.key)
                        .build());

        return request;

    }

    public Mono<ResponseObj> sendRequest(WebClient.RequestBodySpec request){

        Mono<ResponseObj> candidateFlux = request.retrieve().bodyToMono(ResponseObj.class).log();

        return candidateFlux;
    }

    public void handleResponse(ResponseObj responseObj) {

        System.out.println("handle response");
        List<Candidate> ts = responseObj.getResults();

        /*
        for (Candidate t : ts){

            System.out.println(t.getName() + "\n" + t.getParty_full() +"\n" + t.getOffice_full() +"\n" );
        }
*/
        System.out.println(ts.get(0).getCandidate_id());
        System.out.println(ts.get(0).getClass());

        //this.repository.save(ts.get(0));
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean tableExists(AmazonDynamoDB amazonDynamoDB, String tableName){


        DescribeTableResult json = amazonDynamoDB.describeTable(tableName);

        return true;
    }
}
