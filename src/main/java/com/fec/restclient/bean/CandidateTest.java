package com.fec.restclient.bean;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "CandidateTest")
public class CandidateTest {

    String ID;
    String candidate_id;

    @DynamoDBHashKey(attributeName = "Id")
    @DynamoDBAutoGeneratedKey
    public String getID() {
        return ID;
    }
    public void setID(String ID) { this.ID = ID; }

    @DynamoDBAttribute()
    public String getCandidate_id() { return candidate_id; }
    public void setCandidate_id(String candidate_id) {
        this.candidate_id = candidate_id;
    }

}
