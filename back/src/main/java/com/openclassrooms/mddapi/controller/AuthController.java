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

    /**
     * Endpoint to create a new user
     * @param user information about the user
     * @return token to authenticated
     */
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

    /**
     * Endpoint to login a user already registered
     * @param user information about the user
     * @return token to authenticated
     */
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

    /**
     * Endpoint to retrieve all informations about the current user
     * @param request token for authorisation
     * @return user information about the user
     */
    @GetMapping("/me")
    public ResponseEntity<User> getUser(HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        User user = userService.getUser(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Endpoint to update information about the user
     * @param request token for authorisation
     * @param user new information about the user
     * @return nothing
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateUser(HttpServletRequest request, @RequestBody UserRegisterDTO user) {
        Boolean isNewPassword = false;
        String email = request.getUserPrincipal().getName();
        User currentUser = userService.getUser(email);

        if (!user.getUsername().isEmpty()) {
            currentUser.setUsername(user.getUsername());
        }

        if (!user.getEmail().isEmpty()) {
            if (userService.isUserEmailExists(user.getEmail())) {
                return ResponseEntity.status(409).body("Email already exist");
            } else {
                currentUser.setEmail(user.getEmail());
            }
        }

        if (!user.getPassword().isEmpty()) {
            if (!userService.isValidPassword(user.getPassword())) {
                return ResponseEntity.badRequest().body("Password does not meet the conditions");
            } else {
                isNewPassword = true;
                currentUser.setPassword(user.getPassword());
            }
        }

        userService.updateUser(currentUser, isNewPassword);
        return ResponseEntity.ok().build();
    }
}
