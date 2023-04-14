package com.example.activityTracker.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}