package com.paypal.user_service.controller;

import com.paypal.user_service.dto.JwtResponse;
import com.paypal.user_service.dto.LoginRequest;
import com.paypal.user_service.dto.SignupRequest;
import com.paypal.user_service.entity.User;
import com.paypal.user_service.repository.UserRepository;
import com.paypal.user_service.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
private  final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
private final JWTUtil jwtUtil;
 @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
     Optional<User> existingUser=userRepository.findByEmail(signupRequest.getEmail());
     if(existingUser.isPresent()){
         return ResponseEntity.badRequest().body("User already Exists");
     }
     User user=new User();
     user.setName(signupRequest.getName());
     user.setEmail(signupRequest.getEmail());
     user.setRole("ROLE_USER");
     user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
     User savedUser=userRepository.save(user);
     return ResponseEntity.ok("User registered successfully");

 }
 @PostMapping("/login")
    public  ResponseEntity<?>  login(@RequestBody LoginRequest loginRequest) {
     Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
     if(userOptional.isEmpty()){
         return ResponseEntity.status(401).body("User not Found");
     }
     User user= userOptional.get();
     if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
         return ResponseEntity.status(401).body("Invalid credentials");
     }
     Map<String,Object> claims=new HashMap<>();
     claims.put("role",user.getRole()); // making role
     String token=jwtUtil.generateToken(claims, user.getEmail());
     return  ResponseEntity.ok(new JwtResponse(token));
 }







}






