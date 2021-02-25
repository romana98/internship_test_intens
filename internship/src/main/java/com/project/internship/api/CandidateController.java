package com.project.internship.api;

import com.project.internship.dto.CandidateDTO;
import com.project.internship.dto.SearchDTO;
import com.project.internship.dto.SkillDTO;
import com.project.internship.helper.CandidateMapper;
import com.project.internship.helper.SkillMapper;
import com.project.internship.model.Candidate;
import com.project.internship.service.CandidateService;
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
@RequestMapping(value = "/api/candidates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CandidateController {

    @Autowired
    CandidateService candidateService;

    private final CandidateMapper candidateMapper;
    private final SkillMapper skillMapper;

    public CandidateController() {
        candidateMapper = new CandidateMapper();
        skillMapper = new SkillMapper();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCandidate(@Valid @RequestBody CandidateDTO candidateDTO) {

        Candidate savedCandidate = candidateService.save(candidateMapper.toEntity(candidateDTO));

        if (savedCandidate == null) {
            return new ResponseEntity<>("Email or contact number already in use.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(candidateMapper.toDto(savedCandidate), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{candidateId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCandidate(@PathVariable Integer candidateId) {
        if (candidateService.delete(candidateId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Error occurred while deleting candidate", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCandidate(@Valid @RequestBody CandidateDTO candidateDTO) {

        if (candidateService.update(candidateMapper.toEntity(candidateDTO))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Email or contact number already in use.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/by-page", method = RequestMethod.GET)
    public ResponseEntity<Page<CandidateDTO>> getCandidatesPage(Pageable pageable) {
        Page<Candidate> page = candidateService.findAll(pageable);
        List<CandidateDTO> dtos = candidateMapper.toDTOList(page.toList());
        Page<CandidateDTO> pageCandidateDTOS = new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());

        return new ResponseEntity<>(pageCandidateDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/{candidateId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCandidate(@PathVariable Integer candidateId) {
        Candidate candidate = candidateService.findOne(candidateId);
        if (candidate != null) {
            CandidateDTO candidateDTO = candidateMapper.toDto(candidate);
            return new ResponseEntity<>(candidateDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>("Candidate doesn't exist", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{candidateId}/{skillId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeSkillFromCandidate(@PathVariable Integer candidateId, @PathVariable Integer skillId) {
        if (candidateService.removeSkill(candidateId, skillId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Error occurred while removing skill", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{candidateId}", method = RequestMethod.PUT)
    public ResponseEntity<?> addSkillToCandidate(@PathVariable Integer candidateId, @RequestBody SkillDTO skillDTO) {
        if (candidateService.addSkill(candidateId, skillMapper.toEntity(skillDTO))) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Error occurred while adding skill", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CandidateDTO>> searchCandidates(@RequestBody SearchDTO searchDTO, Pageable pageable) {
        Page<Candidate> page = candidateService.search(searchDTO, pageable);
        List<CandidateDTO> candidateDTOs = candidateMapper.toDTOList(page.toList());
        Page<CandidateDTO> candidateDTOPage = new PageImpl<>(candidateDTOs, page.getPageable(), page.getTotalElements());

        return new ResponseEntity<>(candidateDTOPage, HttpStatus.OK);
    }
}
