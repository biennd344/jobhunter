package vn.bin.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.bin.jobhunter.domain.User;
import vn.bin.jobhunter.domain.dto.ResCreateUserDTO;
import vn.bin.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.bin.jobhunter.domain.dto.ResUserDTO;
import vn.bin.jobhunter.domain.dto.ResultPaginationDTO;
import vn.bin.jobhunter.service.UserService;
import vn.bin.jobhunter.util.annotation.ApiMessage;
import vn.bin.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("create a new users")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User user) throws IdInvalidException {

        boolean isEmailExist = this.userService.isEmailExist(user.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email" + user.getEmail() + "da ton tai , vui long su dung email khac");
        }
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        User userResult = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(userResult));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a users")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("User voi id = " + id + "k ton tai");
        }
        this.userService.handleDeleteUser(id);
        // return ResponseEntity.ok("bin");
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch a users by id")
    public ResponseEntity<ResUserDTO> fetchUserById(@PathVariable("id") long id) throws IdInvalidException {
        User fetchUser = this.userService.fetchUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("User voi id = " + id + "k ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(fetchUser));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> fetchAllUser(@Filter Specification<User> specification,
            Pageable pageable)

    {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(specification, pageable));
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {

        User userResult = this.userService.handleUpdateUser(user);
        if (userResult == null) {
            throw new IdInvalidException("User voi id = " + userResult.getId() + "k ton tai");
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUpdateUserDTO(userResult));
    }
}