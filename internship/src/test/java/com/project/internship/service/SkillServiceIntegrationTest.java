package com.project.internship.service;

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
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.project.internship.contants.CandidateConstants.CANDIDATE1_ID;
import static com.project.internship.contants.SkillConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class SkillServiceIntegrationTest {

    @Autowired
    SkillService skillService;

    @Test
    @Transactional
    @Rollback
    public void save_Success() {
        Skill saveSkill = new Skill(NEW_SKILL_NAME);

        Skill savedSkill = skillService.save(saveSkill);

        assertEquals(NEW_SKILL_NAME, savedSkill.getName());
    }

    @Test
    public void save_UniqueConstraint_Fail() {
        Skill saveSkill = new Skill(SKILL_UNIQUE_DEP_NAME);

        Skill savedSkill = skillService.save(saveSkill);

        assertNull(savedSkill);
    }

    @Test
    @Transactional
    @Rollback
    public void delete_Success() {
        boolean isDeleted = skillService.delete(DELETE_SKILL_ID);

        assertTrue(isDeleted);
    }

    @Test
    public void delete_DependencyConstraint_Fail() {
        boolean isDeleted = skillService.delete(SKILL_ID);

        assertFalse(isDeleted);
    }

    @Test
    @Transactional
    @Rollback
    public void update_Success() {
        Skill updateSkill = new Skill(SKILL_ID, NEW_SKILL_NAME);

        boolean updatedSkill = skillService.update(updateSkill);

        assertTrue(updatedSkill);
    }

    @Test
    public void update_FalseSkillId_Fail() {
        Skill updateSkill = new Skill(FALSE_SKILL_ID, NEW_SKILL_NAME);

        boolean updatedSkill = skillService.update(updateSkill);

        assertFalse(updatedSkill);
    }

    @Test
    public void update_UniqueConstraint_Fail() {
        Skill updateSkill = new Skill(DELETE_SKILL_ID, SKILL_UNIQUE_DEP_NAME);

        boolean updatedSkill = skillService.update(updateSkill);

        assertFalse(updatedSkill);
    }

    @Test
    public void findAllPageable_Success() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Skill> skills = skillService.findAll(pageable);

        assertEquals(4, skills.getContent().size());
    }

    @Test
    public void FindAll_Success() {
        List<Skill> skills = skillService.findAll();

        assertEquals(4, skills.size());
    }

    @Test
    public void FindAllSubString_Success() {
        List<Skill> skills = skillService.findAll(AUTOCOMPLETE_SUB_STR_I);

        assertEquals(2, skills.size());

    }

    @Test
    public void findAllByCandidate_Success() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<Skill> skills = skillService.findAllByCandidate(CANDIDATE1_ID, pageable);

        assertEquals(3, skills.getContent().size());
    }

    @Test
    public void findOne_Success() {
        Skill skill = skillService.findOne(SKILL_ID);

        assertEquals("Java Programming", skill.getName());
    }

    @Test
    public void findOne_FalseSkillID_Fail() {
        Skill getSkill = skillService.findOne(FALSE_SKILL_ID);

        assertNull(getSkill);
    }

    @Test
    public void findByName_Success() {
        Skill skill = skillService.findByName(SKILL_REMOVE_NAME);

        assertEquals(SKILL_REMOVE_NAME, skill.getName());
    }

    @Test
    public void findByName_FalseSkillName_Fail() {
        Skill skill = skillService.findByName(NEW_SKILL_NAME);

        assertNull(skill);
    }

    @Test
    public void getSkillIds() {
        Skill skill1 = new Skill(SKILL_REMOVE_NAME);
        Skill skill2 = new Skill(SKILL1_NAME);

        Set<Skill> skills = new HashSet<>(Arrays.asList(skill1, skill2));

        Set<Skill> updatedSkills = skillService.getSkillIds(skills);

        Skill[] updatedSkillsList = updatedSkills.toArray(new Skill[0]);

        for(Skill skill : updatedSkillsList){
            if(skill.getName().equals(SKILL_REMOVE_NAME)){
                assertEquals(SKILL_REMOVE_ID.longValue(), skill.getId());
                assertEquals(SKILL_REMOVE_NAME, skill.getName());
            }else{
                assertEquals(SKILL1_NAME, skill.getName());
            }
        }

        assertEquals(2, updatedSkills.size());

    }
}