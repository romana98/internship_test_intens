package com.project.internship.api;

import com.project.internship.dto.SkillDTO;
import com.project.internship.helper.SkillMapper;
import com.project.internship.model.Skill;
import com.project.internship.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/skills", produces = MediaType.APPLICATION_JSON_VALUE)
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
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(skillMapper.toDto(savedSkill), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSkill() {

       List<Skill> skills = skillService.findAllByCandidateId(1);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
