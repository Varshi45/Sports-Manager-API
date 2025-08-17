package com.sports.manager.Repository;

import com.sports.manager.Entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByAdmin_Id(int adminId);
    List<Match> findBySport_Id(int sportId);
}
