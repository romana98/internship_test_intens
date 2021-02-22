package com.project.internship.service;

import com.project.internship.model.Skill;
import com.project.internship.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SkillService {

    @Autowired
    SkillRepository skillRepository;

    public Skill save(Skill skill) {
        if (skill != null) {
            skill = skillRepository.save(skill);
            return skill;
        }
        return null;
    }

    public boolean delete(Integer skillId) {
        Skill skill = findOne(skillId);
        if (skill != null) {
            skillRepository.delete(skill);
            return true;
        }
        return false;
    }

    public boolean update(Skill skill) {
        Skill oldSkill = findOne(skill.getId());
        if (oldSkill != null) {
            skillRepository.save(skill);
            return true;
        }
        return false;
    }

    public Page<Skill> findAll(Pageable pageable) {
        return skillRepository.findAll(pageable);
    }

    public List<Skill> findAllByCandidateId(Integer candidateId) {
        return skillRepository.findAllByCandidateId(candidateId);
    }

    public Skill findOne(Integer skillId) {
        return skillRepository.findById(skillId).orElse(null);
    }

    public Set<Skill> getSkillIds(Set<Skill> skills) {
        Set<Skill> updatedSkills = new HashSet<>();
        for (Skill skill : skills) {
            Skill foundSkill = skillRepository.findByName(skill.getName());
            if (foundSkill != null) {
                updatedSkills.add(foundSkill);
            } else {
                updatedSkills.add(skill);
            }
        }
        return updatedSkills;
    }
}
