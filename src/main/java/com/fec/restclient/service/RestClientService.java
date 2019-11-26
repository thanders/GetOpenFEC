package com.fec.restclient.service;

import com.fec.restclient.bean.Candidate;
import com.fec.restclient.bean.ResponseObj;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@Configuration
public class RestClientService {

    @Value("${openfec}")
    private String apiKey;

    List<Candidate> candidates;

    public WebClient.RequestBodySpec createRequest(){

        WebClient requestClient = WebClient
                .builder()
                .baseUrl("https://api.open.fec.gov")
                //.defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .build();

        System.out.println("your API Key   " + apiKey);
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
                        .queryParam("api_key", this.apiKey)
                        .build());

        return request;

    }

    public Mono<ResponseObj> sendRequest(WebClient.RequestBodySpec request){

        Mono<ResponseObj> candidateMono = request.retrieve().bodyToMono(ResponseObj.class).log();

        return candidateMono;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
