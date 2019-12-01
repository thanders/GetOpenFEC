package com.fec.restclient.bean.command;

/*
 * Concrete command to create file
 */

import com.fec.restclient.service.RestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateCommand implements Command{

    @Autowired
    RestClientService restClientService;

    String apiKey;

    @Override
    public void execute() {
        System.out.println("Setting the API key");

    }

}
