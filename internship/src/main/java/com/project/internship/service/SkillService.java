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
        if (skillRepository.findByName(skill.getName()) == null) {
            skill = skillRepository.save(skill);
            return skill;
        }
        return null;
    }

    public boolean delete(Integer skillId) {
        List<Skill> skills = skillRepository.findAllByReference(skillId);
        if (skills.isEmpty()) {
            skillRepository.delete(findOne(skillId));
            return true;
        }
        return false;
    }

    public boolean update(Skill skill) {
        Skill oldSkill = findOne(skill.getId());
        Skill existSkill = findByName(skill.getName());
        if (oldSkill != null && existSkill == null) {
            skillRepository.save(skill);
            return true;
        }
        return false;
    }

    public Page<Skill> findAll(Pageable pageable) {
        return skillRepository.findAll(pageable);
    }

    public Page<Skill> findAllByCandidate(Integer candidateId, Pageable pageable) {
        return skillRepository.findAllByCandidateId(candidateId, pageable);
    }

    public Skill findOne(Integer skillId) {
        return skillRepository.findById(skillId).orElse(null);
    }

    public Skill findByName(String name) {
        return skillRepository.findByName(name);
    }

    public Set<Skill> getSkillIds(Set<Skill> skills) {
        Set<Skill> updatedSkills = new HashSet<>();
        for (Skill skill : skills) {
            Skill foundSkill = findByName(skill.getName());
            if (foundSkill != null) {
                updatedSkills.add(foundSkill);
            } else {
                updatedSkills.add(skill);
            }
        }
        return updatedSkills;
    }
}
