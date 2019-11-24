package com.fec.restclient.repository;

import com.fec.restclient.bean.CandidateTest;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface CandidateTestRepository extends CrudRepository<CandidateTest, String> {
    List<CandidateTest> findCandidateByID(String candidate_id);
}
