package vn.bin.jobhunter.controller;

import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.bin.jobhunter.domain.Resume;
import vn.bin.jobhunter.domain.Role;
import vn.bin.jobhunter.domain.response.ResultPaginationDTO;
import vn.bin.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.bin.jobhunter.service.RoleService;
import vn.bin.jobhunter.util.annotation.ApiMessage;
import vn.bin.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("create a roles")
    public ResponseEntity<Role> create(@Valid @RequestBody Role r) throws IdInvalidException {
        if (this.roleService.exitByName(r.getName())) {
            throw new IdInvalidException("roles voi name = " + r.getName() + " da ton tai");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(r));
    }

    @PutMapping("/roles")
    @ApiMessage("update a roles")
    public ResponseEntity<Role> update(@Valid @RequestBody Role r) throws IdInvalidException {

        if (this.roleService.fetchById(r.getId()) == null) {
            throw new IdInvalidException("role id = " + r.getId() + "k ton tai");
        }
        // if (this.roleService.exitByName(r.getName())) {
        // throw new IdInvalidException("role voi name = " + r.getName() + " da ton
        // tai");
        // }

        return ResponseEntity.ok().body(this.roleService.update(r));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a roles")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {

        if (this.roleService.fetchById(id) == null) {
            throw new IdInvalidException("permission id = " + id + "k ton tai");
        }
        this.roleService.delete(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/roles")
    @ApiMessage("get all roles")
    public ResponseEntity<ResultPaginationDTO> getRoles(@Filter Specification<Role> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.getRoles(spec, pageable));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch a roles by id")
    public ResponseEntity<Role> fetchById(@PathVariable("id") long id) throws IdInvalidException {
        Role role = this.roleService.fetchById(id);
        if (role == null) {
            throw new IdInvalidException("Resume voi id = " + id + "khong ton tai");
        }

        return ResponseEntity.ok().body(role);
    }

}
