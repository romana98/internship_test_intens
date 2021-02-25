package com.project.internship.api;

import com.project.internship.TestUtil;
import com.project.internship.dto.SearchDTO;
import com.project.internship.model.Candidate;
import com.project.internship.model.Skill;
import com.project.internship.service.CandidateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static com.project.internship.api.contants.CandidateConstants.*;
import static com.project.internship.api.contants.SkillConstants.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CandidateController.class)
public class CandidateControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CandidateService candidateService;

    private Candidate candidate;

    private static final String basePath = "/api/candidates";

    @Before
    public void setUp() {
        candidate = new Candidate(CANDIDATE_ID, NAME, DATE_OF_BIRTH, CONTACT_NUMBER, EMAIL);

        Skill skill1 = new Skill(SKILL1_ID, SKILL1_NAME);
        Skill skill2 = new Skill(SKILL2_ID, SKILL2_NAME);

        candidate.setSkills(new HashSet<>(Arrays.asList(skill1, skill2)));
    }

    @Test
    public void createCandidate_StatusOk() throws Exception {
        Candidate saveCandidate = new Candidate(NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL);
        Candidate savedCandidate = new Candidate(NEW_CANDIDATE_ID, NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL);

        given(candidateService.save(Mockito.any(Candidate.class))).willReturn(savedCandidate);
        String json = TestUtil.json(saveCandidate);

        mvc.perform(post(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(NEW_CANDIDATE_ID)))
                .andExpect(jsonPath("$.fullName", is(NEW_NAME)))
                .andExpect(jsonPath("$.contactNumber", is(NEW_CONTACT_NUMBER)))
                .andExpect(jsonPath("$.email", is(NEW_EMAIL)))
                .andExpect(jsonPath("$.skills", hasSize(0)));
    }

    @Test
    public void createCandidate_FalseUnique_StatusBadRequest() throws Exception {
        Candidate saveCandidate = new Candidate(NEW_NAME, NEW_DATE_OF_BIRTH, FALSE_UNIQUE_CONTACT_NUMBER, FALSE_UNIQUE_EMAIL);

        given(candidateService.save(Mockito.any(Candidate.class))).willReturn(null);
        String json = TestUtil.json(saveCandidate);

        mvc.perform(post(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Email or contact number already in use.")));
    }

    @Test
    public void deleteCandidate_StatusOk() throws Exception {
        given(candidateService.delete(CANDIDATE_ID)).willReturn(true);

        mvc.perform(delete(basePath + "/" + CANDIDATE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCandidate_FalseCandidateID_StatusBadRequest() throws Exception {
        given(candidateService.delete(FALSE_CANDIDATE_ID)).willReturn(false);

        mvc.perform(delete(basePath + "/" + FALSE_CANDIDATE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Error occurred while deleting candidate")));
    }

    @Test
    public void updateCandidate_StatusOk() throws Exception {
        Candidate updateCandidate = new Candidate(CANDIDATE_ID, UPDATE_NAME, UPDATE_DATE_OF_BIRTH, UPDATE_CONTACT_NUMBER, UPDATE_EMAIL);
        String json = TestUtil.json(updateCandidate);

        given(candidateService.update(Mockito.any(Candidate.class))).willReturn(true);

        mvc.perform(put(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCandidate_FalseCandidateID_StatusBadRequest() throws Exception {
        Candidate updateCandidate = new Candidate(FALSE_CANDIDATE_ID, UPDATE_NAME, UPDATE_DATE_OF_BIRTH, UPDATE_CONTACT_NUMBER, UPDATE_EMAIL);
        String json = TestUtil.json(updateCandidate);

        given(candidateService.update(Mockito.any(Candidate.class))).willReturn(false);

        mvc.perform(put(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Email or contact number already in use.")));
    }

    @Test
    public void updateCandidate_FalseUnique_StatusBadRequest() throws Exception {
        Candidate updateCandidate = new Candidate(CANDIDATE_ID, UPDATE_NAME, UPDATE_DATE_OF_BIRTH, FALSE_UNIQUE_CONTACT_NUMBER, FALSE_UNIQUE_EMAIL);
        String json = TestUtil.json(updateCandidate);

        given(candidateService.update(Mockito.any(Candidate.class))).willReturn(false);

        mvc.perform(put(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Email or contact number already in use.")));
    }

    @Test
    public void getCandidatesPage_StatusOK() throws Exception {
        Page<Candidate> page = new PageImpl<>(Collections.singletonList(candidate));

        given(candidateService.findAll(Mockito.any(Pageable.class))).willReturn(page);

        mvc.perform(get(basePath + "/by-page?page=0&size=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(CANDIDATE_ID)))
                .andExpect(jsonPath("$.content[0].fullName", is(NAME)))
                .andExpect(jsonPath("$.content[0].contactNumber", is(CONTACT_NUMBER)))
                .andExpect(jsonPath("$.content[0].skills", hasSize(2)));
    }

    @Test
    public void getCandidate_StatusOK() throws Exception {
        given(candidateService.findOne(CANDIDATE_ID)).willReturn(candidate);

        mvc.perform(get(basePath + "/" + CANDIDATE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(CANDIDATE_ID)))
                .andExpect(jsonPath("$.fullName", is(NAME)))
                .andExpect(jsonPath("$.contactNumber", is(CONTACT_NUMBER)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.skills", hasSize(2)));
    }

    @Test
    public void getCandidate_FalseCandidateID_StatusNotFound() throws Exception {
        given(candidateService.findOne(FALSE_CANDIDATE_ID)).willReturn(null);

        mvc.perform(get(basePath + "/" + CANDIDATE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Candidate doesn't exist")));
    }

    @Test
    public void removeSkillFromCandidate_StatusOK() throws Exception {
        given(candidateService.removeSkill(CANDIDATE_ID, SKILL1_ID)).willReturn(true);

        mvc.perform(delete(basePath + "/" + CANDIDATE_ID + "/" + SKILL1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void removeSkillFromCandidate_FalseSkillID_StatusBadRequest() throws Exception {
        given(candidateService.removeSkill(CANDIDATE_ID, FALSE_SKILL_ID)).willReturn(false);

        mvc.perform(delete(basePath + "/" + CANDIDATE_ID + "/" + FALSE_SKILL_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Error occurred while removing skill")));
    }

    @Test
    public void removeSkillFromCandidate_FalseCandidateID_StatusBadRequest() throws Exception {
        given(candidateService.removeSkill(FALSE_CANDIDATE_ID, SKILL1_ID)).willReturn(false);

        mvc.perform(delete(basePath + "/" + FALSE_CANDIDATE_ID + "/" + SKILL1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Error occurred while removing skill")));
    }

    @Test
    public void addSkillToCandidate_StatusOk() throws Exception {
        Skill newSkill = new Skill(NEW_SKILL_NAME);
        String json = TestUtil.json(newSkill);

        given(candidateService.addSkill(CANDIDATE_ID, newSkill)).willReturn(true);

        mvc.perform(put(basePath + "/" + CANDIDATE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void addSkillToCandidate_FalseCandidateID_StatusBadRequest() throws Exception {
        Skill newSkill = new Skill(NEW_SKILL_NAME);
        String json = TestUtil.json(newSkill);

        given(candidateService.addSkill(FALSE_CANDIDATE_ID, newSkill)).willReturn(false);

        mvc.perform(put(basePath + "/" + FALSE_CANDIDATE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Error occurred while adding skill")));
    }

    @Test
    public void searchCandidates_StatusOK() throws Exception {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setByName("John");
        searchDTO.setBySkillName("Skill1|Skill2");

        String json = TestUtil.json(searchDTO);

        Page<Candidate> page = new PageImpl<>(Collections.singletonList(candidate));


        given(candidateService.search(Mockito.any(SearchDTO.class), Mockito.any(Pageable.class))).willReturn(page);

        mvc.perform(post(basePath + "/search?page=0&size=5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(CANDIDATE_ID)))
                .andExpect(jsonPath("$.content[0].fullName", is(NAME)))
                .andExpect(jsonPath("$.content[0].contactNumber", is(CONTACT_NUMBER)))
                .andExpect(jsonPath("$.content[0].email", is(EMAIL)))
                .andExpect(jsonPath("$.content[0].skills", hasSize(2)));
    }
}