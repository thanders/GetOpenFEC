package com.fec.restclient.repository;

import com.fec.restclient.bean.Candidate;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


@EnableScan
public interface CandidateRepository extends CrudRepository<Candidate, String> {
    List<Candidate> findCandidateByID(String candidate_id);
    List<Candidate> findByParty(String party);

}
