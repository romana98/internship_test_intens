package com.project.internship.repository;

import com.project.internship.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    Candidate findByEmailAndIdIsNotOrContactNumberAndIdIsNot(String email, Integer id, String contactNumber, Integer id1);

    @Query("SELECT DISTINCT candidate FROM Candidate candidate JOIN candidate.skills skill WHERE skill.name in (?1) " +
            "group by candidate.id having count(skill) =?2")
    Page<Candidate> findAllBySkillsName(List<String> skillsName, Long listSize, Pageable pageable);

    @Query("SELECT DISTINCT candidate FROM Candidate candidate WHERE upper(candidate.fullName) like upper(CONCAT('%', ?1, '%'))")
    Page<Candidate> findAllByName(String name, Pageable pageable);

    @Query("SELECT DISTINCT candidate FROM Candidate candidate JOIN candidate.skills skill WHERE upper(candidate.fullName) " +
            "like upper(CONCAT('%', ?1, '%')) and  skill.name in (?2) group by candidate.id having count(skill) =?3")
    Page<Candidate> findAllByNameAndSkillName(String name, List<String> skillsName, Long listSize, Pageable pageable);

}
