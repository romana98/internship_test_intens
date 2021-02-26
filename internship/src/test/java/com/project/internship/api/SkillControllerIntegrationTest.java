package com.project.internship.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.internship.dto.CandidateDTO;
import com.project.internship.dto.SkillDTO;
import com.project.internship.helper.RestResponsePage;
import com.project.internship.model.Skill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static com.project.internship.contants.CandidateConstants.CANDIDATE_ID;
import static com.project.internship.contants.SkillConstants.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class SkillControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String basePath = "/api/skills";

    @Test
    @Sql(statements = "DELETE FROM skills WHERE name='newSkill';",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createSkill_StatusOK() {
        Skill saveSkill = new Skill(NEW_SKILL_NAME);

        ResponseEntity<SkillDTO> responseEntity =
                restTemplate.postForEntity(basePath, saveSkill,
                        SkillDTO.class);

        SkillDTO skillDTO = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(NEW_SKILL_NAME, skillDTO.getName());
    }

    @Test
    public void createSkill_FalseUnique_StatusBadRequest() {
        Skill saveSkill = new Skill(SKILL_UNIQUE_DEP_NAME);

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(basePath, saveSkill,
                        String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Name already in use.", message);
    }

    @Test
    @Sql(statements = "INSERT INTO skills (id, name) values (4, 'C++ Programming');",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteSkill_StatusOK() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/" + DELETE_SKILL_ID,
                        HttpMethod.DELETE, new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteSkill_DependencyConstraint_StatusBadRequest() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/" + SKILL_ID,
                        HttpMethod.DELETE, new HttpEntity<>(null), String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Candidates have selected skill, deleting failed.", message);
    }

    @Test
    @Sql(statements = "UPDATE skills SET name='Java Programming' WHERE id=1;",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateSkill_StatusOK() {
        Skill updateSkill = new Skill(SKILL_ID, UPDATE_SKILL_NAME);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath,
                        HttpMethod.PUT, new HttpEntity<>(updateSkill, null), String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void update_FalseSkillId_Fail() {
        Skill updateSkill = new Skill(FALSE_SKILL_ID, UPDATE_SKILL_NAME);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath,
                        HttpMethod.PUT, new HttpEntity<>(updateSkill, null), String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Name already in use.", message);
    }

    @Test
    public void updateSkill_FalseUnique_StatusBadRequest() {
        Skill updateSkill = new Skill(SKILL_ID, SKILL_UNIQUE_DEP_NAME);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath,
                        HttpMethod.PUT, new HttpEntity<>(updateSkill, null), String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Name already in use.", message);
    }

    @Test
    public void getSkill_StatusOK() {
        ResponseEntity<SkillDTO> responseEntity =
                restTemplate.getForEntity(basePath + "/" + SKILL_ID,
                        SkillDTO.class);

        SkillDTO skillDTO = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(SKILL_UNIQUE_DEP_NAME, skillDTO.getName());
    }

    @Test
    public void getSkill_FalseSkillID_StatusBadRequest() {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(basePath + "/" + FALSE_SKILL_ID,
                        String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Skill doesn't exist", message);
    }

    @Test
    public void getSkillsPageByCandidate_StatusOK() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(basePath + "/" + CANDIDATE_ID + "/by-page?page=0&size=5",
                        String.class);

        Page<SkillDTO> skills = objectMapper.readValue(responseEntity.getBody(), new TypeReference<RestResponsePage<SkillDTO>>() {
        });

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3, skills.getContent().size());
    }

    @Test
    public void getSkillsPage_StatusOK() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(basePath + "/by-page?page=0&size=5",
                        String.class);

        Page<SkillDTO> skills = objectMapper.readValue(responseEntity.getBody(), new TypeReference<RestResponsePage<SkillDTO>>() {
        });

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(4, skills.getContent().size());
    }

    @Test
    public void getSkillsForAutocomplete_Found_StatusOK() {
        ResponseEntity<SkillDTO[]> responseEntity =
                restTemplate.getForEntity(basePath + "/autocomplete/" + AUTOCOMPLETE_SUB_STR_I,
                        SkillDTO[].class);

        SkillDTO[] skillDTOs = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, skillDTOs.length);
    }

    @Test
    public void getSkillsForAutocomplete_Empty_StatusOK() {
        ResponseEntity<SkillDTO[]> responseEntity =
                restTemplate.getForEntity(basePath + "/autocomplete/" + AUTOCOMPLETE_SUB_STR_FALSE,
                        SkillDTO[].class);

        SkillDTO[] skillDTOs = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, skillDTOs.length);
    }

    @Test
    public void getSkillsForSelect_StatusOK() {
        ResponseEntity<SkillDTO[]> responseEntity =
                restTemplate.getForEntity(basePath + "/select",
                        SkillDTO[].class);

        SkillDTO[] skillDTOs = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(4, skillDTOs.length);
    }
}