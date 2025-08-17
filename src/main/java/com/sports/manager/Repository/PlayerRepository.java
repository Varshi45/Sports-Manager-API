package com.sports.manager.Repository;


import com.sports.manager.Entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findByEmail(String email);
}
