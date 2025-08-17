package com.sports.manager.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNameDto {
    private int id;
    private String role;
    private String firstName;
    private String lastName;
}
