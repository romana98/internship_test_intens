package com.project.internship.repository;

import com.project.internship.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {

    @Query(value = "SELECT * from skills where candidate_id = '%'", nativeQuery = true)
    List<Skill> findAllByCandidateId(Integer candidateId);
}
