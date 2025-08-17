package com.sports.manager.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestDto {
    private String team1;
    private String team2;
    private Date date;
    private String location;
    private int sportId;
}
