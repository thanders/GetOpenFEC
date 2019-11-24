package com.fec.restclient.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.fec.restclient.bean.Candidate;
import org.springframework.stereotype.Service;

@Service
public class DataProcessService {

    AmazonDynamoDB amazonDynamoDB;

    public boolean tableExists(AmazonDynamoDB amazonDynamoDB, String tableName){

        try{ DescribeTableResult json = amazonDynamoDB.describeTable(tableName);
            System.out.println(json.getTable().getAttributeDefinitions());}

        catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Table does not exist");
            return false;
        }
        System.out.println("Table exists");

        return true;
    }


    // Setters
    public void setAmazonDynamoDB(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }
}
