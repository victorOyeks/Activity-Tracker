package com.example.activityTracker.service.implementation;

import com.example.activityTracker.DTO.UserDTO;
import com.example.activityTracker.entity.User;
import com.example.activityTracker.repository.UserRepository;
import com.example.activityTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(UserDTO userDTO) {
        User user = new User(userDTO.getEmail(), userDTO.getPassword(), userDTO.getLastName(), userDTO.getFirstName());
        return userRepository.save(user);
    }

    @Override
    public User findUser(UserDTO userDTO) {
        User user =  userRepository.findByEmail(userDTO.getEmail()).get();
        boolean match = user.checkPassword(userDTO.getPassword());
        if (match){
            return user;
        }
        return null;
    }
}