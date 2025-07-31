package vn.bin.jobhunter.controller;

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
import vn.bin.jobhunter.domain.Permission;
import vn.bin.jobhunter.domain.Skill;
import vn.bin.jobhunter.domain.response.ResultPaginationDTO;
import vn.bin.jobhunter.service.PermissionService;
import vn.bin.jobhunter.util.annotation.ApiMessage;
import vn.bin.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionControler {

    private final PermissionService permissionService;

    public PermissionControler(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("create a permission")
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission p) throws IdInvalidException {
        if (this.permissionService.isPermissionExits(p)) {
            throw new IdInvalidException("Permission da ton tai");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.create(p));
    }

    @PutMapping("/permissions")
    @ApiMessage("update a permission")
    public ResponseEntity<Permission> update(@Valid @RequestBody Permission p) throws IdInvalidException {

        if (this.permissionService.fetchById(p.getId()) == null) {
            throw new IdInvalidException("Permission id = " + p.getId() + "k ton tai");
        }
        if (this.permissionService.isPermissionExits(p)) {
            if (this.permissionService.isSameName(p)) {
                throw new IdInvalidException("Permission name da ton tai");
            }

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.update(p));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {

        if (this.permissionService.fetchById(id) == null) {
            throw new IdInvalidException("permission id = " + id + "k ton tai");
        }
        this.permissionService.delete(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("get all permissions")
    public ResponseEntity<ResultPaginationDTO> getPermissions(@Filter Specification<Permission> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.getPermission(spec, pageable));
    }

}
