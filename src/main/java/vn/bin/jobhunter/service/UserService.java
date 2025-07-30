package vn.bin.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.bin.jobhunter.domain.User;
import vn.bin.jobhunter.domain.response.ResCreateUserDTO;
import vn.bin.jobhunter.domain.response.ResUpdateUserDTO;
import vn.bin.jobhunter.domain.response.ResUserDTO;
import vn.bin.jobhunter.domain.response.ResultPaginationDTO;
import vn.bin.jobhunter.repository.UserReposiriory;

@Service
public class UserService {
    private final UserReposiriory userReposiriory;

    public UserService(UserReposiriory userReposiriory) {
        this.userReposiriory = userReposiriory;
    }

    public User handleCreateUser(User user) {

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
        List<ResUserDTO> listUser = pageUser.getContent().stream().map(item -> new ResUserDTO(
                item.getId(),
                item.getEmail(),
                item.getName(),
                item.getGender(),
                item.getAddress(),
                item.getAge(),
                item.getUpdatedAt(),
                item.getCreatedAt()))
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
