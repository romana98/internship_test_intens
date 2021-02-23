package com.project.internship.repository;

import com.project.internship.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    Candidate findByEmailOrContactNumber(String email, String contactNumber);

    @Query("SELECT candidate FROM Candidate candidate JOIN candidate.skills skill WHERE upper(skill.name) like upper(CONCAT('%', ?1, '%')) ")
    Page<Candidate> findAllBySkillName(String skillName, Pageable pageable);

    @Query("SELECT candidate FROM Candidate candidate WHERE upper(candidate.fullName) like upper(CONCAT('%', ?1, '%'))")
    Page<Candidate> findAllByName(String name, Pageable pageable);

    @Query("SELECT candidate FROM Candidate candidate JOIN candidate.skills skill WHERE upper(candidate.fullName) like upper(CONCAT('%', ?1, '%')) and upper(skill.name) like upper(CONCAT('%', ?2, '%'))")
    Page<Candidate> findAllByNameAndSkillName(String name, String skillName, Pageable pageable);

}
