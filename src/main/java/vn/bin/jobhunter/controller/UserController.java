package vn.bin.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.bin.jobhunter.domain.User;
import vn.bin.jobhunter.service.UserService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User user) {

        User userResult = this.userService.handleCreateUser(user);
        return userResult;
    }

    @DeleteMapping("/user/{id}")
    public String DeleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return "binnn";
    }

    @GetMapping("/user/{id}")
    public User fetchUserById(@PathVariable("id") long id) {
        User user = this.userService.fetchUserById(id);
        return user;
    }

    @GetMapping("/user")
    public List<User> fetchAllUser() {
        List<User> users = this.userService.fetchAllUser();
        return users;
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {

        User userResult = this.userService.handleUpdateUser(user);
        return userResult;
    }
}