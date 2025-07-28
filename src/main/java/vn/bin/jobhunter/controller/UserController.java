package vn.bin.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.bin.jobhunter.domain.User;
import vn.bin.jobhunter.service.UserService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        User userResult = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResult);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> DeleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        // return ResponseEntity.ok("bin");
        return ResponseEntity.status(HttpStatus.OK).body("bin");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable("id") long id) {
        User user = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> fetchAllUser() {
        List<User> users = this.userService.fetchAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {

        User userResult = this.userService.handleUpdateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResult);
    }
}