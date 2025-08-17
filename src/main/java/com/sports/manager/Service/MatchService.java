package com.sports.manager.Service;

import com.sports.manager.Dto.MatchRequestDto;
import com.sports.manager.Entity.Admin;
import com.sports.manager.Entity.Match;
import com.sports.manager.Entity.Sport;
import com.sports.manager.Repository.MatchRepository;
import com.sports.manager.Repository.SportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    public MatchService(MatchRepository matchRepository, SportRepository sportRepository) {
        this.matchRepository = matchRepository;
        this.sportRepository = sportRepository;
    }

    private final MatchRepository matchRepository;
    private final SportRepository sportRepository;

    public List<Match> getAllMatches(){
        return matchRepository.findAll();
    }

    public Match createMatch(MatchRequestDto dto, Admin admin) {
        // Check if sport exists
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid sport ID"));

        Match newMatch = new Match();
        newMatch.setTeam1(dto.getTeam1());
        newMatch.setTeam2(dto.getTeam2());
        newMatch.setDate(dto.getDate());
        newMatch.setLocation(dto.getLocation());
        newMatch.setSport(sport);
        Admin safeAdmin = admin;
        safeAdmin.setPassword("dummy");
        newMatch.setAdmin(safeAdmin);


        newMatch.setTeamSize(sport.getTeamSize());
        newMatch.setSlotsRemaining(sport.getTeamSize());

        return matchRepository.save(newMatch);
    }

    public List<Match> getAllMatchesByAdmin(int adminId) {
        return matchRepository.findByAdmin_Id(adminId);
    }

    public List<Match> getAllMatchesBySport(int sportId) {
        return matchRepository.findBySport_Id(sportId);
    }

    public void deleteMatch(int matchId, Admin admin) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));

        if (match.getAdmin() == null || match.getAdmin().getId() != admin.getId()) {
            throw new RuntimeException("You are not authorized to delete this sport.");
        }

        matchRepository.delete(match);
    }
}
