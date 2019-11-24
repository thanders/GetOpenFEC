package com.fec.restclient;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.fec.restclient.bean.Candidate;

import com.fec.restclient.bean.CandidateTest;
import com.fec.restclient.repository.CandidateRepository;
import com.fec.restclient.repository.CandidateTestRepository;
import com.fec.restclient.service.DataProcessService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RestclientApplication.class)
@WebAppConfiguration
@ActiveProfiles("local")

public class DatabasePersistenceTest {

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private DataProcessService dataProcessService;


    @Autowired
    CandidateRepository repository;

    private static final String EXPECTED_CANDIDATEID = "200";

    private static final String tablename = "Candidate";

    @Before
    public void setup() throws Exception {

        // Create instance of DynamoDBmapper
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        // Create a request for a new table based on the candidate class
        CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Candidate.class);
        // Add provisioned throughput - required
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        // request a description of the table to see if it exists
        Boolean exists = dataProcessService.tableExists(amazonDynamoDB, tablename);

        // If it exists
        if (exists == true){

            dynamoDBMapper.batchDelete(
                    repository.findAll());

            amazonDynamoDB.deleteTable(tablename); // Delete the existing table
            sleep(20000);

            amazonDynamoDB.createTable(tableRequest); // Create the table
            sleep(20000);

        }
        // If table doesn't already exist
        else {
            amazonDynamoDB.createTable(tableRequest); // Create it
            sleep(20000);
        }
    }

    /**
     * Test to see if the table is not empty and that the candidateID is as expected
     * Note: Sleep is mandatory - It takes AWS a while to
     */

    @Test
    public void SaveCandidateTestCase() {
        Candidate can = new Candidate();
        can.setCandidate_id("200");
        String cid = can.getCandidate_id();

        repository.save(can);

        List<Candidate> result = (List<Candidate>) repository.findAll();

        assertTrue("Result size is not equal to one", result.size() == 1);
        assertTrue("Candidate_id is equal to expected Candidate_id",
                result.get(0).getCandidate_id().equals(EXPECTED_CANDIDATEID));
    }
}
