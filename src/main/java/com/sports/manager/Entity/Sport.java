package com.sports.manager.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "sport")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    private String name;

    @Min(value = 1, message = "Team size must be at least 1")
    private int teamSize;

}
