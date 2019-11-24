package com.fec.restclient.bean;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseObj {

    private String api_version;
    private Pagination pagination;
    private List<Candidate> results;

    public ResponseObj() {
        // Default constructor is required by AWS DynamoDB SDK
    }

    public String getApi_version() {
        return api_version;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public List<Candidate> getResults() {
        return results;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }


    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }


    public void setResults(List<Candidate> results) {
        this.results = results;
    }
}
