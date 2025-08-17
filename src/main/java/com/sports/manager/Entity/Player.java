package com.sports.manager.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
