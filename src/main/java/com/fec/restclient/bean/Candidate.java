package com.fec.restclient.bean;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
@Component
@DynamoDBTable(tableName = "Candidate")
public class Candidate {

    String ID;
    String office_full;
    String candidate_status;
    String flags;
    String office;
    Boolean candidate_inactive;
    String candidate_id;
    List<Integer> cycles;
    int active_through;
    String party;
    Boolean has_raised_funds;
    int district_number;
    String load_date;
    String incumbent_challenge;
    String incumbent_challenge_full;
    int district;
    LocalDate last_file_date;
    List<Integer> election_districts;
    String party_full;

    String last_f2_date;
    List<Integer> inactive_election_years;
    String name;
    String state;
    LocalDate first_file_date;
    List<Integer> election_years;
    Boolean federal_funds_flag;


    @DynamoDBHashKey()
    public String getCandidate_id() { return candidate_id; }
    public void setCandidate_id(String candidate_id) {
        this.candidate_id = candidate_id;
    }

    @DynamoDBAttribute()
    public String getOffice_full() { return office_full; }
    public void setOffice_full(String office_full) { this.office_full = office_full; }

    @DynamoDBAttribute()
    public String getCandidate_status() { return candidate_status; }
    public void setCandidate_status(String candidate_status) { this.candidate_status = candidate_status; }

    @DynamoDBIgnore
    public String getFlags() { return flags; }
    public void setFlags(String flags) { this.flags = flags; }
    @DynamoDBIgnore
    public String getOffice() { return office; }
    public void setOffice(String office) { this.office = office; }

    @DynamoDBIgnore
    public Boolean getCandidate_inactive() { return candidate_inactive; }
    public void setCandidate_inactive(Boolean candidate_inactive) { this.candidate_inactive = candidate_inactive; }

    @DynamoDBIgnore
    public List<Integer> getCycles() { return cycles; }
    public void setCycles(ArrayList<Integer> cycles) { this.cycles = cycles; }

    @DynamoDBIgnore
    public int getActive_through() { return active_through; }
    public void setActive_through(int active_through) { this.active_through = active_through; }

    @DynamoDBIgnore
    public String getParty() { return party; }
    public void setParty(String party) { this.party = party; }

    @DynamoDBIgnore
    public Boolean getHas_raised_funds() { return has_raised_funds; }
    public void setHas_raised_funds(Boolean has_raised_funds) { this.has_raised_funds = has_raised_funds; }

    @DynamoDBIgnore
    public int getDistrict_number() { return district_number; }
    public void setDistrict_number(int district_number) { this.district_number = district_number; }

    @DynamoDBIgnore
    public String getLoad_date() { return load_date; }
    public void setLoad_date(String load_date) { this.load_date = load_date; }

    @DynamoDBIgnore
    public String getIncumbent_challenge() { return incumbent_challenge; }
    public void setIncumbent_challenge(String incumbent_challenge) { this.incumbent_challenge = incumbent_challenge; }

    @DynamoDBAttribute()
    public String getIncumbent_challenge_full() { return incumbent_challenge_full; }
    public void setIncumbent_challenge_full(String incumbent_challenge_full) { this.incumbent_challenge_full = incumbent_challenge_full; }

    @DynamoDBIgnore
    public int getDistrict() { return district; }
    public void setDistrict(int district) { this.district = district; }

    @DynamoDBAttribute()
    public String getLast_file_date() { return last_file_date.toString(); }
    public void setLast_file_date(LocalDate last_file_date) { this.last_file_date = last_file_date; }

    @DynamoDBIgnore
    public List<Integer> getElection_districts() { return election_districts; }

    @DynamoDBIgnore
    public void setElection_districts(ArrayList<Integer> election_districts) { this.election_districts = election_districts; }

    @DynamoDBAttribute()
    public String getParty_full() { return party_full; }
    public void setParty_full(String party_full) { this.party_full = party_full; }

    @DynamoDBAttribute()
    public String getLast_f2_date() { return last_f2_date; }
    public void setLast_f2_date(String last_f2_date) { this.last_f2_date = last_f2_date; }

    @DynamoDBIgnore
    public List<Integer> getInactive_election_years() { return inactive_election_years; }
    public void setInactive_election_years(ArrayList<Integer> inactive_election_years) { this.inactive_election_years = inactive_election_years; }

    @DynamoDBAttribute()
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @DynamoDBIgnore
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    @DynamoDBIgnore
    public LocalDate getFirst_file_date() { return first_file_date; }
    public void setFirst_file_date(LocalDate first_file_date) { this.first_file_date = first_file_date; }

    @DynamoDBIgnore
    public List<Integer> getElection_years() { return election_years; }
    public void setElection_years(ArrayList<Integer> election_years) { this.election_years = election_years; }

    @DynamoDBIgnore
    public Boolean getFederal_funds_flag() { return federal_funds_flag; }
    public void setFederal_funds_flag(Boolean federal_funds_flag) { this.federal_funds_flag = federal_funds_flag; }
}
