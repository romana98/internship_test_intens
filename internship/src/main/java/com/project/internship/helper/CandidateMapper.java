package com.project.internship.helper;

import com.project.internship.dto.CandidateDTO;
import com.project.internship.model.Candidate;
import com.project.internship.model.Skill;

import java.util.ArrayList;
import java.util.List;

public class CandidateMapper implements MapperInterface<Candidate, CandidateDTO> {

    @Override
    public Candidate toEntity(CandidateDTO dto) {
        Candidate candidate = new Candidate(dto.getId(), dto.getFullName(), dto.getDateOfBirth(), dto.getContactNumber(), dto.getEmail());
        for (String skillName : dto.getSkills()) {
            candidate.getSkills().add(new Skill(skillName));
        }
        return candidate;
    }

    @Override
    public CandidateDTO toDto(Candidate entity) {
        CandidateDTO candidateDTO = new CandidateDTO(entity.getId(), entity.getFullName(), entity.getContactNumber(), entity.getEmail(), entity.getDateOfBirth());
        for (Skill skill : entity.getSkills()) {
            candidateDTO.getSkills().add(skill.getName());
        }
        return candidateDTO;
    }

    @Override
    public List<CandidateDTO> toDTOList(List<Candidate> candidates) {
        ArrayList<CandidateDTO> dtos = new ArrayList<>();
        for(Candidate candidate : candidates) {
            CandidateDTO dto = toDto(candidate);
            dtos.add(dto);
        }
        return dtos;
    }
}
