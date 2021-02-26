package com.project.internship.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.internship.dto.CandidateDTO;
import com.project.internship.dto.SearchDTO;
import com.project.internship.dto.SkillDTO;
import com.project.internship.helper.RestResponsePage;
import com.project.internship.model.Candidate;
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

import static com.project.internship.contants.CandidateConstants.*;
import static com.project.internship.contants.SkillConstants.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CandidateControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String basePath = "/api/candidates";

    @Test
    @Sql(statements = "DELETE FROM candidates WHERE email='johnny.doe@email.com';",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCandidate_StatusOk() {
        Candidate saveCandidate = new Candidate(NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL);

        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.postForEntity(basePath, saveCandidate,
                        CandidateDTO.class);

        CandidateDTO candidateDTO = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(NEW_NAME, candidateDTO.getFullName());
    }

    @Test
    public void createCandidate_FalseUnique_StatusBadRequest() {
        Candidate saveCandidate = new Candidate(NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, CANDIDATE_UPDATE_EMAIL);

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(basePath, saveCandidate,
                        String.class);

        String message = responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Email or contact number already in use.", message);
    }

    @Test
    @Sql(statements = "INSERT INTO candidates (id, full_name, date_of_birth, contact_number, email) values (3, 'Karen Maitland', '2000-03-15 00:00:00.000000', '062303030', 'karen.maitland@email.com');",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCandidate_StatusOk() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/" + CANDIDATE_DELETE_ID,
                        HttpMethod.DELETE, new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    public void deleteCandidate_FalseCandidateID_StatusBadRequest() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/" + FALSE_CANDIDATE_ID,
                        HttpMethod.DELETE, new HttpEntity<>(null), String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error occurred while deleting candidate", message);
    }

    @Test
    @Sql(statements = "UPDATE candidates SET full_name='Karen Maitland', date_of_birth='2000-03-15 00:00:00.000000', contact_number='062303030', email='karen.maitland@email.com' WHERE id=3;",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCandidate_StatusOk() {
        CandidateDTO updateCandidateDTO = new CandidateDTO(CANDIDATE_DELETE_ID, NEW_NAME, NEW_CONTACT_NUMBER, NEW_EMAIL, NEW_DATE_OF_BIRTH);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/",
                        HttpMethod.PUT, new HttpEntity<>(updateCandidateDTO, null), String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void updateCandidate_FalseCandidateID_StatusBadRequest() {
        CandidateDTO updateCandidateDTO = new CandidateDTO(FALSE_CANDIDATE_ID, NEW_NAME, NEW_CONTACT_NUMBER, NEW_EMAIL, NEW_DATE_OF_BIRTH);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/",
                        HttpMethod.PUT, new HttpEntity<>(updateCandidateDTO, null), String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Email or contact number already in use.", message);
    }

    @Test
    public void updateCandidate_FalseUnique_StatusBadRequest() {
        CandidateDTO updateCandidateDTO = new CandidateDTO(CANDIDATE_DELETE_ID, NEW_NAME, NEW_CONTACT_NUMBER, CANDIDATE_UPDATE_EMAIL, NEW_DATE_OF_BIRTH);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/",
                        HttpMethod.PUT, new HttpEntity<>(updateCandidateDTO, null), String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Email or contact number already in use.", message);
    }

    @Test
    public void getCandidatesPage_StatusOK() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(basePath + "/by-page?page=0&size=5",
                        String.class);

        Page<CandidateDTO> candidates = objectMapper.readValue(responseEntity.getBody(), new TypeReference<RestResponsePage<CandidateDTO>>() {
        });

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3, candidates.getContent().size());
    }

    @Test
    public void getCandidate_StatusOK() {
        ResponseEntity<CandidateDTO> responseEntity =
                restTemplate.getForEntity(basePath + "/" + CANDIDATE_ID,
                        CandidateDTO.class);

        CandidateDTO candidateDTO = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(NAME_GET, candidateDTO.getFullName());
        assertEquals(EMAIL_GET, candidateDTO.getEmail());
        assertEquals(CONTACT_NUMBER_GET, candidateDTO.getContactNumber());

    }

    @Test
    public void getCandidate_FalseCandidateID_StatusNotFound() {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(basePath + "/" + FALSE_CANDIDATE_ID,
                        String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Candidate doesn't exist", message);
    }

    @Test
    @Sql(statements = "INSERT INTO candidates_skills (candidate_id, skill_id) values (1, 1);",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void removeSkillFromCandidate_StatusOK() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/" + CANDIDATE_ID + "/" + SKILL_REMOVE_ID,
                        HttpMethod.DELETE, new HttpEntity<>(null), String.class);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void removeSkillFromCandidate_FalseSkillID_StatusBadRequest() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/" + CANDIDATE_ID + "/" + FALSE_SKILL_ID,
                        HttpMethod.DELETE, new HttpEntity<>(null), String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error occurred while removing skill", message);
    }

    @Test
    public void removeSkillFromCandidate_FalseCandidateID_StatusBadRequest() {
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/" + FALSE_CANDIDATE_ID + "/" + SKILL_ID,
                        HttpMethod.DELETE, new HttpEntity<>(null), String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error occurred while removing skill", message);
    }

    @Test
    @Sql(statements = "DELETE FROM candidates_skills WHERE candidate_id = (SELECT id FROM candidates WHERE email = 'karen.maitland@email.com') and skill_id = (SELECT id FROM skills WHERE name = 'Java Programming')",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addSkillToCandidate_StatusOk() {
        SkillDTO skillDTO = new SkillDTO(SKILL_REMOVE_NAME);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/" + CANDIDATE_DELETE_ID,
                        HttpMethod.PUT, new HttpEntity<>(skillDTO, null), String.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void addSkillToCandidate_FalseCandidateID_StatusBadRequest() {
        SkillDTO skillDTO = new SkillDTO(NEW_SKILL_NAME);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/" + FALSE_CANDIDATE_ID,
                        HttpMethod.PUT, new HttpEntity<>(skillDTO, null), String.class);

        String message = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error occurred while adding skill", message);
    }

    @Test
    public void searchCandidates_StatusOK() throws JsonProcessingException {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setByName("");
        searchDTO.setBySkillName("Java Programming");

        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(basePath + "/search?page=0&size=5",
                        HttpMethod.POST, new HttpEntity<>(searchDTO, null),
                        String.class);

        Page<CandidateDTO> candidates = objectMapper.readValue(responseEntity.getBody(), new TypeReference<RestResponsePage<CandidateDTO>>() {
        });

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, candidates.getContent().size());
    }
}