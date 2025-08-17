package com.sports.manager.Repository;

import com.sports.manager.Entity.Admin;
import com.sports.manager.Entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SportRepository extends JpaRepository<Sport, Integer> {
    List<Sport> findByAdmin_Id(int adminId);
}
