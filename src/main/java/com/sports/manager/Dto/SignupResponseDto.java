package com.sports.manager.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
