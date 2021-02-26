package com.project.internship.service;

import com.project.internship.dto.SearchDTO;
import com.project.internship.model.Candidate;
import com.project.internship.model.Skill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;

import static com.project.internship.contants.CandidateConstants.*;
import static com.project.internship.contants.SkillConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CandidateServiceIntegrationTest {

    @Autowired
    CandidateService candidateService;

    @Test
    @Transactional
    @Rollback
    public void save_NoSkills_Success() {
        Candidate saveCandidate = new Candidate(NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL);

        Candidate savedCandidate = candidateService.save(saveCandidate);
        assertEquals(NEW_NAME, savedCandidate.getFullName());
        assertEquals(NEW_CONTACT_NUMBER, savedCandidate.getContactNumber());
        assertEquals(NEW_EMAIL, savedCandidate.getEmail());
        assertEquals(0, savedCandidate.getSkills().size());

    }

    @Test
    @Transactional
    @Rollback
    public void save_WithSkills_Success() {
        Candidate saveCandidate = new Candidate(NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL);
        Skill skill1 = new Skill(SKILL1_NAME);
        Skill skill2 = new Skill(SKILL2_NAME);
        saveCandidate.setSkills(new HashSet<>(Arrays.asList(skill1, skill2)));

        Candidate savedCandidate = candidateService.save(saveCandidate);
        assertEquals(NEW_NAME, savedCandidate.getFullName());
        assertEquals(NEW_CONTACT_NUMBER, savedCandidate.getContactNumber());
        assertEquals(NEW_EMAIL, savedCandidate.getEmail());
        assertEquals(2, savedCandidate.getSkills().size());
    }

    @Test
    public void save_UniqueConstraint_Fail() {
        Candidate saveCandidate = new Candidate(NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, "john.smith@email.com");
        Skill skill1 = new Skill(SKILL1_NAME);
        Skill skill2 = new Skill(SKILL2_NAME);
        saveCandidate.setSkills(new HashSet<>(Arrays.asList(skill1, skill2)));

        Candidate savedCandidate = candidateService.save(saveCandidate);
        assertNull(savedCandidate);
    }

    @Test
    @Transactional
    @Rollback
    public void delete_Success() {

        boolean isDeleted = candidateService.delete(CANDIDATE_DELETE_ID);

        assertTrue(isDeleted);
    }

    @Test
    public void delete_FalseCandidateID_Fail() {
        boolean isDeleted = candidateService.delete(FALSE_CANDIDATE_ID);

        assertFalse(isDeleted);
    }

    @Test
    @Transactional
    @Rollback
    public void update_Success() {
        Candidate updateCandidate = new Candidate(CANDIDATE_DELETE_ID, NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL);
        boolean isUpdated = candidateService.update(updateCandidate);
        assertTrue(isUpdated);
    }

    @Test
    public void update_UniqueConstraint_Fail() {
        Candidate updateCandidate = new Candidate(CANDIDATE_DELETE_ID, NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, CANDIDATE_UPDATE_EMAIL);
        boolean isUpdated = candidateService.update(updateCandidate);
        assertFalse(isUpdated);
    }

    @Test
    public void findAll_Success() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Candidate> found = candidateService.findAll(pageable);

        assertEquals(3, found.getNumberOfElements());
    }

    @Test
    public void findOne_Success() {
        Candidate candidate = candidateService.findOne(CANDIDATE_ID);
        assertEquals(CANDIDATE_ID.longValue(), candidate.getId());
    }

    @Test
    public void findOne_FalseCandidateID_Fail() {
        Candidate candidate = candidateService.findOne(FALSE_CANDIDATE_ID);
        assertNull(candidate);
    }

    @Test
    @Transactional
    @Rollback
    public void removeSkill_Success() {
        boolean isRemoved = candidateService.removeSkill(CANDIDATE_ID, SKILL_REMOVE_ID);
        assertTrue(isRemoved);
    }

    @Test
    public void removeSkill_FalseCandidateID_Fail() {
        boolean isRemoved = candidateService.removeSkill(FALSE_CANDIDATE_ID, SKILL_REMOVE_ID);
        assertFalse(isRemoved);
    }

    @Test
    public void removeSkill_FalseSkillID_Fail() {
        boolean isRemoved = candidateService.removeSkill(CANDIDATE_ID, FALSE_SKILL_ID);
        assertFalse(isRemoved);
    }

    @Test
    @Transactional
    @Rollback
    public void addSkill_SkillInDB_Success() {
        Skill newSkill = new Skill(SKILL_REMOVE_NAME);
        boolean isAdded = candidateService.addSkill(CANDIDATE_DELETE_ID, newSkill);
        assertTrue(isAdded);
    }

    @Test
    @Transactional
    @Rollback
    public void addSkill_SkillNotInDB_Success() {
        Skill newSkill = new Skill(SKILL1_NAME);
        boolean isAdded = candidateService.addSkill(CANDIDATE_ID, newSkill);
        assertTrue(isAdded);
    }

    @Test
    public void addSkill_FalseCandidateID_Fail() {
        Skill newSkill = new Skill(SKILL2_NAME);
        boolean isAdded = candidateService.addSkill(FALSE_CANDIDATE_ID, newSkill);
        assertFalse(isAdded);
    }

    @Test
    public void search_EmptyParams() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setByName("");
        searchDTO.setBySkillName("");

        Pageable pageable = PageRequest.of(0, 5);
        Page<Candidate> candidates = candidateService.search(searchDTO, pageable);
        assertEquals(3, candidates.getContent().size());
    }

    @Test
    public void search_ByName() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setByName("John");
        searchDTO.setBySkillName("");

        Pageable pageable = PageRequest.of(0, 5);
        Page<Candidate> candidates = candidateService.search(searchDTO, pageable);
        assertEquals(1, candidates.getContent().size());
    }

    @Test
    public void search_BySkills() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setByName("");
        searchDTO.setBySkillName("Java Programming");

        Pageable pageable = PageRequest.of(0, 5);
        Page<Candidate> candidates = candidateService.search(searchDTO, pageable);
        assertEquals(2, candidates.getContent().size());
    }


}