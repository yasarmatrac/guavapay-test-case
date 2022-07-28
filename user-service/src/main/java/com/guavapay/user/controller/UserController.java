package com.guavapay.user.controller;


import com.guavapay.user.domain.User;
import com.guavapay.user.dto.UserDTO;
import com.guavapay.user.mapper.UserMapper;
import com.guavapay.user.request.UserCreateRequest;
import com.guavapay.user.request.UserRegisterRequest;
import com.guavapay.user.service.UserService;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Register normal user by given information")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegisterRequest request) {
        var result = userService.register(request);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.user"})
    @Operation(summary = "Create user by given information")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateRequest request) {
        var result = userService.save(request);
        return ResponseEntity.ok(userMapper.map(result));
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.user"})
    @Operation(summary = "Get users by given query")
    @GetMapping
    public Page<UserDTO> findAll(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
        return userService.findAll(predicate, pageable).map(userMapper::map);
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.user"})
    @Operation(summary = "Get users by given id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.of(userService.findById(id).map(userMapper::map));
    }

    @SecurityRequirement(name = "Oauth2", scopes = {"api.user"})
    @Operation(summary = "Get logged user")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> loggedUser() {
        return ResponseEntity.of(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(userMapper::map));
    }
}
