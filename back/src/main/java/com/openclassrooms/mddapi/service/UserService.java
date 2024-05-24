package com.openclassrooms.mddapi.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.dto.UserLoginDTO;
import com.openclassrooms.mddapi.model.dto.UserRegisterDTO;
import com.openclassrooms.mddapi.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    private User registerDtoToEntity(UserRegisterDTO userRegisterDTO) {
        return modelMapper.map(userRegisterDTO, User.class);
    }

    public User register(UserRegisterDTO userDTO) {
        User user = registerDtoToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    public Boolean isUserEmailExists(String email) {
        if (userRepository.findByEmail(email) == null) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public Boolean canConnect(UserLoginDTO user) {
        User userRegistered = userRepository.findByEmail(user.getEmail());
        if (userRegistered == null) {
            return false;
        }
        return passwordEncoder.matches(user.getPassword(), userRegistered.getPassword());
    }

    public User getUser(String identifier) {
        return userRepository.findByEmail(identifier);
    }

    public User updateUser(User user) {
        User currentUser = userRepository.findById(user.getId()).orElse(null);
        if (user.getUsername() != null) { // TODO: check with front if null or empty
            currentUser.setUsername(user.getUsername());;
        }
        if (user.getEmail() != null) { // TODO: check with front if null or empty
            currentUser.setEmail(user.getEmail());
        }
        return userRepository.save(currentUser);
    }
}
