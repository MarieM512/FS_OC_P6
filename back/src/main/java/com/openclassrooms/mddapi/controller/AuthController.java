package com.openclassrooms.mddapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.dto.UserLoginDTO;
import com.openclassrooms.mddapi.model.dto.UserRegisterDTO;
import com.openclassrooms.mddapi.service.JWTService;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.model.response.LogResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    public AuthController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO user) {
        if (user.getUsername().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Please fill all fields");
        } else if (userService.isUserEmailExists(user.getEmail())) {
            return ResponseEntity.status(409).body("Email already exist");
        } else if (!userService.isValidPassword(user.getPassword())) {
            return ResponseEntity.badRequest().body("Password does not meet the conditions");
        } else {
            userService.register(user);
            String token = jwtService.generateToken(user.getEmail());
            LogResponse logResponse = new LogResponse(token);
            return ResponseEntity.ok(logResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Please fill all fields");
        } else if (userService.canConnect(user)) {
            String token = jwtService.generateToken(user.getEmail());
            LogResponse logResponse = new LogResponse(token);
            return ResponseEntity.ok(logResponse);
        } else {
            return ResponseEntity.status(401).body("Email or password invalid");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String email = jwtService.decodeToken(token);
            User user = userService.getUser(email);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateUser(HttpServletRequest request, @RequestBody UserRegisterDTO user) { // TODO: check le token
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String emailToken = jwtService.decodeToken(token);
            User currentUser = userService.getUser(emailToken);

            if (userService.isUserEmailExists(user.getEmail())) {
                return ResponseEntity.status(409).body("Email already exist");
            }

            if (!userService.isValidPassword(user.getPassword())) {
                return ResponseEntity.badRequest().body("Password does not meet the conditions");
            }

            currentUser.setUsername(user.getUsername());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());

            User userRegistered = userService.updateUser(currentUser);
            return ResponseEntity.ok(userRegistered);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}
