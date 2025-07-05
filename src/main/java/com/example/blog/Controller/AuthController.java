package com.example.blog.Controller;

import com.example.blog.Dto.JwtResponse;
import com.example.blog.Dto.LoginRequest;
import com.example.blog.Dto.RegisterRequest;
import com.example.blog.Entity.UserEntity;
import com.example.blog.Repository.RoleRepository;
import com.example.blog.Repository.UserRepository;
import com.example.blog.Security.JwtHelper;
import com.example.blog.Services.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final CustomUserDetailService customUserDetailService;

    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("Email Already Exists");
        }

        UserEntity user = UserEntity.builder().name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(roleRepository.findById("ROLE_USER").orElseThrow()))
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("User Registered Successfully");

    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtHelper.generateToken(user.getEmail());

        return ResponseEntity.ok(
                JwtResponse.builder()
                        .token(token)
                        .userName(user.getName())
                        .role(user.getRoles().stream().findFirst().get().getRoleName())
                        .build());
    }


}
