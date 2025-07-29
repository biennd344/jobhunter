package vn.bin.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.bin.jobhunter.domain.User;
import vn.bin.jobhunter.domain.dto.Meta;
import vn.bin.jobhunter.domain.dto.ResultPaginationDTO;
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
        Meta mt = new Meta();
        mt.setPage(pageUser.getNumber() + 1);
        mt.setPageSize(pageUser.getSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());
        return rs;
    }

    public User handleUpdateUser(User requser) {
        User currentUser = this.fetchUserById(requser.getId());
        if (currentUser != null) {
            currentUser.setEmail(requser.getEmail());
            currentUser.setName(requser.getName());
            currentUser.setPassword(requser.getPassword());
            // update
            currentUser = this.userReposiriory.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userReposiriory.findByEmail(username);
    }

}
