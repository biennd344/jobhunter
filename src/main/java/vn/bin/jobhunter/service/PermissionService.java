package vn.bin.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import vn.bin.jobhunter.domain.Permission;
import vn.bin.jobhunter.domain.Skill;
import vn.bin.jobhunter.domain.response.ResultPaginationDTO;
import vn.bin.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExits(Permission p) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(
                p.getModule(),
                p.getApiPath(),
                p.getMethod());

    }

    public Permission create(Permission p) {
        return this.permissionRepository.save(p);
    }

    public Permission fetchById(long id) {
        Optional<Permission> perOptional = this.permissionRepository.findById(id);
        if (perOptional.isPresent())
            return perOptional.get();

        return null;

    }

    public Permission update(Permission p) {
        Permission permissionDB = this.fetchById(p.getId());
        if (permissionDB != null) {
            permissionDB.setName(p.getName());
            permissionDB.setApiPath(p.getApiPath());
            permissionDB.setMethod(p.getMethod());
            permissionDB.setModule(p.getModule());
            permissionDB = this.permissionRepository.save(permissionDB);
            return permissionDB;

        }
        return null;
    }

    public void delete(long id) {

        Optional<Permission> permissOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        this.permissionRepository.delete(currentPermission);

    }

    public ResultPaginationDTO getPermission(Specification<Permission> spec, Pageable pageable) {

        Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pagePermission.getTotalPages());
        mt.setTotal(pagePermission.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pagePermission.getContent());

        return rs;

    }

}
