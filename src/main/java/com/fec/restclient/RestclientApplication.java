package com.fec.restclient;

import com.fec.restclient.bean.command.CreateCommand;
import com.fec.restclient.bean.command.DeleteCommand;
import com.fec.restclient.bean.menu.Menu;
import com.fec.restclient.service.DataProcessService;
import com.fec.restclient.service.RestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class RestclientApplication {

    @Autowired
    RestClientService restClientService;

    @Autowired
    DataProcessService dataProcessService;

    @Value("${openfec}")
    String openFECkey;

    @Autowired
    Menu menu;

    @Autowired
    CreateCommand createCommand;

    @PostConstruct
    public void startApplication() {

        System.out.println("\n"+ "--- OpenFEC RestClient application ---"+ "\n" + "Note: This application requires API keys for OpenFEC and AWS Dynamodb (Access and Secret)"+ "\n");

        // Client creates Invoker object, command object and configure them

        menu.setCommand("Create", createCommand);
        menu.setCommand("SetOpenFecKey", createCommand);
        menu.setCommand("CandidateGetRequest", createCommand);

        //Invoker invokes command
        menu.displayCurrentKeys();
        int result = menu.printOptions();

        menu.chooseOption(result);

    }

    public static void main(String[] args) throws InterruptedException {

        // Start the spring application (application context)
        SpringApplication.run(RestclientApplication.class, args);

    }

}
