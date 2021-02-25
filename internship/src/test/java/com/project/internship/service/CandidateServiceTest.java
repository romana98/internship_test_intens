package com.project.internship.service;

import com.project.internship.dto.SearchDTO;
import com.project.internship.model.Candidate;
import com.project.internship.model.Skill;
import com.project.internship.repository.CandidateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static com.project.internship.contants.CandidateConstants.*;
import static com.project.internship.contants.SkillConstants.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CandidateServiceTest {

    @MockBean
    private CandidateRepository candidateRepository;

    @MockBean
    private SkillService skillService;

    @Autowired
    private CandidateService candidateService;

    private Candidate candidate;

    @Before
    public void setUp() {
        candidate = new Candidate(CANDIDATE_ID, NAME, DATE_OF_BIRTH, CONTACT_NUMBER, EMAIL);

        Skill skill1 = new Skill(SKILL1_ID, SKILL1_NAME);

        candidate.setSkills(new HashSet<>(Collections.singletonList(skill1)));
    }

    @Test
    public void save_NoSkills_Success() {
        Candidate saveCandidate = new Candidate(NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL);
        Set<Skill> updatedSkills = new HashSet<>();

        given(candidateRepository
                .findByEmailAndIdIsNotOrContactNumberAndIdIsNot(saveCandidate.getEmail(), -1, saveCandidate.getContactNumber(), -1))
                .willReturn(null);
        given(skillService.getSkillIds(saveCandidate.getSkills())).willReturn(updatedSkills);
        given(candidateRepository.save(saveCandidate)).
                willReturn(new Candidate(NEW_CANDIDATE_ID, NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL));

        Candidate savedCandidate = candidateService.save(saveCandidate);

        verify(candidateRepository, times(1)).
                findByEmailAndIdIsNotOrContactNumberAndIdIsNot(saveCandidate.getEmail(), -1, saveCandidate.getContactNumber(), -1);
        verify(skillService, times(1)).
                getSkillIds(saveCandidate.getSkills());
        verify(candidateRepository, times(1)).
                save(saveCandidate);

        assertEquals(NEW_CANDIDATE_ID.longValue(), savedCandidate.getId());
        assertEquals(NEW_NAME, savedCandidate.getFullName());
        assertEquals(NEW_DATE_OF_BIRTH, savedCandidate.getDateOfBirth());
        assertEquals(NEW_CONTACT_NUMBER, savedCandidate.getContactNumber());
        assertEquals(NEW_EMAIL, savedCandidate.getEmail());
    }

    @Test
    public void save_WithSkills_Success() {
        Candidate saveCandidate = new Candidate(NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL);
        Skill skill1 = new Skill(SKILL1_NAME);
        Skill skill2 = new Skill(SKILL2_NAME);
        saveCandidate.setSkills(new HashSet<>(Arrays.asList(skill1, skill2)));

        Set<Skill> updatedSkills = new HashSet<>(Arrays.asList(skill1, skill2));

        given(candidateRepository
                .findByEmailAndIdIsNotOrContactNumberAndIdIsNot(saveCandidate.getEmail(), -1, saveCandidate.getContactNumber(), -1))
                .willReturn(null);
        given(skillService.getSkillIds(saveCandidate.getSkills())).willReturn(updatedSkills);

        given(candidateRepository.save(saveCandidate)).
                willReturn(new Candidate(NEW_CANDIDATE_ID, NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL, updatedSkills));

        Candidate savedCandidate = candidateService.save(saveCandidate);

        verify(candidateRepository, times(1)).
                findByEmailAndIdIsNotOrContactNumberAndIdIsNot(saveCandidate.getEmail(), -1, saveCandidate.getContactNumber(), -1);
        verify(skillService, times(1)).
                getSkillIds(saveCandidate.getSkills());
        verify(candidateRepository, times(1)).
                save(saveCandidate);

        assertEquals(NEW_CANDIDATE_ID.longValue(), savedCandidate.getId());
        assertEquals(NEW_NAME, savedCandidate.getFullName());
        assertEquals(NEW_DATE_OF_BIRTH, savedCandidate.getDateOfBirth());
        assertEquals(NEW_CONTACT_NUMBER, savedCandidate.getContactNumber());
        assertEquals(NEW_EMAIL, savedCandidate.getEmail());
        assertEquals(2, savedCandidate.getSkills().size());
    }

    @Test
    public void save_UniqueConstraint_Fail() {
        Candidate saveCandidate = new Candidate(NEW_NAME, NEW_DATE_OF_BIRTH, CONTACT_NUMBER, EMAIL);
        Skill skill1 = new Skill(SKILL1_NAME);
        Skill skill2 = new Skill(SKILL2_NAME);
        saveCandidate.setSkills(new HashSet<>(Arrays.asList(skill1, skill2)));

        Set<Skill> updatedSkills = new HashSet<>(Arrays.asList(skill1, skill2));

        given(candidateRepository
                .findByEmailAndIdIsNotOrContactNumberAndIdIsNot(saveCandidate.getEmail(), -1, saveCandidate.getContactNumber(), -1))
                .willReturn(candidate);
        given(skillService.getSkillIds(saveCandidate.getSkills())).willReturn(updatedSkills);

        given(candidateRepository.save(saveCandidate)).
                willReturn(new Candidate(NEW_CANDIDATE_ID, NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL, updatedSkills));

        Candidate savedCandidate = candidateService.save(saveCandidate);

        verify(candidateRepository, times(1)).
                findByEmailAndIdIsNotOrContactNumberAndIdIsNot(saveCandidate.getEmail(), -1, saveCandidate.getContactNumber(), -1);
        verify(skillService, times(0)).
                getSkillIds(saveCandidate.getSkills());
        verify(candidateRepository, times(0)).
                save(saveCandidate);

        assertNull(savedCandidate);

    }

    @Test
    public void delete_Success() {
        given(candidateRepository.findById(candidate.getId())).willReturn(java.util.Optional.of(candidate));
        doNothing().when(candidateRepository).delete(candidate);

        boolean isDeleted = candidateService.delete(candidate.getId());

        verify(candidateRepository, times(1)).
                findById(candidate.getId());

        assertTrue(isDeleted);
    }

    @Test
    public void delete_FalseCandidateID_Fail() {
        given(candidateRepository.findById(FALSE_CANDIDATE_ID)).willReturn(java.util.Optional.empty());

        boolean isDeleted = candidateService.delete(FALSE_CANDIDATE_ID);

        verify(candidateRepository, times(1)).
                findById(FALSE_CANDIDATE_ID);

        assertFalse(isDeleted);
    }

    @Test
    public void update_Success() {
        Candidate updateCandidate = new Candidate(CANDIDATE_ID, NEW_NAME, NEW_DATE_OF_BIRTH, NEW_CONTACT_NUMBER, NEW_EMAIL);

        given(candidateRepository.findById(updateCandidate.getId())).willReturn(java.util.Optional.of(candidate));
        given(candidateRepository
                .findByEmailAndIdIsNotOrContactNumberAndIdIsNot(
                        updateCandidate.getEmail(),
                        updateCandidate.getId(),
                        updateCandidate.getContactNumber(),
                        updateCandidate.getId()))
                .willReturn(null);

        given(candidateRepository.save(updateCandidate)).
                willReturn(updateCandidate);

        boolean isUpdated = candidateService.update(updateCandidate);

        verify(candidateRepository, times(1)).
                findById(updateCandidate.getId());

        verify(candidateRepository, times(1))
                .findByEmailAndIdIsNotOrContactNumberAndIdIsNot(
                        updateCandidate.getEmail(),
                        updateCandidate.getId(),
                        updateCandidate.getContactNumber(),
                        updateCandidate.getId());

        verify(candidateRepository, times(1)).
                save(updateCandidate);

        assertTrue(isUpdated);
    }

    @Test
    public void update_UniqueConstraint_Fail() {
        Candidate updateCandidate = new Candidate(CANDIDATE_ID, NEW_NAME, NEW_DATE_OF_BIRTH, FALSE_UNIQUE_CONTACT_NUMBER, FALSE_UNIQUE_CONTACT_NUMBER);

        given(candidateRepository.findById(updateCandidate.getId())).willReturn(java.util.Optional.empty());
        given(candidateRepository
                .findByEmailAndIdIsNotOrContactNumberAndIdIsNot(
                        updateCandidate.getEmail(),
                        updateCandidate.getId(),
                        updateCandidate.getContactNumber(),
                        updateCandidate.getId()))
                .willReturn(null);

        given(candidateRepository.save(updateCandidate)).
                willReturn(updateCandidate);

        boolean isUpdated = candidateService.update(updateCandidate);

        verify(candidateRepository, times(1)).
                findById(updateCandidate.getId());

        verify(candidateRepository, times(0))
                .findByEmailAndIdIsNotOrContactNumberAndIdIsNot(
                        updateCandidate.getEmail(),
                        updateCandidate.getId(),
                        updateCandidate.getContactNumber(),
                        updateCandidate.getId());

        verify(candidateRepository, times(0)).
                save(updateCandidate);

        assertFalse(isUpdated);
    }

    @Test
    public void findAll() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Candidate> page = new PageImpl<>(Collections.singletonList(candidate));
        given(candidateRepository.findAll(pageable)).willReturn(page);

        Page<Candidate> candidates = candidateService.findAll(pageable);

        verify(candidateRepository, times(1)).
                findAll(pageable);

        assertEquals(page, candidates);
    }

    @Test
    public void findOne_Success() {
        given(candidateRepository.findById(candidate.getId())).willReturn(java.util.Optional.of(candidate));

        Candidate getCandidate = candidateService.findOne(candidate.getId());

        verify(candidateRepository, times(1)).
                findById(candidate.getId());

        assertEquals(candidate, getCandidate);
    }

    @Test
    public void findOne_FalseCandidateID_Fail() {
        given(candidateRepository.findById(FALSE_CANDIDATE_ID)).willReturn(java.util.Optional.empty());

        Candidate getCandidate = candidateService.findOne(FALSE_CANDIDATE_ID);

        verify(candidateRepository, times(1)).
                findById(FALSE_CANDIDATE_ID);

        assertNull(getCandidate);
    }

    @Test
    public void removeSkill_Success() {
        Skill skill = new Skill(SKILL1_ID, SKILL1_NAME);
        given(skillService.findOne(SKILL1_ID)).willReturn(skill);

        given(candidateRepository.findById(candidate.getId())).willReturn(java.util.Optional.of(candidate));

        candidate.getSkills().remove(skill);
        given(candidateRepository.save(candidate)).willReturn(candidate);

        boolean isRemoved = candidateService.removeSkill(candidate.getId(), SKILL1_ID);

        verify(skillService, times(1)).
                findOne(SKILL1_ID);

        verify(candidateRepository, times(1)).
                findById(candidate.getId());

        verify(candidateRepository, times(1)).
                save(candidate);

        assertTrue(isRemoved);
    }

    @Test
    public void removeSkill_FalseCandidateID_Fail() {
        Skill skill = new Skill(SKILL1_ID, SKILL1_NAME);
        given(skillService.findOne(SKILL1_ID)).willReturn(skill);

        given(candidateRepository.findById(FALSE_CANDIDATE_ID)).willReturn(java.util.Optional.empty());

        boolean isRemoved = candidateService.removeSkill(FALSE_CANDIDATE_ID, SKILL1_ID);

        verify(skillService, times(1)).
                findOne(SKILL1_ID);

        verify(candidateRepository, times(1)).
                findById(FALSE_CANDIDATE_ID);

        assertFalse(isRemoved);
    }

    @Test
    public void removeSkill_FalseSkillID_Fail() {
        given(skillService.findOne(FALSE_SKILL_ID)).willReturn(null);

        given(candidateRepository.findById(candidate.getId())).willReturn(java.util.Optional.of(candidate));

        boolean isRemoved = candidateService.removeSkill(candidate.getId(), FALSE_SKILL_ID);

        verify(skillService, times(1)).
                findOne(FALSE_SKILL_ID);

        verify(candidateRepository, times(1)).
                findById(candidate.getId());

        assertFalse(isRemoved);
    }

    @Test
    public void addSkill_SkillInDB_Success() {
        Skill newSkill = new Skill(SKILL2_NAME);
        Skill skill = new Skill(SKILL2_ID, SKILL2_NAME);

        given(skillService.findByName(newSkill.getName())).willReturn(skill);
        given(candidateRepository.findById(candidate.getId())).willReturn(java.util.Optional.of(candidate));

        candidate.getSkills().add(skill);
        given(candidateRepository.save(candidate)).willReturn(candidate);

        boolean isAdded = candidateService.addSkill(candidate.getId(), newSkill);

        verify(skillService, times(1)).
                findByName(SKILL2_NAME);

        verify(candidateRepository, times(1)).
                findById(candidate.getId());

        verify(candidateRepository, times(1)).
                save(candidate);

        assertTrue(isAdded);
    }

    @Test
    public void addSkill_SkillNotInDB_Success() {
        Skill newSkill = new Skill(NEW_SKILL_NAME);

        given(skillService.findByName(newSkill.getName())).willReturn(null);
        given(candidateRepository.findById(candidate.getId())).willReturn(java.util.Optional.of(candidate));

        candidate.getSkills().add(newSkill);
        given(candidateRepository.save(candidate)).willReturn(candidate);

        boolean isAdded = candidateService.addSkill(candidate.getId(), newSkill);

        verify(skillService, times(1)).
                findByName(NEW_SKILL_NAME);

        verify(candidateRepository, times(1)).
                findById(candidate.getId());

        verify(candidateRepository, times(1)).
                save(candidate);

        assertTrue(isAdded);
    }

    @Test
    public void addSkill_FalseCandidateID_Fail() {
        Skill newSkill = new Skill(SKILL2_NAME);
        Skill skill = new Skill(SKILL2_ID, SKILL2_NAME);

        given(skillService.findByName(newSkill.getName())).willReturn(skill);
        given(candidateRepository.findById(candidate.getId())).willReturn(java.util.Optional.empty());

        boolean isAdded = candidateService.addSkill(candidate.getId(), newSkill);

        verify(skillService, times(1)).
                findByName(SKILL2_NAME);

        verify(candidateRepository, times(1)).
                findById(candidate.getId());

        assertFalse(isAdded);
    }

    @Test
    public void search_EmptyParams() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setByName("");
        searchDTO.setBySkillName("");

        Pageable pageable = PageRequest.of(0, 5);
        Page<Candidate> page = new PageImpl<>(Collections.singletonList(candidate));

        given(candidateRepository.findAll(pageable)).willReturn(page);

        Page<Candidate> candidates = candidateService.search(searchDTO, pageable);

        verify(candidateRepository, times(1)).
                findAll(pageable);

        assertEquals(page, candidates);
    }

    @Test
    public void search_ByName() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setByName("John");
        searchDTO.setBySkillName("");

        Pageable pageable = PageRequest.of(0, 5);
        Page<Candidate> page = new PageImpl<>(Collections.singletonList(candidate));

        given(candidateRepository.findAllByName(searchDTO.getByName(), pageable)).willReturn(page);

        Page<Candidate> candidates = candidateService.search(searchDTO, pageable);

        verify(candidateRepository, times(1)).
                findAllByName(searchDTO.getByName(), pageable);

        assertEquals(page, candidates);
    }

    @Test
    public void search_BySkills() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setByName("");
        searchDTO.setBySkillName("Skill1|Skill2");

        Pageable pageable = PageRequest.of(0, 5);
        Page<Candidate> page = new PageImpl<>(Collections.singletonList(candidate));

        List<String> skills = Arrays.asList(searchDTO.getBySkillName().split("\\|"));
        given(candidateRepository.findAllBySkillsName(skills, (long) skills.size(), pageable)).willReturn(page);

        Page<Candidate> candidates = candidateService.search(searchDTO, pageable);

        verify(candidateRepository, times(1)).
                findAllBySkillsName(skills, (long) skills.size(), pageable);

        assertEquals(page, candidates);
    }

    @Test
    public void search_ByNameAndSkills() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setByName("John");
        searchDTO.setBySkillName("Skill1|Skill2");

        Pageable pageable = PageRequest.of(0, 5);
        Page<Candidate> page = new PageImpl<>(Collections.singletonList(candidate));

        List<String> skills = Arrays.asList(searchDTO.getBySkillName().split("\\|"));
        given(candidateRepository.findAllByNameAndSkillName(searchDTO.getByName(), skills, (long) skills.size(), pageable)).willReturn(page);

        Page<Candidate> candidates = candidateService.search(searchDTO, pageable);

        verify(candidateRepository, times(1)).
                findAllByNameAndSkillName(searchDTO.getByName(), skills, (long) skills.size(), pageable);

        assertEquals(page, candidates);
    }
}