package com.project.internship.api;

import com.project.internship.dto.SkillDTO;
import com.project.internship.helper.SkillMapper;
import com.project.internship.model.Skill;
import com.project.internship.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping(value = "/api/skills", produces = MediaType.APPLICATION_JSON_VALUE)
public class SkillController {

    @Autowired
    SkillService skillService;

    private final SkillMapper skillMapper;

    public SkillController() {
        skillMapper = new SkillMapper();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSkill(@Valid @RequestBody SkillDTO skillDTO) {

        Skill savedSkill = skillService.save(skillMapper.toEntity(skillDTO));

        if (savedSkill == null) {
            return new ResponseEntity<>("Name already in use.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(skillMapper.toDto(savedSkill), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{skillId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSkill(@PathVariable Integer skillId) {
        if (skillService.delete(skillId)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Candidates have selected skill, deleting failed.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSkill(@Valid @RequestBody SkillDTO skillDTO) {

        if (skillService.update(skillMapper.toEntity(skillDTO))) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Name already in use.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{skillId}", method = RequestMethod.GET)
    public ResponseEntity<?> getSkill(@PathVariable Integer skillId) {
        Skill skill = skillService.findOne(skillId);
        if (skill != null) {
            SkillDTO skillDTO = skillMapper.toDto(skill);
            return new ResponseEntity<>(skillDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>("Skill doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{candidateId}/by-page", method = RequestMethod.GET)
    public ResponseEntity<Page<SkillDTO>> getSkillsPageByCandidate(@PathVariable Integer candidateId, Pageable pageable) {
        Page<Skill> page = skillService.findAllByCandidate(candidateId, pageable);
        List<SkillDTO> dtos = skillMapper.toDTOList(page.toList());
        Page<SkillDTO> pageSkillDTOS = new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());

        return new ResponseEntity<>(pageSkillDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/by-page", method = RequestMethod.GET)
    public ResponseEntity<Page<SkillDTO>> getSkillsPage(Pageable pageable) {
        Page<Skill> page = skillService.findAll(pageable);
        List<SkillDTO> dtos = skillMapper.toDTOList(page.toList());
        Page<SkillDTO> pageSkillDTOS = new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());

        return new ResponseEntity<>(pageSkillDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/autocomplete/{subStrSkill}", method = RequestMethod.GET)
    public ResponseEntity<List<SkillDTO>> getSkillsForAutocomplete(@PathVariable String subStrSkill) {
        List<Skill> skills = skillService.findAll(subStrSkill);
        List<SkillDTO> dtos = skillMapper.toDTOList(skills);

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public ResponseEntity<List<SkillDTO>> getSkillsForSelect() {
        List<Skill> skills = skillService.findAll();
        List<SkillDTO> dtos = skillMapper.toDTOList(skills);

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
