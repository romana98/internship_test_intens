package com.project.internship.helper;

import com.project.internship.dto.SkillDTO;
import com.project.internship.model.Skill;

import java.util.ArrayList;
import java.util.List;

public class SkillMapper implements MapperInterface<Skill, SkillDTO> {

    @Override
    public Skill toEntity(SkillDTO dto) {
        return new Skill(dto.getId(), dto.getName());
    }

    @Override
    public SkillDTO toDto(Skill entity) {
        return new SkillDTO(entity.getId(), entity.getName());
    }

    @Override
    public List<SkillDTO> toDTOList(List<Skill> skills) {
        ArrayList<SkillDTO> dtos = new ArrayList<>();
        for (Skill skill : skills) {
            SkillDTO dto = toDto(skill);
            dtos.add(dto);
        }
        return dtos;
    }
}
