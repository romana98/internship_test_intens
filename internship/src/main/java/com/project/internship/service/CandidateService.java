package com.project.internship.service;

import com.project.internship.model.Candidate;
import com.project.internship.model.Skill;
import com.project.internship.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CandidateService {

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    SkillService skillService;

    public Candidate save(Candidate candidate) {
        if (candidateRepository.findByEmailOrContactNumber(candidate.getEmail(), candidate.getContactNumber()) == null) {
            Set<Skill> updatedSkills = skillService.getSkillIds(candidate.getSkills());
            candidate.setSkills(updatedSkills);
            candidate = candidateRepository.save(candidate);
            return candidate;
        }
        return null;
    }

    public boolean delete(Integer candidateId) {
        Candidate candidate = findOne(candidateId);
        if (candidate != null) {
            candidateRepository.delete(candidate);
            return true;
        }
        return false;
    }

    public boolean update(Candidate candidate) {
        Candidate oldCandidate = findOne(candidate.getId());
        if (oldCandidate != null) {
            Set<Skill> updatedSkills = skillService.getSkillIds(candidate.getSkills());
            candidate.setSkills(updatedSkills);
            candidateRepository.save(candidate);
            return true;
        }
        return false;
    }

    public Page<Candidate> findAll(Pageable pageable) {
        return candidateRepository.findAll(pageable);
    }

    public Candidate findOne(Integer candidateId) {
        return candidateRepository.findById(candidateId).orElse(null);
    }
}
