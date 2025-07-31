package vn.bin.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.bin.jobhunter.domain.Company;
import vn.bin.jobhunter.domain.Role;
import vn.bin.jobhunter.domain.User;
import vn.bin.jobhunter.domain.response.ResCreateUserDTO;
import vn.bin.jobhunter.domain.response.ResUpdateUserDTO;
import vn.bin.jobhunter.domain.response.ResUserDTO;
import vn.bin.jobhunter.domain.response.ResultPaginationDTO;
import vn.bin.jobhunter.repository.UserReposiriory;

@Service
public class UserService {
    private final UserReposiriory userReposiriory;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserService(UserReposiriory userReposiriory, CompanyService companyService, RoleService roleService) {
        this.userReposiriory = userReposiriory;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    public User handleCreateUser(User user) {
        if (user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }
        if (user.getRole() != null) {
            Role r = this.roleService.fetchById(user.getRole().getId());
            user.setRole(r != null ? r : null);
        }
        return this.userReposiriory.save(user);
    }

    public void handleDeleteUser(long id) {

        this.userReposiriory.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userReposiriory.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = this.userReposiriory.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageUser.getNumber() + 1);
        mt.setPageSize(pageUser.getSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        List<ResUserDTO> listUser = pageUser.getContent().stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listUser);
        return rs;
    }

    public User handleUpdateUser(User requser) {
        User currentUser = this.fetchUserById(requser.getId());
        if (currentUser != null) {
            currentUser.setAddress(requser.getAddress());
            currentUser.setGender(requser.getGender());
            currentUser.setAge(requser.getAge());
            currentUser.setName(requser.getName());
            if (requser.getCompany() != null) {
                Optional<Company> companyOptional = this.companyService.findById(requser.getCompany().getId());
                currentUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
            }
            if (requser.getRole() != null) {
                Role r = this.roleService.fetchById(requser.getRole().getId());
                requser.setRole(r != null ? r : null);
            }
            // update
            currentUser = this.userReposiriory.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userReposiriory.findByEmail(username);
    }

    public boolean isEmailExist(String email) {
        return this.userReposiriory.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }

        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        return res;

    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();
        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }
        if (user.getRole() != null) {
            roleUser.setId(user.getCompany().getId());
            roleUser.setName(user.getCompany().getName());
            res.setRole(roleUser);
        }
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdateAt(user.getUpdatedAt());
        return res;

    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser com = new ResUpdateUserDTO.CompanyUser();
        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setUpdateAt(user.getUpdatedAt());
        return res;

    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userReposiriory.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userReposiriory.findByRefreshTokenAndEmail(token, email);
    }

}
