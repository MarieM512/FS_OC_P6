package com.openclassrooms.mddapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.dto.UserLoginDTO;
import com.openclassrooms.mddapi.model.dto.UserRegisterDTO;
import com.openclassrooms.mddapi.service.JWTService;
import com.openclassrooms.mddapi.service.UserService;

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
        if (user.getUsername().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()) { // TODO: Check empty fields in front (null or isEmpty())
            return ResponseEntity.badRequest().body("Please fill all fields");
        } else if (userService.isUserEmailExists(user)) {
            return ResponseEntity.status(409).body("Email already exist");
        } else if (userService.isUserUsernameExists(user)) {
            return ResponseEntity.status(409).body("Username already exist");
        } else {
            userService.register(user);
            String token = jwtService.generateToken(user.getEmail());
            return ResponseEntity.ok(token);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO user) {
        if (user.getIdentifier() == null || user.getPassword() == null) { // TODO: Check empty fields in front (null or isEmpty())
            return ResponseEntity.badRequest().body("Please fill all fields");
        } else if (userService.canConnect(user)) {
            String token = jwtService.generateToken(user.getIdentifier());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Email / Username or password invalid");
        }
    }
}
