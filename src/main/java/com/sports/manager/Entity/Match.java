package com.sports.manager.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "matches")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    private int teamSize;

    private int slotsRemaining;

    private String team1;

    private String team2;

    private Date date;

    private String location;

    @ManyToMany
    @JoinTable(
            name = "match_players",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> players = new ArrayList<>();


    public Match(Admin admin, Sport sport) {
        this.admin = admin;
        this.sport = sport;
        this.teamSize = sport.getTeamSize();
        this.slotsRemaining = sport.getTeamSize();
    }

    public boolean addPlayer(Player player) {
        if (slotsRemaining > 0 && !players.contains(player)) {
            players.add(player);
            slotsRemaining = Math.max(0, slotsRemaining - 1);
            return true;
        }
        return false;
    }

}
