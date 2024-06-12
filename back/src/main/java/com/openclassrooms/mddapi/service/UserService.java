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

    /**
     * Permit to convert user DTO to user entity
     * @param userRegisterDTO to convert
     * @return user entity
     */
    private User registerDtoToEntity(UserRegisterDTO userRegisterDTO) {
        return modelMapper.map(userRegisterDTO, User.class);
    }

    /**
     * Permit to register a new user
     * @param userDTO information about the new user
     * @return user ceated
     */
    public User register(UserRegisterDTO userDTO) {
        User user = registerDtoToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Check if user email already exists
     * @param email to check
     * @return true if exist and false if not
     */
    public Boolean isUserEmailExists(String email) {
        if (userRepository.findByEmail(email) == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check if is valid password
     * @param password to check
     * @return true if valid and false if not
     */
    public Boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * Check if the user can connect
     * @param user information about the user
     * @return true if he can and false if not
     */
    public Boolean canConnect(UserLoginDTO user) {
        User userRegistered = userRepository.findByEmail(user.getEmail());
        if (userRegistered == null) {
            return false;
        }
        return passwordEncoder.matches(user.getPassword(), userRegistered.getPassword());
    }

    /**
     * Permit to get user
     * @param identifier email of the user
     * @return user
     */
    public User getUser(String identifier) {
        return userRepository.findByEmail(identifier);
    }

    /**
     * Permit to update information about the user
     * @param user to update
     * @param isNewPassword his modified password
     * @return user updated
     */
    public User updateUser(User user, Boolean isNewPassword) {
        User newUser = user;
        if (isNewPassword) {
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(newUser);
    }
}
