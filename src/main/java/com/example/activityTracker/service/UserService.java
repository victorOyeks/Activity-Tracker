package com.example.activityTracker.service;


import com.example.activityTracker.DTO.UserDTO;
import com.example.activityTracker.entity.User;

public interface UserService {

    User save(UserDTO userDTO);
    User findUser(UserDTO userDTO);
}