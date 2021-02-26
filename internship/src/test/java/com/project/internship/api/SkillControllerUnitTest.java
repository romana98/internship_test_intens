package com.project.internship.api;

import com.project.internship.helper.TestUtil;
import com.project.internship.model.Candidate;
import com.project.internship.model.Skill;
import com.project.internship.service.SkillService;
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

import java.util.*;

import static com.project.internship.contants.CandidateConstants.*;
import static com.project.internship.contants.SkillConstants.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SkillController.class)
public class SkillControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    SkillService skillService;

    private Skill skill;

    private static final String basePath = "/api/skills";

    @Before
    public void setUp() {
        skill = new Skill(SKILL_ID, SKILL_NAME);

        Candidate candidate1 = new Candidate(CANDIDATE1_ID, NAME1, DATE_OF_BIRTH1, CONTACT_NUMBER1, EMAIL1);
        Candidate candidate2 = new Candidate(CANDIDATE2_ID, NAME2, DATE_OF_BIRTH2, CONTACT_NUMBER2, EMAIL2);

        skill.setCandidates(new HashSet<>(Arrays.asList(candidate1, candidate2)));
    }

    @Test
    public void createSkill_StatusOK() throws Exception {
        Skill saveSkill = new Skill(NEW_SKILL_NAME);
        Skill savedSkill = new Skill(NEW_SKILL_ID, NEW_SKILL_NAME);

        given(skillService.save(Mockito.any(Skill.class))).willReturn(savedSkill);
        String json = TestUtil.json(saveSkill);

        mvc.perform(post(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(NEW_SKILL_ID)))
                .andExpect(jsonPath("$.name", is(NEW_SKILL_NAME)));
    }

    @Test
    public void createSkill_FalseUnique_StatusBadRequest() throws Exception {
        Skill saveSkill = new Skill(SKILL_NAME);

        given(skillService.save(Mockito.any(Skill.class))).willReturn(null);
        String json = TestUtil.json(saveSkill);

        mvc.perform(post(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Name already in use.")));
    }

    @Test
    public void deleteSkill_StatusOK() throws Exception {
        given(skillService.delete(NEW_SKILL_ID)).willReturn(true);

        mvc.perform(delete(basePath + "/" + NEW_SKILL_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSkill_DependencyConstraint_StatusBadRequest() throws Exception {
        given(skillService.delete(SKILL_ID)).willReturn(false);

        mvc.perform(delete(basePath + "/" + SKILL_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Candidates have selected skill, deleting failed.")));
    }

    @Test
    public void updateSkill_StatusOK() throws Exception {
        Skill updateSkill = new Skill(SKILL_ID, UPDATE_SKILL_NAME);
        String json = TestUtil.json(updateSkill);

        given(skillService.update(Mockito.any(Skill.class))).willReturn(true);

        mvc.perform(put(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void update_FalseSkillId_Fail() throws Exception {
        Skill updateSkill = new Skill(FALSE_SKILL_ID, UPDATE_SKILL_NAME);
        String json = TestUtil.json(updateSkill);

        given(skillService.update(Mockito.any(Skill.class))).willReturn(false);

        mvc.perform(put(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Name already in use.")));
    }

    @Test
    public void updateSkill_FalseUnique_StatusBadRequest() throws Exception {
        Skill updateSkill = new Skill(SKILL_ID, FALSE_UNIQUE_NAME);
        String json = TestUtil.json(updateSkill);

        given(skillService.update(Mockito.any(Skill.class))).willReturn(false);

        mvc.perform(put(basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Name already in use.")));
    }

    @Test
    public void getSkill_StatusOK() throws Exception {
        given(skillService.findOne(SKILL_ID)).willReturn(skill);

        mvc.perform(get(basePath + "/" + SKILL_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(SKILL_ID)))
                .andExpect(jsonPath("$.name", is(SKILL_NAME)));
    }

    @Test
    public void getSkill_FalseSkillID_StatusBadRequest() throws Exception {
        given(skillService.findOne(FALSE_SKILL_ID)).willReturn(null);

        mvc.perform(get(basePath + "/" + SKILL_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Skill doesn't exist")));
    }

    @Test
    public void getSkillsPageByCandidate_StatusOK() throws Exception {
        Page<Skill> page = new PageImpl<>(Collections.singletonList(skill));

        given(skillService.findAllByCandidate(Mockito.eq(CANDIDATE_ID), Mockito.any(Pageable.class))).willReturn(page);

        mvc.perform(get(basePath + "/" + CANDIDATE_ID + "/by-page?page=0&size=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(SKILL_ID)))
                .andExpect(jsonPath("$.content[0].name", is(SKILL_NAME)));
    }

    @Test
    public void getSkillsPage_StatusOK() throws Exception {
        Page<Skill> page = new PageImpl<>(Collections.singletonList(skill));

        given(skillService.findAll(Mockito.any(Pageable.class))).willReturn(page);

        mvc.perform(get(basePath + "/by-page?page=0&size=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(SKILL_ID)))
                .andExpect(jsonPath("$.content[0].name", is(SKILL_NAME)));
    }

    @Test
    public void getSkillsForAutocomplete_Found_StatusOK() throws Exception {
        List<Skill> skills = Collections.singletonList(skill);

        given(skillService.findAll(AUTOCOMPLETE_SUB_STR)).willReturn(skills);

        mvc.perform(get(basePath + "/autocomplete/" + AUTOCOMPLETE_SUB_STR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(SKILL_ID)))
                .andExpect(jsonPath("$.[0].name", is(SKILL_NAME)));
    }

    @Test
    public void getSkillsForAutocomplete_Empty_StatusOK() throws Exception {
        List<Skill> skills = new ArrayList<>();

        given(skillService.findAll(AUTOCOMPLETE_SUB_STR_FALSE)).willReturn(skills);

        mvc.perform(get(basePath + "/autocomplete/" + AUTOCOMPLETE_SUB_STR_FALSE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getSkillsForSelect_StatusOK() throws Exception {
        List<Skill> skills = Collections.singletonList(skill);

        given(skillService.findAll()).willReturn(skills);

        mvc.perform(get(basePath + "/select")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(SKILL_ID)))
                .andExpect(jsonPath("$[0].name", is(SKILL_NAME)));
    }
}