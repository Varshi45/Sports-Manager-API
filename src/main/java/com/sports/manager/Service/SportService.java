package com.sports.manager.Service;

import com.sports.manager.Dto.SportDto;
import com.sports.manager.Entity.Admin;
import com.sports.manager.Entity.Sport;
import com.sports.manager.Repository.SportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportService {
    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    private final SportRepository sportRepository;

    public Sport createSport(SportDto sportDto, Admin admin){
        // ✅ Build Sport entity
        Sport sport = new Sport();
        sport.setName(sportDto.getName());
        sport.setTeamSize(sportDto.getTeamSize());
        Admin safeAdmin = admin;
        safeAdmin.setPassword("dummy");
        sport.setAdmin(safeAdmin);  // auto assign adminId
        return sportRepository.save(sport);
    }

    public List<Sport> getAllSports() {
        return sportRepository.findAll();
    }

    public List<Sport> getAllSportsByAdmin(int adminId) {
        return sportRepository.findByAdmin_Id(adminId);
    }

    public void deleteSport(int sportId, Admin admin) {
        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new RuntimeException("Sport not found with id: " + sportId));

        // 2. Validate ownership (assuming Sport has a createdBy / adminId field)
        if (sport.getAdmin() == null || sport.getAdmin().getId() != admin.getId()) {
            throw new RuntimeException("You are not authorized to delete this sport.");
        }

        // 3. Perform deletion
        sportRepository.delete(sport);
    }
}
