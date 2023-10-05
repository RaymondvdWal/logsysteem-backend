package com.example.logsysteem.services;


import com.example.logsysteem.dtos.UserDto;
import com.example.logsysteem.exceptions.RecordNotFoundException;
import com.example.logsysteem.exceptions.UserNotFoundException;
import com.example.logsysteem.model.Authority;
import com.example.logsysteem.model.ProfilePicture;
import com.example.logsysteem.model.User;
import com.example.logsysteem.repository.ProfilePictureRepository;
import com.example.logsysteem.repository.UserRepository;
import com.example.logsysteem.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfilePictureRepository profilePictureRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, ProfilePictureRepository profilePictureRepository) {
        this.userRepository = userRepository;
        this.profilePictureRepository = profilePictureRepository;
    }

    public List<UserDto> getUsers() {
        List<UserDto> collection = new ArrayList<>();
        List<User> list =userRepository.findAll();
        for (User user : list) {
            collection.add(fromUser(user));
        }
        return collection;
    }

    public UserDto getUser(String username) {

        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()){
            throw new UserNotFoundException(username);
        }

        User user = optionalUser.get();

        return fromUser(user);
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public String createUser(UserDto userDto) {
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userDto.setApikey(randomString);
        User newUser = userRepository.save(toUser(userDto));
        return newUser.getUsername();
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserDto newUser) {
        if (!userRepository.existsById(username)) throw new UserNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setEnabled(newUser.getEnabled());
        user.setFirstname(newUser.getFirstname());
        user.setLastname(newUser.getLastname());
        user.setEmail(newUser.getEmail());
        user.setApikey(newUser.getApikey());
        userRepository.save(user);
    }

    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException(username);
        User user = userRepository.findById(username).get();
        UserDto userDto = fromUser(user);
        return userDto.getAuthorities();
    }

    public void addAuthority(String username, String authority) {

        if (!userRepository.existsById(username)) throw new RecordNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException(username);
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }

    public UserDto assignProfilePictureToUser(String username, Long profilePictureId) throws RecordNotFoundException {
        Optional<User> optionalUser = userRepository.findById(username);
        Optional<ProfilePicture> optionalProfilePicture = profilePictureRepository.findById(profilePictureId);

        if (optionalUser.isEmpty() || optionalProfilePicture.isEmpty()) {
            throw new RecordNotFoundException("User or picture not found");
        }

        User user = optionalUser.get();
        ProfilePicture profilePicture = optionalProfilePicture.get();

        user.setProfilePicture(profilePicture);
        userRepository.save(user);

        return fromUser(user);
    }

    public static UserDto fromUser(User user){

        var dto = new UserDto();

        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.firstname = user.getFirstname();
        dto.lastname = user.getLastname();
        dto.enabled = user.isEnabled();
        dto.apikey = user.getApikey();
        dto.email = user.getEmail();
        dto.profilePicture = user.getProfilePicture();

        if (user.getWorkStations().size() > 0 ) {
            dto.workStation = user.getWorkStations().get(user.getWorkStations().size() - 1);
        }
        dto.authorities = user.getAuthorities();

        return dto;
    }

    public User toUser(UserDto userDto) {

        var user = new User();

        user.setUsername(userDto.getUsername());
        user.setPassword((passwordEncoder.encode(userDto.getPassword())));
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEnabled(userDto.getEnabled());
        user.setApikey(userDto.getApikey());
        user.setEmail(userDto.getEmail());
        user.setProfilePicture(userDto.getProfilePicture());

        return user;
    }



}
