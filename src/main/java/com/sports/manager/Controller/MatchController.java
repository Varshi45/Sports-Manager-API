package com.sports.manager.Controller;

import com.sports.manager.Dto.MatchRequestDto;
import com.sports.manager.Entity.Admin;
import com.sports.manager.Entity.Match;
import com.sports.manager.Service.MatchService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    private final MatchService matchService;


    @GetMapping("/")
    public List<MatchRequestDto> getAllMatches(){
        return matchService.getAllMatches();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMatch(HttpServletRequest request,
                                         @RequestBody MatchRequestDto matchRequestDto) {
        try {
            Admin admin = (Admin) request.getAttribute("user"); // current logged-in admin
            Match newMatch = matchService.createMatch(matchRequestDto, admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(newMatch);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping("/admin")
    public List<MatchRequestDto> getAllMatchesByAdmin(HttpServletRequest request){
        Admin admin = (Admin) request.getAttribute("user");

        int adminId = admin.getId();

        return matchService.getAllMatchesByAdmin(adminId);
    }

    @GetMapping("/sport/{sportId}")
    public List<MatchRequestDto> getAllMatchesBySport(@PathVariable int sportId){
        return matchService.getAllMatchesBySport(sportId);
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<?> deleteMatch(@PathVariable int matchId, HttpServletRequest request) {
        Admin admin = (Admin) request.getAttribute("user"); // user injected from JWT filter
        try {
            matchService.deleteMatch(matchId, admin);
            return ResponseEntity.ok("{\"message\": \"Sport deleted successfully.\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error deleting sport: " + e.getMessage() + "\"}");
        }
    }



}
