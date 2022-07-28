package com.guavapay.user.service;

import com.guavapay.user.domain.User;
import com.guavapay.user.enums.UserRole;
import com.guavapay.user.repository.UserRepository;
import com.guavapay.user.request.UserCreateRequest;
import com.guavapay.user.request.UserRegisterRequest;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User register(UserRegisterRequest request) {
        var userCreateRequest = UserCreateRequest.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .userRole(UserRole.USER)
                .build();
        return save(userCreateRequest);

    }

    public User save(UserCreateRequest request) {
        var builder = User.builder();
        builder.name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .enabled(true)
                .enabled(true)
                .locked(false)
                .expired(false)
                .failureLoginCount(0)
                .userRole(request.getUserRole());
        var user = builder.build();
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Page<User> findAll(Predicate predicate, Pageable pageable) {
        return userRepository.findAll(predicate, pageable);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


}
