package com.project.internship.service;

import com.project.internship.model.Candidate;
import com.project.internship.model.Skill;
import com.project.internship.repository.SkillRepository;
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
public class SkillServiceTest {

    @MockBean
    private SkillRepository skillRepository;

    @Autowired
    private SkillService skillService;

    private Skill skill;

    @Before
    public void setUp() {

        skill = new Skill(SKILL_ID, SKILL_NAME);

        Candidate candidate1 = new Candidate(CANDIDATE1_ID, NAME1, DATE_OF_BIRTH1, CONTACT_NUMBER1, EMAIL1);
        Candidate candidate2 = new Candidate(CANDIDATE2_ID, NAME2, DATE_OF_BIRTH2, CONTACT_NUMBER2, EMAIL2);

        skill.setCandidates(new HashSet<>(Arrays.asList(candidate1, candidate2)));
    }

    @Test
    public void save_Success() {
        Skill saveSkill = new Skill(NEW_SKILL_NAME);

        given(skillRepository.findByName(NEW_SKILL_NAME)).
                willReturn(null);

        given(skillRepository.save(saveSkill)).
                willReturn(new Skill(NEW_SKILL_ID, NEW_SKILL_NAME));

        Skill savedSkill = skillService.save(saveSkill);

        verify(skillRepository, times(1)).
                findByName(NEW_SKILL_NAME);

        verify(skillRepository, times(1)).
                save(saveSkill);

        assertEquals(NEW_SKILL_ID.longValue(), savedSkill.getId());
        assertEquals(NEW_SKILL_NAME, savedSkill.getName());

    }

    @Test
    public void save_UniqueConstraint_Fail() {
        Skill saveSkill = new Skill(SKILL_NAME);

        given(skillRepository.findByName(SKILL_NAME)).
                willReturn(skill);

        Skill savedSkill = skillService.save(saveSkill);

        verify(skillRepository, times(1)).
                findByName(SKILL_NAME);

        assertNull(savedSkill);
    }

    @Test
    public void delete_Success() {
        Skill newSkill = new Skill(SKILL1_ID, SKILL1_NAME);
        List<Skill> skills = new ArrayList<>();

        given(skillRepository.findAllByReference(SKILL1_ID)).
                willReturn(skills);
        given(skillRepository.findById(SKILL1_ID)).
                willReturn(Optional.of(newSkill));

        doNothing().when(skillRepository).delete(newSkill);

        boolean isDeleted = skillService.delete(SKILL1_ID);

        verify(skillRepository, times(1)).
                findAllByReference(SKILL1_ID);

        verify(skillRepository, times(1)).
                findById(SKILL1_ID);

        assertTrue(isDeleted);
    }

    @Test
    public void delete_DependencyConstraint_Fail() {
        List<Skill> skills = Collections.singletonList(skill);

        given(skillRepository.findAllByReference(SKILL_ID)).
                willReturn(skills);

        boolean isDeleted = skillService.delete(SKILL1_ID);

        verify(skillRepository, times(1)).
                findAllByReference(SKILL1_ID);


        assertFalse(isDeleted);
    }

    @Test
    public void update_Success() {
        Skill updateSkill = new Skill(SKILL_ID, NEW_SKILL_NAME);

        given(skillRepository.findById(updateSkill.getId())).
                willReturn(Optional.of(skill));
        given(skillRepository.findByName(updateSkill.getName())).
                willReturn(null);
        given(skillRepository.save(updateSkill)).
                willReturn(updateSkill);

        boolean updatedSkill = skillService.update(updateSkill);

        verify(skillRepository, times(1)).
                findById(updateSkill.getId());
        verify(skillRepository, times(1)).
                findByName(updateSkill.getName());
        verify(skillRepository, times(1)).
                save(updateSkill);

        assertTrue(updatedSkill);
    }

    @Test
    public void update_FalseSkillId_Fail() {
        Skill updateSkill = new Skill(FALSE_SKILL_ID, NEW_SKILL_NAME);

        given(skillRepository.findById(updateSkill.getId())).
                willReturn(Optional.empty());
        given(skillRepository.findByName(updateSkill.getName())).
                willReturn(null);

        boolean updatedSkill = skillService.update(updateSkill);

        verify(skillRepository, times(1)).
                findById(updateSkill.getId());
        verify(skillRepository, times(1)).
                findByName(updateSkill.getName());

        assertFalse(updatedSkill);
    }

    @Test
    public void update_UniqueConstraint_Fail() {
        Skill updateSkill = new Skill(SKILL_ID, SKILL_NAME);

        given(skillRepository.findById(updateSkill.getId())).
                willReturn(Optional.of(skill));
        given(skillRepository.findByName(updateSkill.getName())).
                willReturn(skill);

        boolean updatedSkill = skillService.update(updateSkill);

        verify(skillRepository, times(1)).
                findById(updateSkill.getId());
        verify(skillRepository, times(1)).
                findByName(updateSkill.getName());

        assertFalse(updatedSkill);
    }

    @Test
    public void findAllPageable_Success() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Skill> page = new PageImpl<>(Collections.singletonList(skill));
        given(skillRepository.findAll(pageable)).willReturn(page);

        Page<Skill> skills = skillService.findAll(pageable);

        verify(skillRepository, times(1)).
                findAll(pageable);

        assertEquals(page, skills);
    }

    @Test
    public void FindAll_Success() {
        List<Skill> skills = Collections.singletonList(skill);
        given(skillRepository.findAll()).willReturn(skills);

        List<Skill> gotSkills = skillService.findAll();

        verify(skillRepository, times(1)).
                findAll();

        assertEquals(skills, gotSkills);
    }

    @Test
    public void FindAllSubString_Success() {
        List<Skill> skills = Collections.singletonList(skill);
        given(skillRepository.findAll(AUTOCOMPLETE_SUB_STR)).willReturn(skills);

        List<Skill> gotSkills = skillService.findAll(AUTOCOMPLETE_SUB_STR);

        verify(skillRepository, times(1)).
                findAll(AUTOCOMPLETE_SUB_STR);

        assertEquals(skills, gotSkills);
    }

    @Test
    public void findAllByCandidate_Success() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Skill> page = new PageImpl<>(Collections.singletonList(skill));
        given(skillRepository.findAllByCandidateId(CANDIDATE1_ID, pageable)).willReturn(page);

        Page<Skill> skills = skillService.findAllByCandidate(CANDIDATE1_ID, pageable);

        verify(skillRepository, times(1)).
                findAllByCandidateId(CANDIDATE1_ID, pageable);

        assertEquals(page, skills);
    }

    @Test
    public void findOne_Success() {
        given(skillRepository.findById(skill.getId())).willReturn(java.util.Optional.of(skill));

        Skill getSkill = skillService.findOne(skill.getId());

        verify(skillRepository, times(1)).
                findById(skill.getId());

        assertEquals(skill, getSkill);
    }

    @Test
    public void findOne_FalseSkillID_Fail() {
        given(skillRepository.findById(FALSE_SKILL_ID)).willReturn(java.util.Optional.empty());

        Skill getSkill = skillService.findOne(FALSE_SKILL_ID);

        verify(skillRepository, times(1)).
                findById(FALSE_SKILL_ID);

        assertNull(getSkill);
    }

    @Test
    public void findByName_Success() {
        given(skillRepository.findByName(skill.getName())).willReturn(skill);

        Skill getSkill = skillService.findByName(skill.getName());

        verify(skillRepository, times(1)).
                findByName(skill.getName());

        assertEquals(skill, getSkill);
    }

    @Test
    public void findByName_FalseSkillName_Fail() {
        given(skillRepository.findByName(NEW_SKILL_NAME)).willReturn(null);

        Skill getSkill = skillService.findByName(NEW_SKILL_NAME);

        verify(skillRepository, times(1)).
                findByName(NEW_SKILL_NAME);

        assertNull(getSkill);
    }

    @Test
    public void getSkillIds() {
        Skill skill1 = new Skill(SKILL1_NAME);
        Skill skill2 = new Skill(SKILL2_NAME);
        Skill skill3 = new Skill(NEW_SKILL_NAME);

        Skill FoundSkill1 = new Skill(SKILL1_ID, SKILL1_NAME);
        Skill FoundSkill2 = new Skill(SKILL2_ID, SKILL2_NAME);

        Set<Skill> skills = new HashSet<>(Arrays.asList(skill1, skill2, skill3));
        Set<Skill> expectedUpdatedSkills = new HashSet<>(Arrays.asList(FoundSkill1, FoundSkill2, skill3));

        given(skillRepository.findByName(SKILL1_NAME)).willReturn(FoundSkill1);
        given(skillRepository.findByName(SKILL2_NAME)).willReturn(FoundSkill2);
        given(skillRepository.findByName(NEW_SKILL_NAME)).willReturn(null);

        Set<Skill> updatedSkills = skillService.getSkillIds(skills);

        verify(skillRepository, times(1)).
                findByName(SKILL1_NAME);

        verify(skillRepository, times(1)).
                findByName(SKILL2_NAME);

        verify(skillRepository, times(1)).
                findByName(NEW_SKILL_NAME);

        assertEquals(expectedUpdatedSkills, updatedSkills);
    }
}