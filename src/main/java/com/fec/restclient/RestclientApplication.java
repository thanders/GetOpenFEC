package com.fec.restclient;

import com.fec.restclient.bean.ResponseObj;
import com.fec.restclient.service.RestClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class RestclientApplication {


    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(RestclientApplication.class, args);

        RestClientService request = new RestClientService();

        String key="tMI367zG8WsPfiDi1loWw8cJM1pa1JvIZWLgWh8w";

        request.setKey(key);

        // create the Webclient request object
        WebClient.RequestBodySpec reqObj = request.createRequest();

        // initiate the request
        Mono<ResponseObj> result = request.sendRequest(reqObj);

        // subscribe to the response & call the handle response method
        result.subscribe(request::handleResponse);

        //wait for a while for the response
        Thread.sleep(500);
    }

}
