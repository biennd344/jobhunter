package vn.bin.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.bin.jobhunter.domain.User;
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

    public List<User> fetchAllUser() {

        return this.userReposiriory.findAll();
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

}
