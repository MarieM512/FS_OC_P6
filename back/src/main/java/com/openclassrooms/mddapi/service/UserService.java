package com.openclassrooms.mddapi.service;

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

    public Boolean isUserEmailExists(UserRegisterDTO user) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean isUserUsernameExists(UserRegisterDTO user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean canConnect(UserLoginDTO user) {
        User userRegistered = userRepository.findByUsername(user.getIdentifier());
        if (user.getIdentifier().contains("@") && user.getIdentifier().contains(".")) {
            userRegistered = userRepository.findByEmail(user.getIdentifier());
        }
        if (userRegistered == null) {
            return false;
        }
        return passwordEncoder.matches(user.getPassword(), userRegistered.getPassword());
    }

    public User getUser(String identifier) {
        if (identifier.contains("@") && identifier.contains(".")) {
            return userRepository.findByEmail(identifier);
        } else {
            return userRepository.findByUsername(identifier);
        }
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
