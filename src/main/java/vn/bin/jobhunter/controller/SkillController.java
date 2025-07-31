package vn.bin.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.bin.jobhunter.domain.Skill;
import vn.bin.jobhunter.domain.response.ResultPaginationDTO;
import vn.bin.jobhunter.service.SkillService;
import vn.bin.jobhunter.util.annotation.ApiMessage;
import vn.bin.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("create a skill")
    public ResponseEntity<Skill> create(@Valid @RequestBody Skill s) throws IdInvalidException {
        if (s.getName() != null && this.skillService.isNameExist(s.getName())) {
            throw new IdInvalidException("Skill name = " + s.getName() + "da ton tai");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(s));
    }

    @PutMapping("/skills")
    @ApiMessage("update a skill")
    public ResponseEntity<Skill> update(@Valid @RequestBody Skill s) throws IdInvalidException {
        Skill currentSkill = this.skillService.fetchSkillById(s.getId());
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + s.getId() + "k ton tai");
        }
        if (s.getName() != null && this.skillService.isNameExist(s.getName())) {
            throw new IdInvalidException("Skill name = " + s.getName() + "da ton tai");
        }
        currentSkill.setName((s.getName()));

        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.updateSkill(currentSkill));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.fetchSkillById(id);
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + id + "k ton tai");
        }
        this.skillService.deleteSkill(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/skills")
    @ApiMessage("get all skills")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Skill> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchAllSkill(spec, pageable));
    }

}
