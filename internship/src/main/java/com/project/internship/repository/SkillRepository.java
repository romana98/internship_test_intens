package com.project.internship.repository;

import com.project.internship.model.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {

    @Query("SELECT skill FROM Skill skill JOIN skill.candidates candidate WHERE candidate.id = ?1")
    Page<Skill> findAllByCandidateId(Integer candidateId, Pageable pageable);

    @Query("SELECT skill FROM Candidate candidate JOIN candidate.skills skill WHERE skill.id = ?1")
    List<Skill> findAllByReference(Integer skillId);

    Skill findByName(String name);
}
