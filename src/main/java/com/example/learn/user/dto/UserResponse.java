package com.example.learn.user.dto;

import com.example.learn.user.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {

    private Integer id;
    private String name;
    private String email;
    private UserType type;
}
