package com.example.learn.user;

import com.example.learn.auth.dto.CustomUserDetails;
import com.example.learn.common.dto.PageRequestDto;
import com.example.learn.user.dto.UserFilter;
import com.example.learn.user.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    public UserController(UserService service) {
        userService = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getUsers(PageRequestDto pageRequestDto, UserFilter userFilter) {
        Page<UserResponse> users = userService.getAllUsers(pageRequestDto, userFilter);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> profile(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getProfile(authentication));
    }
}
