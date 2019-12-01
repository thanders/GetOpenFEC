package com.fec.restclient.bean.command;

/*
 * Concrete command to create file
 */

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fec.restclient.bean.Candidate;
import com.fec.restclient.service.DataProcessService;
import com.fec.restclient.service.RestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CandidateDBSave implements Command{

    @Autowired
    RestClientService restClientService;

    @Autowired
    DataProcessService dataProcessService;


    public void executeCandidateDBSave(Iterable<Candidate> candidates) {

        System.out.println("--- Starting iterable<candidate> batchsave to AWS DynamoDB ---");
        List<DynamoDBMapper.FailedBatch> batchSaveStatus = dataProcessService.getDynamoDBMapper().batchSave(candidates);

        if (batchSaveStatus.isEmpty()) {
            System.out.println("--- Finished iterable<candidate> batchsave to AWS DynamoDB ---");
        }

    }

    @Override
    public void execute() {

    }
}
